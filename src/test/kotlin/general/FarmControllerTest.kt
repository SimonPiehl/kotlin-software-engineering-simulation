package general

import de.unisaarland.cs.se.selab.controller.FarmController
import de.unisaarland.cs.se.selab.controller.PathFinderController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.SowingPlan
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FarmControllerTest {

    private lateinit var data: SimulationData
    private lateinit var pathFinder: PathFinderController
    private lateinit var farmController: FarmController
    private lateinit var farm: Farm

    @BeforeTest
    fun setup() {
        // Initialize simulation data and ticks
        data = SimulationData(999, 19)
        data.setCurrentTick(19)
        data.setYearTick(19)

        // Create machines with diverse actions and plants
        val coord = Coordinate(-1, -1)
        val machines = listOf(
            Machine(0, "harvester", listOf(Action.HARVESTING), listOf(PlantType.WHEAT), 2, coord),
            Machine(1, "irrigator", listOf(Action.IRRIGATING), listOf(PlantType.APPLE), 2, coord),
            Machine(2, "cutter", listOf(Action.CUTTING), listOf(PlantType.APPLE), 2, coord),
            Machine(3, "mower", listOf(Action.MOWING), listOf(PlantType.APPLE), 2, coord),
            Machine(4, "sower", listOf(Action.SOWING), listOf(PlantType.WHEAT), 2, coord),
            Machine(5, "weeder", listOf(Action.WEEDING), listOf(PlantType.WHEAT), 2, coord),
            Machine(6, "harvy", listOf(Action.HARVESTING), listOf(PlantType.APPLE), 2, coord),
            Machine(7, "waterfield", listOf(Action.IRRIGATING), listOf(PlantType.WHEAT), 2, coord)
        )

        // Setup farm with machines and sowing plan
        val sowingPlan = SowingPlan(0, 1, PlantType.WHEAT, listOf(0, 1))
        farm = Farm(0, "FarmTest", machines, mutableListOf(sowingPlan))
        data.addFarm(farm)
        s2(data)
    }
    fun s2(dat: SimulationData) {
        // Create tiles with varied growables and properties
        val tiles = listOf(
            Tile(
                0,
                TileType.FIELD,
                Coordinate(0, 0),
                false,
                Direction.NORTHEAST,
                false,
                Growable(null, listOf(PlantType.WHEAT), 10000).apply {
                    setMoistureExposure(50)
                }
            ),
            Tile(
                1,
                TileType.FIELD,
                Coordinate(2, 0),
                false,
                Direction.NORTHEAST,
                false,
                Growable(null, listOf(PlantType.WHEAT), 10000).apply {
                    setMoistureExposure(50)
                }
            ),
            Tile(
                2,
                TileType.PLANTATION,
                Coordinate(2, 2),
                false,
                Direction.NORTHEAST,
                false,
                Growable(PlantType.APPLE, listOf(PlantType.APPLE), 1000).apply {
                    setCropsExpected(100)
                    setMoistureExposure(50)
                }
            ),
            Tile(
                3,
                TileType.PLANTATION,
                Coordinate(2, 4),
                false,
                Direction.NORTHEAST,
                false,
                Growable(PlantType.APPLE, listOf(PlantType.APPLE), 1000).apply {
                    setCropsExpected(1000)
                    setMoistureExposure(50)
                }
            )
        )

        // Add tiles to map
        tiles.forEach { dat.getMap().addTile(it, 0) }

        // Setup PathFinder with simulation data
        pathFinder = PathFinderController(dat)

        // Create FarmController with dependencies
        farmController = FarmController(dat, pathFinder)
    }

    @Test
    fun testFarmControllerFarmActions() {
        // Perform farming actions
        data.setCurrentTick(3)
        data.setYearTick(3)
        farmController.farmsAction()

        val c2 = data.getMap().getTileByID(2)?.getGrowable()?.getWasCutAtTick()
        val c3 = data.getMap().getTileByID(3)?.getGrowable()?.getLastTickWorkedOn()
        assert(c2 == 3)
        assert(c2 == c3)

        data.setCurrentTick(19)
        data.setYearTick(19)
        farmController.farmsAction()

        // Check harvest effect: tile 2 was harvested at current tick
        val tile4Growable = data.getMap().getTileByID(0)?.getGrowable()
        assertNotNull(tile4Growable, "Tile 2 growable should not be null")
        assertEquals(19, tile4Growable.getWasSowedAtTick(), "Tile 2 should be sowed at tick 19")
        assertEquals(1500000, tile4Growable.getCropsExpected(), "Tile 2 crops expected reset after harvest")

        // Check irrigation effect: tile 3 moisture exposure updated
        // farmController.farmsAction()
        val tile3Growable = data.getMap().getTileByID(2)?.getGrowable()
        assertNotNull(tile3Growable, "Tile 3 growable should not be null")
        assertEquals(19, tile3Growable.getLiesFallowSinceTick(), "Tile 3 should be harvested at tick 19")

        // Check sowing effect: tile 0 current plant set to WHEAT
        val tile0Growable = data.getMap().getTileByID(0)?.getGrowable()
        assertNotNull(tile0Growable, "Tile 0 growable should not be null")
        assertEquals(PlantType.WHEAT, tile0Growable.getCurrentPlant(), "Tile 0 should be planted with WHEAT")

        // Check machine states: verify at least one machine performed an action
        val performedActions = farm.getMachines()
            .mapNotNull { it.getPerformedAction() }

        assertTrue(performedActions.isEmpty(), "machines were reset")

        // Advance tick and rerun to verify consistent behavior over time
        data.setCurrentTick(21)
        data.setYearTick(21)
        farmController.farmsAction()

        // Example: check harvest count stayed logical or moisture updated again
        val tile2 = data.getMap().getTileByID(2)?.getGrowable()?.getMoistureExposure()
        val tile3 = data.getMap().getTileByID(3)?.getGrowable()?.getMoistureExposure()
        val max = data.getMap().getTileByID(2)?.getGrowable()?.getMaxMoisture()
        assert(tile2 == tile3)
        assertTrue(tile3 == max)

        val w0 = data.getMap().getTileByID(0)?.getGrowable()?.getMoistureExposure()
        val w1 = data.getMap().getTileByID(1)?.getGrowable()?.getMoistureExposure()
        val ma = data.getMap().getTileByID(1)?.getGrowable()?.getMaxMoisture()
        assert(w0 == w1)
        assertTrue(w1 == ma)

        data.setCurrentTick(23)
        data.setYearTick(23)
        farmController.farmsAction()

        data.setCurrentTick(11)
        data.setYearTick(11)
        farmController.farmsAction()

        val tile0 = data.getMap().getTileByID(0)?.getGrowable()?.getWasHarvestedAtTick()
        val tile1 = data.getMap().getTileByID(1)?.getGrowable()?.getCropsExpected()
        assert(tile0 == 11)
        assert(tile1 == 0)

        // Add any additional assertions based on your business rules and needs
    }
}
