package controller

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.SoilMoistureAndSunController
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.SunConstants
import de.unisaarland.cs.se.selab.util.YearTicks
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class SoilMoistureAndSunControllerTest {

    private lateinit var data: SimulationData
    private lateinit var controller: SoilMoistureAndSunController
    private lateinit var tile: Tile
    private lateinit var growable: Growable
    private lateinit var map: de.unisaarland.cs.se.selab.model.Map

    @BeforeEach
    fun setup() {
        data = mock()
        tile = mock()
        growable = mock()
        map = mock()

        whenever(data.getMap()).thenReturn(map)
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)

        controller = SoilMoistureAndSunController(data)
    }

    @Test
    fun `reduceSoilMoistureAndAdaptSun should reduce moisture and adapt sun`() {
        whenever(map.getAllGrowable()).thenReturn(listOf(tile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JUNE)

        controller.reduceSoilMoistureAndAdaptSun()

        // check moisture reduction
        verify(tile).reduceMoisture()
        // check sun adaption
        verify(growable).setSunlightExposure(SunConstants.JUNE_MONTHLY_SUN)
        // check reset of incidents
        verify(growable).resetIncidentsThisTick()
    }

    @Test
    fun `resetPlantationsInNovember should reset plantation properties`() {
        // Arrange
        val plantationTile = mock<Tile>()
        val plantationGrowable = mock<Growable>()

        whenever(plantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(plantationTile.getGrowable()).thenReturn(plantationGrowable)
        whenever(plantationGrowable.getPermanentDisabled()).thenReturn(false)
        whenever(plantationGrowable.getCurrentPlant()).thenReturn(PlantType.APPLE)

        whenever(map.getAllGrowable()).thenReturn(listOf(plantationTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_NOVEMBER)

        // Act
        controller.reduceSoilMoistureAndAdaptSun()

        // Assert - Verify that all plantation properties were reset
        verify(plantationGrowable).setWasCutAtTick(-1)
        verify(plantationGrowable).setWasHarvestedAtTick(-1)
        verify(plantationGrowable).setWasMowedAtTick(-1)
        verify(plantationGrowable).setCropsExpected(1700000)

        // Also verify that normal moisture reduction and sun adaptation still happened
        verify(plantationTile).reduceMoisture()
        verify(plantationGrowable).setSunlightExposure(SunConstants.NOVEMBER_MONTHLY_SUN)
        verify(plantationGrowable).resetIncidentsThisTick()
    }

    @Test
    fun `resetPlantationsInNovember should not reset permanently disabled plantations`() {
        // Arrange
        val disabledPlantationTile = mock<Tile>()
        val disabledPlantationGrowable = mock<Growable>()

        whenever(disabledPlantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(disabledPlantationTile.getGrowable()).thenReturn(disabledPlantationGrowable)
        whenever(disabledPlantationGrowable.getPermanentDisabled()).thenReturn(true)

        whenever(map.getAllGrowable()).thenReturn(listOf(disabledPlantationTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_NOVEMBER)

        // Act
        controller.reduceSoilMoistureAndAdaptSun()

        // Assert - Verify that no reset methods were called for permanently disabled plantations
        verify(disabledPlantationGrowable, never()).setWasCutAtTick(-1)
        verify(disabledPlantationGrowable, never()).setWasHarvestedAtTick(-1)
        verify(disabledPlantationGrowable, never()).setWasMowedAtTick(-1)
        verify(disabledPlantationGrowable, never()).setCropsExpected(any())

        // But normal moisture reduction and sun adaptation should still happen
        verify(disabledPlantationTile).reduceMoisture()
        verify(disabledPlantationGrowable).setSunlightExposure(SunConstants.NOVEMBER_MONTHLY_SUN)
    }

    @Test
    fun `resetPlantationsInNovember should not reset non-plantation tiles`() {
        // Arrange
        val fieldTile = mock<Tile>()
        val fieldGrowable = mock<Growable>()

        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.getGrowable()).thenReturn(fieldGrowable)

        whenever(map.getAllGrowable()).thenReturn(listOf(fieldTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_NOVEMBER)

        // Act
        controller.reduceSoilMoistureAndAdaptSun()

        // Assert - Verify that no reset methods were called for field tiles
        verify(fieldGrowable, never()).setWasCutAtTick(-1)
        verify(fieldGrowable, never()).setWasHarvestedAtTick(-1)
        verify(fieldGrowable, never()).setWasMowedAtTick(-1)
        verify(fieldGrowable, never()).setCropsExpected(any())

        // But normal moisture reduction and sun adaptation should still happen
        verify(fieldTile).reduceMoisture()
        verify(fieldGrowable).setSunlightExposure(SunConstants.NOVEMBER_MONTHLY_SUN)
    }

    @Test
    fun `resetPlantationsInNovember should only be called in early november`() {
        // Arrange
        val plantationTile = mock<Tile>()
        val plantationGrowable = mock<Growable>()

        whenever(plantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(plantationTile.getGrowable()).thenReturn(plantationGrowable)
        whenever(plantationGrowable.getPermanentDisabled()).thenReturn(false)

        whenever(map.getAllGrowable()).thenReturn(listOf(plantationTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.LATE_NOVEMBER) // Not early november!

        // Act
        controller.reduceSoilMoistureAndAdaptSun()

        // Assert - Verify that no reset methods were called when it's not early november
        verify(plantationGrowable, never()).setWasCutAtTick(-1)
        verify(plantationGrowable, never()).setWasHarvestedAtTick(-1)
        verify(plantationGrowable, never()).setWasMowedAtTick(-1)
        verify(plantationGrowable, never()).setCropsExpected(any())
    }

    @Test
    fun `reduceSoilMoistureAndAdaptSun should count under-threshold fields`() {
        whenever(map.getAllGrowable()).thenReturn(listOf(tile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JULY)

        whenever(tile.amIBelowMoistureThreshold()).thenReturn(true)
        whenever(tile.amIPermanentlyDisabled()).thenReturn(false)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(tile).reduceMoisture()
        verify(growable).setSunlightExposure(SunConstants.JULY_MONTHLY_SUN)
    }

    @Test
    fun `adaptSunOfGrowable should not set sunlight when sunValue is null`() {
        val invalidYearTick = 999
        val tileWithGrowable = mock<Tile>()
        val growable = mock<Growable>()

        whenever(tileWithGrowable.getGrowable()).thenReturn(growable)
        whenever(map.getAllGrowable()).thenReturn(listOf(tileWithGrowable))
        whenever(data.getYearTick()).thenReturn(invalidYearTick)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(growable, never()).setSunlightExposure(any())
        verify(tileWithGrowable).reduceMoisture()
        verify(growable).resetIncidentsThisTick()
    }

    @Test
    fun `resetPlantationsInNovember should handle null current plant`() {
        val plantationTile = mock<Tile>()
        val plantationGrowable = mock<Growable>()

        whenever(plantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(plantationTile.getGrowable()).thenReturn(plantationGrowable)
        whenever(plantationGrowable.getPermanentDisabled()).thenReturn(false)
        whenever(plantationGrowable.getCurrentPlant()).thenReturn(null)

        whenever(map.getAllGrowable()).thenReturn(listOf(plantationTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_NOVEMBER)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(plantationGrowable).setWasCutAtTick(-1)
        verify(plantationGrowable).setWasHarvestedAtTick(-1)
        verify(plantationGrowable).setWasMowedAtTick(-1)
        verify(plantationGrowable).setCropsExpected(0)
    }

    @Test
    fun `reduceMoistureOfGrowable should count under-threshold plantations`() {
        // Arrange
        val plantationTile = mock<Tile>()
        val plantationGrowable = mock<Growable>()

        whenever(plantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(plantationTile.getGrowable()).thenReturn(plantationGrowable)
        whenever(plantationTile.amIBelowMoistureThreshold()).thenReturn(true)
        whenever(plantationTile.amIPermanentlyDisabled()).thenReturn(false)

        whenever(map.getAllGrowable()).thenReturn(listOf(plantationTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JUNE)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(plantationTile).reduceMoisture()
        verify(plantationGrowable).setSunlightExposure(SunConstants.JUNE_MONTHLY_SUN)
    }

    @Test
    fun `reduceMoistureOfGrowable should not count permanently disabled tiles`() {
        val disabledTile = mock<Tile>()
        val disabledGrowable = mock<Growable>()

        whenever(disabledTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(disabledTile.getGrowable()).thenReturn(disabledGrowable)
        whenever(disabledTile.amIBelowMoistureThreshold()).thenReturn(true)
        whenever(disabledTile.amIPermanentlyDisabled()).thenReturn(true)

        whenever(map.getAllGrowable()).thenReturn(listOf(disabledTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JUNE)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(disabledTile).reduceMoisture()
        verify(disabledGrowable).setSunlightExposure(SunConstants.JUNE_MONTHLY_SUN)
    }

    @Test
    fun `reduceMoistureOfGrowable should not count tiles above moisture threshold`() {
        val normalTile = mock<Tile>()
        val normalGrowable = mock<Growable>()

        whenever(normalTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(normalTile.getGrowable()).thenReturn(normalGrowable)
        whenever(normalTile.amIBelowMoistureThreshold()).thenReturn(false)
        whenever(normalTile.amIPermanentlyDisabled()).thenReturn(false)

        whenever(map.getAllGrowable()).thenReturn(listOf(normalTile))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JUNE)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(normalTile).reduceMoisture()
        verify(normalGrowable).setSunlightExposure(SunConstants.JUNE_MONTHLY_SUN)
    }

    @Test
    fun `adaptSunOfGrowable should handle tiles without growable`() {
        val tileWithoutGrowable = mock<Tile>()

        whenever(tileWithoutGrowable.getGrowable()).thenReturn(null)
        whenever(map.getAllGrowable()).thenReturn(listOf(tileWithoutGrowable))
        whenever(data.getYearTick()).thenReturn(YearTicks.EARLY_JUNE)

        controller.reduceSoilMoistureAndAdaptSun()

        verify(tileWithoutGrowable).reduceMoisture()
    }
}
