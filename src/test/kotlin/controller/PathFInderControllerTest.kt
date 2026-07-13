package controller

import de.unisaarland.cs.se.selab.controller.PathFinderController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class PathFInderControllerTest {

    private lateinit var data: SimulationData
    private lateinit var pathFinderController: PathFinderController

    @BeforeEach
    fun setup() {
        val shedTile = Tile(1, TileType.FARMSTEAD, Coordinate(-1, -1), false, Direction.NONE, true, null)
        val growable = Growable(PlantType.POTATO, 5000)
        val fieldTile = Tile(2, TileType.FIELD, Coordinate(0, 2), false, Direction.NONE, false, growable)
        val villageTile = Tile(3, TileType.VILLAGE, Coordinate(0, 0), false, Direction.NONE, false, null)
        val machine = Machine(
            0,
            "kleiner roter Traktor",
            listOf(Action.CUTTING),
            listOf(PlantType.APPLE),
            7,
            Coordinate(-1, -1)
        )
        val farm = Farm(3, "Fancy Farm", listOf(machine), mutableListOf())

        data = SimulationData(50, 1)

        data.addTile(shedTile, 3)
        data.addTile(fieldTile, 3)
        data.addTile(villageTile, null)
        data.addFarm(farm)

        pathFinderController = PathFinderController(data)
    }

    @Test
    fun `moveToShedUnloadedMachine machine already at shed`() {
        val machine = data.findMachineById(0) ?: return
        pathFinderController.moveToShedUnloadedMachine(machine)
        assert(machine.getLocationOfShed() == machine.getCurrentLocation())
    }

    @Test
    fun `moveToShedUnloadedMachine machine on field one Tile away`() {
        val machine = data.findMachineById(0) ?: return
        machine.setCurrentLocation(Coordinate(0, 2))
        pathFinderController.moveToShedUnloadedMachine(machine)

        assert(machine.getLocationOfShed() == machine.getCurrentLocation())
    }

    @Test
    fun `moveToShedLoadedMachine not possible`() {
        val machine = data.findMachineById(0) ?: return
        val farm = data.getFarmByID(3) ?: return
        machine.setCurrentLocation(Coordinate(0, 2))
        machine.setAmountLoadedThisTick(5000)
        pathFinderController.moveToShedLoadedMachine(machine, farm)
        assert(machine.isDisabledPermanently())
        assert(machine.getCurrentLocation() == Coordinate(0, 2))
    }

    @Test
    fun `moveToShedLoadedMachine possible`() {
        val roadTile1 = Tile(4, TileType.ROAD, Coordinate(-2, 2), false, Direction.NONE, false, null)
        data.addTile(roadTile1, null)
        val roadTile2 = Tile(5, TileType.ROAD, Coordinate(-2, 0), false, Direction.NONE, false, null)
        data.addTile(roadTile2, null)
        val machine = data.findMachineById(0) ?: return
        val farm = data.getFarmByID(3) ?: return
        machine.setCurrentLocation(Coordinate(0, 2))
        machine.setAmountLoadedThisTick(5000)
        pathFinderController.moveToShedLoadedMachine(machine, farm)
        assert(machine.getCurrentLocation() == machine.getLocationOfShed())
        assert(!machine.isDisabledPermanently())
    }

    @Test
    fun `moveToShedLoadedMachine possible for other farmstead`() {
        val shedTile2 = Tile(4, TileType.FARMSTEAD, Coordinate(1, 1), false, Direction.NONE, true, null)
        data.addTile(shedTile2, 3)
        val machine = data.findMachineById(0) ?: return
        val farm = data.getFarmByID(3) ?: return
        machine.setCurrentLocation(Coordinate(0, 2))
        machine.setAmountLoadedThisTick(5000)
        pathFinderController.moveToShedLoadedMachine(machine, farm)
        assert(!machine.isDisabledPermanently())
        assert(machine.getLocationOfShed() == Coordinate(1, 1))
        assert(machine.getCurrentLocation() == Coordinate(1, 1))
    }
}
