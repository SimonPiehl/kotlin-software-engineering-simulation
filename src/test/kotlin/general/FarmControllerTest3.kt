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

class FarmControllerTest3 {

    private lateinit var data: SimulationData
    private lateinit var pathFinder: PathFinderController
    private lateinit var farmController: FarmController
    private lateinit var farm: Farm

    @BeforeTest
    fun setup() {
        // Initialize simulation data and ticks
        data = SimulationData(999, 11)
        data.setCurrentTick(11)
        data.setYearTick(11)

        // Create machines with diverse actions and plants
        val coord = Coordinate(-1, -1)
        val machines = listOf(
            Machine(2, "cutter", listOf(Action.CUTTING), listOf(PlantType.APPLE), 2, coord),
            Machine(3, "mower", listOf(Action.MOWING), listOf(PlantType.ALMOND), 2, coord),
            Machine(4, "sower", listOf(Action.SOWING), listOf(PlantType.WHEAT), 2, coord),
            Machine(5, "weeder", listOf(Action.WEEDING), listOf(PlantType.WHEAT), 2, coord),
            Machine(6, "harvy", listOf(Action.HARVESTING), listOf(PlantType.APPLE, PlantType.ALMOND), 2, coord)
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
                Growable(PlantType.WHEAT, listOf(PlantType.WHEAT), 10000).apply {
                    setMoistureExposure(50)
                    setCropsExpected(100)
                    setWasSowedAtTick(19)
                }
            ),
            Tile(
                1,
                TileType.FIELD,
                Coordinate(2, 0),
                false,
                Direction.NORTHEAST,
                false,
                Growable(PlantType.WHEAT, listOf(PlantType.WHEAT), 10000).apply {
                    setMoistureExposure(50)
                    setCropsExpected(100)
                    setWasSowedAtTick(19)
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
                Growable(PlantType.ALMOND, listOf(PlantType.ALMOND), 1000).apply {
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

        data.setCurrentTick(18)
        data.setYearTick(18)
        farmController.farmsAction()
        // Add any additional assertions based on your business rules and needs
    }
}
