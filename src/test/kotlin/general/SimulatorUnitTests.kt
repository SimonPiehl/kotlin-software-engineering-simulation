package general

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.Simulator
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Statistic
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class SimulatorUnitTests {

    private lateinit var data: SimulationData
    private lateinit var simulator: Simulator

    private val farm: Farm = mock()
    private val statistic: Statistic = mock()
    private val map: Map = mock()
    private val tile: Tile = mock()
    private val growable: Growable = mock()

    @BeforeEach
    fun setup() {
        data = mock()

        var tick = 0
        whenever(data.getMaxTick()).thenReturn(2)
        whenever(data.getCurrentTick()).thenAnswer { tick }
        whenever(data.getYearTick()).thenReturn(1)

        doAnswer { invocation ->
            tick = (invocation.arguments[0] ?: error("setCurrentTick argument is null")) as Int
            null
        }.whenever(data).setCurrentTick(anyInt())

        doNothing().whenever(data).setYearTick(anyInt())

        // Farm + Statistic mocks
        whenever(data.getFarms()).thenReturn(listOf(farm) as? MutableList<Farm>?)
        whenever(farm.getID()).thenReturn(42)
        whenever(farm.getStatistic()).thenReturn(statistic)
        whenever(statistic.computeCombinedHarvest()).thenReturn(0)
        whenever(statistic.getPotatoAmount()).thenReturn(0)
        whenever(statistic.getWheatAmount()).thenReturn(0)
        whenever(statistic.getOatAmount()).thenReturn(0)
        whenever(statistic.getPumpkinAmount()).thenReturn(0)
        whenever(statistic.getAppleAmount()).thenReturn(0)
        whenever(statistic.getGrapeAmount()).thenReturn(0)
        whenever(statistic.getAlmondAmount()).thenReturn(0)
        whenever(statistic.getCherryAmount()).thenReturn(0)

        // Map mocks
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getAllGrowable()).thenReturn(listOf(tile))
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(growable.getCropsExpected()).thenReturn(0)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)

        simulator = Simulator(data)
    }

    @Test
    fun `simulate should iterate until maxTick and calculate statistics`() {
        simulator.simulate()

        verify(data, atLeast(3)).setCurrentTick(anyInt())
        verify(data, atLeast(2)).setYearTick(anyInt())

        verify(statistic).computeCombinedHarvest()

        verify(map, atLeastOnce()).getAllGrowable()
        verify(tile, atLeastOnce()).getGrowable()
        verify(growable, atLeastOnce()).getCropsExpected()
    }
}
