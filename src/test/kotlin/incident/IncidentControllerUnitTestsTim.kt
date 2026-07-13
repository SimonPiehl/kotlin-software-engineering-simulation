package incident

import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BeeHappy
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Duration
import de.unisaarland.cs.se.selab.util.GeneralConstants
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

/**
 * Incident controller unit tests
 *
 * tests behavior of City Expansions, BrokenMachine, Drought, BeeHappy & AnimalAttack
 */
class IncidentControllerUnitTestsTim {
    lateinit var data: SimulationData
    lateinit var map: Map
    lateinit var coo: Coordinate

    @BeforeEach
    fun setup() {
        data = mock()
        map = mock()
        coo = mock()
    }

    // ------------------ CITY EXPANSION ------------------
    @Test
    fun testCityExpansion() {
        val tile: Tile = mock()
        val incident: CityExpansion = mock()
        // Incident
        whenever(incident.getLocation()).thenReturn(1)
        // Data
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf(0 to mutableListOf(incident))
        )
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)
        whenever(data.getClouds()).thenReturn(mutableListOf<Cloud>())
        // Map
        whenever(map.getTileByID(1)).thenReturn(tile)
        whenever(map.getTilesByCoordinates(emptyList())).thenReturn(mutableListOf<Tile>())
        // Tile
        whenever(tile.getTileType()).thenReturn(TileType.ROAD)
        whenever(tile.getCoordinate()).thenReturn(coo)
        whenever(tile.getID()).thenReturn(1)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        verify(tile).changeToVillage()
    }

    @Test
    fun testCityExpansionZwo() {
        val tile: Tile = mock()
        val incident: CityExpansion = mock()
        // Incident
        whenever(incident.getLocation()).thenReturn(1)
        // Data
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf(0 to mutableListOf(incident))
        )
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)
        whenever(data.getClouds()).thenReturn(mutableListOf<Cloud>())
        // Map
        whenever(map.getTileByID(1)).thenReturn(tile)
        whenever(map.getTilesByCoordinates(emptyList())).thenReturn(mutableListOf<Tile>())
        // Tile
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile.getCoordinate()).thenReturn(coo)
        whenever(tile.getID()).thenReturn(1)
        // Additional for FIELD
        whenever(map.getFarmIDbyField(tile)).thenReturn(1)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        verify(map).removeTile(tile, 1)
        verify(tile).changeToVillage()
        verify(map).addTile(tile, null)
    }

    @Test
    fun testCityExpansionWithCloud() {
        val tile: Tile = mock()
        val incident: CityExpansion = mock()
        // Cloud
        val cloud: Cloud = mock()
        whenever(cloud.getLocation()).thenReturn(coo)
        // Incident
        whenever(incident.getLocation()).thenReturn(1)
        // Data
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf(0 to mutableListOf(incident))
        )
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)
        whenever(data.getClouds()).thenReturn(mutableListOf<Cloud>(cloud))
        // Map
        whenever(map.getTileByID(1)).thenReturn(tile)
        whenever(map.getTilesByCoordinates(emptyList())).thenReturn(mutableListOf<Tile>())
        // Tile
        whenever(tile.getTileType()).thenReturn(TileType.ROAD)
        whenever(tile.getCoordinate()).thenReturn(coo)
        whenever(tile.getID()).thenReturn(1)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        verify(tile).changeToVillage()
        // Cloud
        verify(data).deleteCloud(cloud)
    }

    // ------------------ BROKEN MACHINE ------------------
    @Test
    fun testBrokenMachine_FirstTimeBreak() {
        val machine: Machine = mock()
        val duration: Duration = mock()
        val incident: BrokenMachine = mock()
        val tile: Tile = mock()

        // Incident
        whenever(incident.getMachine()).thenReturn(machine)
        whenever(incident.getDuration()).thenReturn(duration)
        whenever(incident.getID()).thenReturn(42)

        // Duration
        whenever(duration.getEndTick()).thenReturn(5)

        // Machine not broken yet
        whenever(machine.getIsBroken()).thenReturn(false)
        whenever(machine.getCurrentLocation()).thenReturn(coo)
        whenever(machine.getID()).thenReturn(100)

        // Data
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(incident)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map
        whenever(map.getTileByCoordinate(coo)).thenReturn(tile)
        whenever(tile.getID()).thenReturn(7)

        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify machine set to broken
        verify(machine).setIsBroken(true)
        // Verify tile lookup & logging
        verify(map).getTileByCoordinate(coo)
    }

    /** Test broken machine incident when another is already there, and it needs to be checked which lasts longer */
    @Test
    fun testBrokenMachineOverridingEachOther() {
        val machine: Machine = mock()
        val tile: Tile = mock()
        val duration1: Duration = mock()
        val duration2: Duration = mock()
        val incident1: BrokenMachine = mock()
        val incident2: BrokenMachine = mock()

        // Machine setup
        whenever(machine.getID()).thenReturn(100)
        whenever(machine.getCurrentLocation()).thenReturn(coo)

        // Tile setup
        whenever(map.getTileByCoordinate(coo)).thenReturn(tile)
        whenever(tile.getID()).thenReturn(7)

        // Incident1: short duration, tick 0
        whenever(incident1.getMachine()).thenReturn(machine)
        whenever(incident1.getDuration()).thenReturn(duration1)
        whenever(duration1.getEndTick()).thenReturn(5)
        whenever(incident1.getID()).thenReturn(1)

        // Incident2: longer duration, tick 1
        whenever(incident2.getMachine()).thenReturn(machine)
        whenever(incident2.getDuration()).thenReturn(duration2)
        whenever(duration2.getEndTick()).thenReturn(10)
        whenever(incident2.getID()).thenReturn(2)

        // First tick: incident1 arrives, machine not broken yet
        whenever(machine.getIsBroken()).thenReturn(false)
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(incident1)))
        whenever(data.getMap()).thenReturn(map)

        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify machine marked broken
        verify(machine).setIsBroken(true)

        // Second tick: new incident arrives for same machine, now already broken
        whenever(machine.getIsBroken()).thenReturn(true)
        whenever(data.getCurrentTick()).thenReturn(1)
        whenever(data.getIncidents()).thenReturn(mutableMapOf(1 to mutableListOf(incident2)))

        controller.checkIncidents()

        // We know overrideBrokenMachine path executed because:
        // setIsBroken is NOT called again, tile was looked up again for logging
        verify(machine, times(1)).setIsBroken(true) // only first time
        verify(map, atLeastOnce()).getTileByCoordinate(coo)
    }

    // ------------------ DROUGHT ------------------
    @Test
    fun testDrought_AffectsFieldsAndPlantations() {
        val drought: Drought = mock()
        val centerTile: Tile = mock()
        val fieldTile: Tile = mock()
        val plantationTile: Tile = mock()
        val roadTile: Tile = mock() // should not be affected
        val growableField: Growable = mock()
        val growablePlantation: Growable = mock()
        val coo: Coordinate = mock()

        // Incident setup
        whenever(drought.getLocation()).thenReturn(1)
        whenever(drought.getRadius()).thenReturn(1)
        whenever(drought.getID()).thenReturn(77)

        // Data setup
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(drought)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map setup
        whenever(map.getTileByID(1)).thenReturn(centerTile)

        // Coordinate + neighbours
        whenever(centerTile.getCoordinate()).thenReturn(coo)
        val neighbourCoos = listOf(Coordinate(0, 1), Coordinate(1, 0))
        whenever(coo.getNeighbours(1)).thenReturn(neighbourCoos)

        whenever(map.getTilesByCoordinates(neighbourCoos))
            .thenReturn(mutableListOf(fieldTile, plantationTile, roadTile))

        // Center tile should also be included
        whenever(centerTile.getTileType()).thenReturn(TileType.ROAD) // not affected
        whenever(centerTile.getID()).thenReturn(100)

        // Field tile setup
        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.getID()).thenReturn(101)
        whenever(fieldTile.getGrowable()).thenReturn(growableField)
        whenever(growableField.getCropsExpected()).thenReturn(5)

        // Plantation tile setup
        whenever(plantationTile.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(plantationTile.getID()).thenReturn(102)
        whenever(plantationTile.getGrowable()).thenReturn(growablePlantation)
        whenever(growablePlantation.getCropsExpected()).thenReturn(0) // no harvest change expected

        // Road tile setup (ignored)
        whenever(roadTile.getTileType()).thenReturn(TileType.ROAD)
        whenever(roadTile.getID()).thenReturn(103)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify drought applied
        verify(data).addTilesWhereDrought(fieldTile)
        verify(data).addTilesWhereDrought(plantationTile)

        // Growables moisture reset
        verify(growableField).setMoistureExposure(0)
        verify(growablePlantation).setMoistureExposure(0)

        // Harvest changed only for field (cropsExpected > 0)
        verify(data).addTilesHarvestChanged(fieldTile, GeneralConstants.DROUGHT_EFFECT_ZERO)
        verify(data, never()).addTilesHarvestChanged(eq(plantationTile), any())
    }

    @Test
    fun testDrought_NoAffectedTiles() {
        val drought: Drought = mock()
        val centerTile: Tile = mock()
        val coo: Coordinate = mock()

        // Incident setup
        whenever(drought.getLocation()).thenReturn(1)
        whenever(drought.getRadius()).thenReturn(1)

        // Data setup
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(drought)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map setup
        whenever(map.getTileByID(1)).thenReturn(centerTile)
        whenever(centerTile.getCoordinate()).thenReturn(coo)
        whenever(coo.getNeighbours(1)).thenReturn(emptyList())
        whenever(map.getTilesByCoordinates(emptyList())).thenReturn(mutableListOf())

        // No fields or plantations in range
        whenever(centerTile.getTileType()).thenReturn(TileType.ROAD)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Nothing should be added
        verify(data, never()).addTilesWhereDrought(any())
        verify(data, never()).addTilesHarvestChanged(any(), any())
    }

    // ------------------ BEE HAPPY ------------------
    @Test
    fun testBeeHappyAppliesEffectToField() {
        val beeIncident: BeeHappy = mock()
        val centerTile: Tile = mock()
        val meadowTile: Tile = mock()
        val fieldTile: Tile = mock()
        val growable: Growable = mock()

        val cooCenter: Coordinate = mock()
        val cooMeadow: Coordinate = mock()
        val cooField: Coordinate = mock()

        // Incident setup
        whenever(beeIncident.getLocation()).thenReturn(1)
        whenever(beeIncident.getRadius()).thenReturn(1)
        whenever(beeIncident.getEffect()).thenReturn(20)
        whenever(beeIncident.getID()).thenReturn(88)

        // Data setup
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(beeIncident)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getYearTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map setup
        whenever(map.getTileByID(1)).thenReturn(centerTile)

        // Center coordinate
        whenever(centerTile.getCoordinate()).thenReturn(cooCenter)
        whenever(cooCenter.getNeighbours(1)).thenReturn(listOf(cooMeadow))

        // Radius neighbours include meadow
        whenever(map.getTilesByCoordinates(listOf(cooMeadow))).thenReturn(mutableListOf(meadowTile))
        whenever(centerTile.getTileType()).thenReturn(TileType.ROAD)

        // Meadow tile
        whenever(meadowTile.getTileType()).thenReturn(TileType.MEADOW)
        whenever(meadowTile.getCoordinate()).thenReturn(cooMeadow)

        // Tiles around meadow (radius 2) include a field
        whenever(cooMeadow.getNeighbours(2)).thenReturn(listOf(cooField))
        whenever(map.getTilesByCoordinates(listOf(cooField))).thenReturn(mutableListOf(fieldTile))

        // Field tile setup
        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.canIBePollinated(0, 0)).thenReturn(true)
        whenever(fieldTile.getID()).thenReturn(200)
        whenever(fieldTile.getGrowable()).thenReturn(growable)

        // Growable setup
        whenever(growable.getCurrentPlant()).thenReturn(PlantType.WHEAT)
        whenever(growable.getCropsExpected()).thenReturn(5)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify
        verify(data).addTilesHarvestChanged(fieldTile, 1.2)
    }

    @Test
    fun testBeeHappySkipsPotato() {
        val beeIncident: BeeHappy = mock()
        val centerTile: Tile = mock()
        val meadowTile: Tile = mock()
        val fieldTile: Tile = mock()
        val growable: Growable = mock()

        val cooCenter: Coordinate = mock()
        val cooMeadow: Coordinate = mock()
        val cooField: Coordinate = mock()

        // Incident setup
        whenever(beeIncident.getLocation()).thenReturn(1)
        whenever(beeIncident.getRadius()).thenReturn(1)
        whenever(beeIncident.getEffect()).thenReturn(50) // 50%
        whenever(beeIncident.getID()).thenReturn(99)
        whenever(beeIncident.toString()).thenReturn("BeeHappyPotato")

        // Data setup
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(beeIncident)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getYearTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map setup
        whenever(map.getTileByID(1)).thenReturn(centerTile)
        whenever(centerTile.getCoordinate()).thenReturn(cooCenter)
        whenever(cooCenter.getNeighbours(1)).thenReturn(listOf(cooMeadow))
        whenever(map.getTilesByCoordinates(listOf(cooMeadow))).thenReturn(mutableListOf(meadowTile))

        // Meadow
        whenever(meadowTile.getTileType()).thenReturn(TileType.MEADOW)
        whenever(meadowTile.getCoordinate()).thenReturn(cooMeadow)
        whenever(cooMeadow.getNeighbours(2)).thenReturn(listOf(cooField))
        whenever(map.getTilesByCoordinates(listOf(cooField))).thenReturn(mutableListOf(fieldTile))

        // Field tile (potato → should skip)
        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.canIBePollinated(0, 0)).thenReturn(true)
        whenever(fieldTile.getID()).thenReturn(201)
        whenever(fieldTile.getGrowable()).thenReturn(growable)

        whenever(growable.getCurrentPlant()).thenReturn(PlantType.POTATO)
        whenever(growable.getCropsExpected()).thenReturn(5)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify no harvest change because potato
        verify(data, never()).addTilesHarvestChanged(any(), any())
    }

    @Test
    fun testBeeHappyNoPollination() {
        val beeIncident: BeeHappy = mock()
        val centerTile: Tile = mock()
        val meadowTile: Tile = mock()
        val fieldTile: Tile = mock()

        val cooCenter: Coordinate = mock()
        val cooMeadow: Coordinate = mock()
        val cooField: Coordinate = mock()

        // Incident setup
        whenever(beeIncident.getLocation()).thenReturn(1)
        whenever(beeIncident.getRadius()).thenReturn(1)
        whenever(beeIncident.getEffect()).thenReturn(30)
        whenever(beeIncident.getID()).thenReturn(100)
        whenever(beeIncident.toString()).thenReturn("BeeHappyNoPollination")

        // Data setup
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(beeIncident)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getYearTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map setup
        whenever(map.getTileByID(1)).thenReturn(centerTile)
        whenever(centerTile.getCoordinate()).thenReturn(cooCenter)
        whenever(cooCenter.getNeighbours(1)).thenReturn(listOf(cooMeadow))
        whenever(map.getTilesByCoordinates(listOf(cooMeadow))).thenReturn(mutableListOf(meadowTile))

        // Meadow
        whenever(meadowTile.getTileType()).thenReturn(TileType.MEADOW)
        whenever(meadowTile.getCoordinate()).thenReturn(cooMeadow)
        whenever(cooMeadow.getNeighbours(2)).thenReturn(listOf(cooField))
        whenever(map.getTilesByCoordinates(listOf(cooField))).thenReturn(mutableListOf(fieldTile))

        // tile that cannot be pollinated
        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.canIBePollinated(0, 0)).thenReturn(false)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // No effect applied
        verify(data, never()).addTilesHarvestChanged(any(), any())
    }

    // ------------------ ANIMAL ATTACK ------------------
    @Test
    fun testAnimalAttackFieldAffected() {
        val incident: AnimalAttack = mock()
        val forestTile: Tile = mock()
        val fieldTile: Tile = mock()
        val growable: Growable = mock()

        val cooForest: Coordinate = mock()
        val cooField: Coordinate = mock()

        // Incident
        whenever(incident.getLocation()).thenReturn(1)
        whenever(incident.getRadius()).thenReturn(1)
        whenever(incident.getID()).thenReturn(77)

        // data
        whenever(data.getIncidents()).thenReturn(mutableMapOf(0 to mutableListOf(incident)))
        whenever(data.getCurrentTick()).thenReturn(0)
        whenever(data.getYearTick()).thenReturn(0)
        whenever(data.getMap()).thenReturn(map)

        // Map
        whenever(map.getTileByID(1)).thenReturn(forestTile)
        whenever(forestTile.getCoordinate()).thenReturn(cooForest)
        whenever(cooForest.getNeighbours(1)).thenReturn(listOf(cooField))
        whenever(map.getTilesByCoordinates(listOf(cooField))).thenReturn(mutableListOf(fieldTile))

        // Forest tile
        whenever(forestTile.getTileType()).thenReturn(TileType.FOREST)

        // Field tile
        whenever(fieldTile.getTileType()).thenReturn(TileType.FIELD)
        whenever(fieldTile.getID()).thenReturn(10)
        whenever(fieldTile.getGrowable()).thenReturn(growable)
        whenever(growable.getCropsExpected()).thenReturn(5)

        // Act
        val controller = IncidentController(data)
        controller.checkIncidents()

        // Verify FIELD -50% harvest reduction
        verify(data).addTilesHarvestChanged(fieldTile, GeneralConstants.ANIMAL_ATTACK_MUL_50)
    }
}
