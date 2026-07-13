package farm

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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Method

class FarmControllerUnit {

    private lateinit var data: SimulationData
    private lateinit var pathFinder: PathFinderController
    private lateinit var farmController: FarmController
    private lateinit var farm: Farm
    private lateinit var machine: Machine
    private lateinit var sowingPlan: SowingPlan
    private lateinit var tile: Tile
    private lateinit var growable: Growable
    private lateinit var plantation: Tile
    private lateinit var machine1: Machine
    private lateinit var machine2: Machine
    private lateinit var field: Tile
    private lateinit var tile1: Tile
    private lateinit var tile2: Tile
    private lateinit var growable1: Growable
    private lateinit var growable2: Growable

    @BeforeEach
    fun setup() {
        data = mock()
        pathFinder = mock()
        farmController = FarmController(data, pathFinder)
        farm = mock()
        machine = mock()
        sowingPlan = mock()
        tile = mock()
        plantation = mock()
        growable = mock()
        whenever(data.getFarms()).thenReturn(mutableListOf(farm))

        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)

        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmPlantationsByID(any())).thenReturn(listOf(plantation))

        whenever(farm.getID()).thenReturn(1)
        whenever(farm.getMachinesByPlantAndAction(PlantType.APPLE, Action.HARVESTING)).thenReturn(listOf(machine))

        whenever(plantation.canIBeHarvested(10, 1)).thenReturn(true)
        whenever(plantation.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(plantation.getGrowable()).thenReturn(growable)
        whenever(plantation.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(plantation.getID()).thenReturn(101)

        whenever(growable.getCurrentPlant()).thenReturn(PlantType.APPLE)
        whenever(growable.getCropsExpected()).thenReturn(50)

        whenever(pathFinder.isTileReachable(farm, machine, plantation)).thenReturn(true)
        whenever(machine.getDuration()).thenReturn(4)
        whenever(machine.getID()).thenReturn(19)
        whenever(machine.canIStillWork()).thenReturn(false)
        whenever(machine.getWorkedOnPlant()).thenReturn(PlantType.APPLE)

        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getFarms()).thenReturn(mutableListOf(farm))
        whenever(farm.getID()).thenReturn(1)
        whenever(farm.getPlans()).thenReturn(emptyList())
        whenever(farm.getMachines()).thenReturn(emptyList())
        whenever(data.getYearTick()).thenReturn(1)
        whenever(farm.getPlans()).thenReturn(listOf(sowingPlan))
        whenever(farm.getMachinesByPlantAndAction(any(), eq(Action.SOWING))).thenReturn(listOf(machine))
        whenever(machine.canIWork()).thenReturn(true)
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.WHEAT))
        whenever(tile.getID()).thenReturn(1)
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(growable.getPossiblePlants()).thenReturn(listOf(PlantType.WHEAT))
        whenever(growable.getWasSowedAtTick()).thenReturn(-1)
        whenever(sowingPlan.getStartTick()).thenReturn(5)
        whenever(sowingPlan.getPlantToSow()).thenReturn(PlantType.WHEAT)
        whenever(sowingPlan.getLocations()).thenReturn(listOf(1))
        whenever(sowingPlan.getID()).thenReturn(123)
        whenever(farm.getMachinesByPlantAndAction(PlantType.WHEAT, Action.SOWING))
            .thenReturn(listOf(machine))
        whenever(pathFinder.isTileReachable(eq(farm), eq(machine), eq(tile))).thenReturn(true)
        val mapMock = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(mapMock.getFarmFieldsByID(any())).thenReturn(listOf(tile))

        whenever(data.getMap()).thenReturn(mapMock)

        whenever(tile.canIBeSowed(any(), any(), eq(PlantType.WHEAT))).thenReturn(true)
    }

    @Test
    fun harvest() {
        val machine: Machine = mock()
        val tile: Tile = mock()
        val growable: Growable = mock()
        val cord: Coordinate = Coordinate(0, 0)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(data.getCurrentTick()).thenReturn(42)
        whenever(machine.getID()).thenReturn(1)
        whenever(tile.getID()).thenReturn(2)
        whenever(machine.getDuration()).thenReturn(10)
        whenever(growable.getCropsExpected()).thenReturn(100)
        whenever(growable.getCurrentPlant()).thenReturn(PlantType.WHEAT)
        whenever(machine.getWorkedOnPlant()).thenReturn(PlantType.WHEAT)

        // Call private function harvest via reflection
        val method = FarmController::class.java.getDeclaredMethod("harvest", Machine::class.java, Tile::class.java)
        method.isAccessible = true
        method.invoke(farmController, machine, tile)

        verify(growable).setWasHarvestedAtTick(42)
        verify(machine).setAmountLoadedThisTick(100)
        verify(machine).setPerformedAction(Action.HARVESTING)
        verify(machine).setWorkedOnPlant(PlantType.WHEAT)
        verify(machine).setCurrentLocation(cord)
        verify(machine).worked()
        verify(tile).getTileType()
        verify(growable).setCurrentPlant(null)
        verify(growable).setCropsExpected(0)
        verify(growable).setWasSowedAtTick(-1)
        verify(growable).setWasWeededAtTick(any())
        verify(growable).setWasCutAtTick(-1)
        verify(growable).setWasMowedAtTick(-1)
        verify(growable).setLastTickWorkedOn(42)
        Mockito.framework().clearInlineMocks()
    }

    /*
    @Test
    fun farmActs() {
        val spyController = spy(farmController)

        // Stub farm methods called during farmActs to avoid NPE
        whenever(farm.getID()).thenReturn(1)

        // Return empty sowing plans, but you can add mocks if needed
        whenever(farm.getPlans()).thenReturn(emptyList())

        // Return empty machines list so private methods can proceed safely
        whenever(farm.getMachines()).thenReturn(emptyList())

        // SimulationData current tick used in logic
        whenever(data.getCurrentTick()).thenReturn(10)

        // Stub data.getMap() if any private method uses it, with minimal stubs
        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmPlantationsByID(any())).thenReturn(emptyList())
        whenever(map.getFarmFieldsByID(any())).thenReturn(emptyList())

        // Use reflection to access private method farmActs
        val farmActsMethod = FarmController::class.java.getDeclaredMethod("farmActs", Farm::class.java)
        farmActsMethod.isAccessible = true

        // Invoke private method farmActs on spy controller
        farmActsMethod.invoke(spyController, farm)

        // Verify expected interactions, e.g. farm.getMachines() called once for resets
        verify(farm, times(2)).getMachines()
    }
    */
    @Test
    fun checkAndIfPossibleSOWINGFields() {
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleSOWINGFields",
            Farm::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm)

        verify(growable).setCurrentPlant(PlantType.WHEAT)
        verify(growable).setWasSowedAtTick(10)

        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()

        verify(sowingPlan).setWasUsedInTick(10)

        verify(farm).removePlan(sowingPlan)

        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun checkAndIfPossibleCUTTINGPlantation() {
        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)

        // Set up map to return plantation list
        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmPlantationsByID(any())).thenReturn(listOf(plantation))

        // Farm id and machines able to cut given plant
        whenever(farm.getID()).thenReturn(1)
        whenever(farm.getMachinesByPlantAndAction(any(), eq(Action.CUTTING))).thenReturn(listOf(machine))

        // Plantation can be cut and is not worked on this tick
        whenever(plantation.canIBeCut(10, 1)).thenReturn(true)
        whenever(plantation.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(plantation.getGrowable()).thenReturn(growable)
        whenever(plantation.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(plantation.getID()).thenReturn(42)

        // Growable's current plant type to be cut
        whenever(growable.getCurrentPlant()).thenReturn(PlantType.OAT)

        // Machine can work and can cut at plantation
        whenever(machine.canIWork()).thenReturn(true)
        whenever(pathFinder.isTileReachable(farm, machine, plantation)).thenReturn(true)
        whenever(machine.getDuration()).thenReturn(5)
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.canIStillWork()).thenReturn(false)

        // Setup machine void methods to do nothing during test
        doNothing().whenever(machine).setPerformedAction(Action.CUTTING)
        doNothing().whenever(machine).setWorkedOnPlant(PlantType.OAT)
        doNothing().whenever(machine).setCurrentLocation(Coordinate(0, 0))
        doNothing().whenever(machine).worked()

        // Setup pathfinder void method to do nothing
        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleCUTTINGPlantation",
            Farm::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm)

        // Verify plantation growable got wasCutAtTick set with current tick
        verify(growable).setWasCutAtTick(10)

        // Verify machine got set to CUTTING and performed necessary updates/
        verify(machine).setPerformedAction(Action.CUTTING)
        verify(machine).setWorkedOnPlant(PlantType.OAT)
        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()

        // Verify pathfinder call to move machine back to shed
        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun performOnLeftOverMachinesById() {
        machine1 = mock()
        machine2 = mock()

        whenever(farm.getMachines()).thenReturn(listOf(machine1, machine2))

        // Set machine1 can work, machine2 cannot
        whenever(machine1.canIWork()).thenReturn(true)
        whenever(machine1.getDuration()).thenReturn(5)
        whenever(machine1.getID()).thenReturn(1)

        whenever(machine2.canIWork()).thenReturn(false)
        whenever(machine2.getDuration()).thenReturn(10)
        whenever(machine2.getID()).thenReturn(2)

        // No performed action initially
        whenever(machine1.getPerformedAction()).thenReturn(null)
        whenever(machine2.getPerformedAction()).thenReturn(null)

        // Sort expects comparable, so provide IDs and durations for sorting
        whenever(machine1.getDuration()).thenReturn(5)
        whenever(machine2.getDuration()).thenReturn(10)

        // Setup pathFinder stubs for reachability for all relevant action checks if needed
        whenever(pathFinder.isTileReachable(any(), any(), any())).thenReturn(true)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "performOnLeftOverMachinesById",
            Farm::class.java
        )
        method.isAccessible = true

        // Call the private method via reflection
        method.invoke(farmController, farm)

        // Verify farm.getMachines called once
        verify(farm, times(1)).getMachines()

        // Verify canIWork called, machine1 called but machine2 skipped due to cannot work
        verify(machine1, times(5)).canIWork()
        verify(machine2, times(1)).canIWork()

        // Example: if those internal private methods call pathFinder.isTileReachable or similar,
        // you can verify those as indirect evidence
        // verify(pathFinder, atLeastOnce()).isTileReachable(any(), eq(machine1), any())
    }

    @Test
    fun checkAndIfPossibleIRRIGATINGFields() {
        field = mock()
        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(farm.getID()).thenReturn(1)

        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmFieldsByID(any())).thenReturn(listOf(field))

        whenever(field.canIBeIrrigated(10)).thenReturn(true)
        whenever(field.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(field.getGrowable()).thenReturn(growable)
        whenever(field.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(field.getID()).thenReturn(100)

        whenever(growable.getCurrentPlant()).thenReturn(PlantType.OAT)
        whenever(growable.getMaxMoisture()).thenReturn(5)

        whenever(machine.canIWork()).thenReturn(true)
        whenever(machine.getPerformedAction()).thenReturn(null)
        whenever(machine.getPossibleActions()).thenReturn(listOf(Action.IRRIGATING))
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.OAT))
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)
        whenever(machine.canIStillWork()).thenReturn(false)

        whenever(pathFinder.isTileReachable(farm, machine, field)).thenReturn(true)

        doNothing().whenever(growable).setMoistureExposure(5)
        doNothing().whenever(growable).setWasIrrigatedAtTick(10)
        doNothing().whenever(machine).setWorkedOnPlant(PlantType.OAT)
        doNothing().whenever(machine).setPerformedAction(Action.IRRIGATING)
        doNothing().whenever(machine).setCurrentLocation(Coordinate(0, 0))
        doNothing().whenever(machine).worked()
        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleIRRIGATINGFields",
            Farm::class.java,
            Machine::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm, machine)

        verify(growable).setMoistureExposure(5)
        verify(growable).setWasIrrigatedAtTick(10)
        verify(machine).setWorkedOnPlant(PlantType.OAT)
        verify(machine).setPerformedAction(Action.IRRIGATING)
        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()
        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun checkAndIfPossibleWEEDINGFields() {
        field = mock()
        whenever(data.getCurrentTick()).thenReturn(10)

        // Farm stub to return ID and machines by plant and action
        whenever(farm.getID()).thenReturn(1)
        whenever(farm.getMachinesByPlantAndAction(any(), eq(Action.WEEDING))).thenReturn(listOf(machine))

        // Map stub to return fields list for the farm
        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmFieldsByID(any())).thenReturn(listOf(field))

        // Field conditions for weeding
        whenever(field.canIBeWeeded(10)).thenReturn(true)
        whenever(field.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(field.getGrowable()).thenReturn(growable)
        whenever(field.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(field.getID()).thenReturn(100)

        // Growable plant type and default values
        whenever(growable.getCurrentPlant()).thenReturn(PlantType.WHEAT)
        doNothing().whenever(growable).addWasWeededAtTick(10)
        doNothing().whenever(growable).setLastTickWorkedOn(10)

        // Machine setup for weeding
        whenever(machine.canIWork()).thenReturn(true)
        whenever(machine.getPerformedAction()).thenReturn(null)
        whenever(machine.getPossibleActions()).thenReturn(listOf(Action.WEEDING))
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.WHEAT))
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)
        whenever(machine.canIStillWork()).thenReturn(false)

        // Stub pathfinder reachable check
        whenever(pathFinder.isTileReachable(farm, machine, field)).thenReturn(true)

        // Void methods on machine
        doNothing().whenever(machine).setWorkedOnPlant(PlantType.WHEAT)
        doNothing().whenever(machine).setPerformedAction(Action.WEEDING)
        doNothing().whenever(machine).setCurrentLocation(Coordinate(0, 0))
        doNothing().whenever(machine).worked()

        // Stub pathfinder move to shed
        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleWEEDINGFields",
            Farm::class.java,
            Machine::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm, machine)

        verify(growable).addWasWeededAtTick(10)
        verify(growable).setLastTickWorkedOn(10)
        verify(machine).setWorkedOnPlant(PlantType.WHEAT)
        verify(machine).setPerformedAction(Action.WEEDING)
        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()
        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun checkAndIfPossibleIRRIGATINGPlantations() {
        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)

        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmPlantationsByID(any())).thenReturn(listOf(plantation))

        whenever(farm.getID()).thenReturn(1)
        // whenever(farm.getMachinesByPlantAndAction(any(), Action.IRRIGATING)).thenReturn(listOf(machine))

        whenever(plantation.canIBeIrrigated(10)).thenReturn(true)
        whenever(plantation.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(plantation.getGrowable()).thenReturn(growable)
        whenever(plantation.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(plantation.getID()).thenReturn(123)

        whenever(growable.getCurrentPlant()).thenReturn(PlantType.OAT)
        whenever(growable.getMaxMoisture()).thenReturn(5)

        whenever(machine.canIWork()).thenReturn(true)
        whenever(machine.getPerformedAction()).thenReturn(null)
        whenever(machine.getPossibleActions()).thenReturn(listOf(Action.IRRIGATING))
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.OAT))
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)
        whenever(machine.canIStillWork()).thenReturn(false)

        whenever(pathFinder.isTileReachable(farm, machine, plantation)).thenReturn(true)

        doNothing().whenever(growable).setMoistureExposure(any())
        doNothing().whenever(growable).setWasIrrigatedAtTick(any())
        doNothing().whenever(machine).setWorkedOnPlant(any())
        doNothing().whenever(machine).setPerformedAction(any())
        doNothing().whenever(machine).setCurrentLocation(any())
        doNothing().whenever(machine).worked()
        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleIRRIGATINGPlantations",
            Farm::class.java,
            Machine::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm, machine)

        verify(growable).setMoistureExposure(5)
        verify(growable).setWasIrrigatedAtTick(10)
        verify(machine).setWorkedOnPlant(PlantType.OAT)
        verify(machine).setPerformedAction(Action.IRRIGATING)
        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()
        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun checkAndIfPossibleMOWINGPlantation() {
        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)

        val map = mock<de.unisaarland.cs.se.selab.model.Map>()
        whenever(data.getMap()).thenReturn(map)
        whenever(map.getFarmPlantationsByID(any())).thenReturn(listOf(plantation))

        whenever(farm.getID()).thenReturn(1)
        // whenever(farm.getMachinesByPlantAndAction(any(), Action.MOWING)).thenReturn(listOf(machine))

        whenever(plantation.canIBeMowed(10, 1)).thenReturn(true)
        whenever(plantation.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(plantation.getGrowable()).thenReturn(growable)
        whenever(plantation.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(plantation.getID()).thenReturn(123)

        whenever(growable.getCurrentPlant()).thenReturn(PlantType.OAT)

        whenever(machine.canIWork()).thenReturn(true)
        whenever(machine.getPerformedAction()).thenReturn(null)
        whenever(machine.getPossibleActions()).thenReturn(listOf(Action.MOWING))
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.OAT))
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)
        whenever(machine.canIStillWork()).thenReturn(false)

        whenever(pathFinder.isTileReachable(farm, machine, plantation)).thenReturn(true)

        doNothing().whenever(growable).setWasMowedAtTick(any())
        doNothing().whenever(machine).setWorkedOnPlant(any())
        doNothing().whenever(machine).setPerformedAction(any())
        doNothing().whenever(machine).setCurrentLocation(any())
        doNothing().whenever(machine).worked()
        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "checkAndIfPossibleMOWINGPlantations",
            Farm::class.java,
            Machine::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm, machine)

        verify(growable).setWasMowedAtTick(10)
        verify(machine).setWorkedOnPlant(PlantType.OAT)
        verify(machine).setPerformedAction(Action.MOWING)
        verify(machine).setCurrentLocation(Coordinate(0, 0))
        verify(machine).worked()
        verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun continueAction() {
        tile1 = mock()
        tile2 = mock()
        growable1 = mock()
        growable2 = mock()

        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)

        whenever(machine.getPerformedAction()).thenReturn(Action.IRRIGATING)
        whenever(machine.getPossiblePlants()).thenReturn(listOf(PlantType.OAT))
        whenever(machine.getWorkedOnPlant()).thenReturn(PlantType.OAT)
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)

        // Two reachable tiles
        whenever(pathFinder.getAllReachableTilesInRadiusTwo(farm, machine)).thenReturn(listOf(tile1, tile2))

        whenever(tile1.canIBe(10, 1, Action.IRRIGATING)).thenReturn(true)
        whenever(tile1.getGrowable()).thenReturn(growable1)
        whenever(growable1.getCurrentPlant()).thenReturn(PlantType.OAT)
        whenever(tile1.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(tile1.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile1.getID()).thenReturn(101)
        whenever(tile1.getCoordinate()).thenReturn(Coordinate(5, 5))
        whenever(growable1.getMaxMoisture()).thenReturn(3)

        whenever(tile2.canIBe(10, 1, Action.IRRIGATING)).thenReturn(true)
        whenever(tile2.getGrowable()).thenReturn(growable2)
        whenever(growable2.getCurrentPlant()).thenReturn(PlantType.OAT)
        whenever(tile2.checkWorkedOnThisTick(10)).thenReturn(false)
        whenever(tile2.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(tile2.getID()).thenReturn(102)
        whenever(tile2.getCoordinate()).thenReturn(Coordinate(6, 6))
        whenever(growable2.getMaxMoisture()).thenReturn(5)

        // Stub void methods on growable1 and growable2
        doNothing().whenever(growable1).setWasIrrigatedAtTick(10)
        doNothing().whenever(growable1).setMoistureExposure(3)
        doNothing().whenever(growable2).setWasIrrigatedAtTick(10)
        doNothing().whenever(growable2).setMoistureExposure(5)

        doNothing().whenever(machine).setCurrentLocation(any())
        doNothing().whenever(machine).worked()

        doNothing().whenever(pathFinder).moveToShedUnloadedMachine(machine)
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "continueAction",
            Farm::class.java,
            Machine::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, farm, machine)

        // Verify irrigation on tile1 (sorted by tileType and ID: FIELD (tile1) before PLANTATION (tile2))
        verify(growable1).setWasIrrigatedAtTick(10)
        verify(growable1).setMoistureExposure(3)
        verify(machine).setCurrentLocation(Coordinate(5, 5))
        verify(machine).worked()

        // Verify pathfinder told machine to return home
        // verify(pathFinder).moveToShedUnloadedMachine(machine)
    }

    @Test
    fun continueSow() {
        sowingPlan = mock()
        tile = mock()
        growable = mock()

        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(data.getYearTick()).thenReturn(1)
        whenever(farm.getID()).thenReturn(1)

        whenever(sowingPlan.getPlantToSow()).thenReturn(PlantType.WHEAT)
        whenever(sowingPlan.getLocations()).thenReturn(listOf(100))

        whenever(pathFinder.getAllReachableTilesInRadiusTwo(farm, machine)).thenReturn(listOf(tile))

        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(growable.getPossiblePlants()).thenReturn(listOf(PlantType.WHEAT))
        whenever(tile.canIBeSowed(10, 1, PlantType.WHEAT)).thenReturn(true)
        whenever(tile.getID()).thenReturn(100)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(5, 5))

        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(4)

        doNothing().whenever(growable).setCurrentPlant(PlantType.WHEAT)
        doNothing().whenever(growable).setWasSowedAtTick(10)
        doNothing().whenever(machine).setCurrentLocation(Coordinate(5, 5))
        doNothing().whenever(machine).worked()
        // Use Java reflection to get private method with param types Farm.class, Machine.class, SowingPlan.class
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "continueSow",
            Farm::class.java,
            Machine::class.java,
            SowingPlan::class.java
        )
        method.isAccessible = true

        // Invoke private method
        method.invoke(farmController, farm, machine, sowingPlan)

        // Verify expected interactions
        verify(growable).setCurrentPlant(PlantType.WHEAT)
        verify(growable).setWasSowedAtTick(10)
        verify(machine).setCurrentLocation(Coordinate(5, 5))
        verify(machine).worked()
    }

    @Test
    fun harvest2() {
        whenever(data.getCurrentTick()).thenReturn(10)
        whenever(machine.getID()).thenReturn(1)
        whenever(machine.getDuration()).thenReturn(5)
        whenever(tile.getID()).thenReturn(50)
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(growable.getCropsExpected()).thenReturn(100)
        whenever(growable.getCurrentPlant()).thenReturn(PlantType.WHEAT)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(7, 8))
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)

        // Stub methods on machine
        doNothing().whenever(machine).setAmountLoadedThisTick(any())
        doNothing().whenever(machine).setPerformedAction(any())
        doNothing().whenever(machine).setWorkedOnPlant(any())
        doNothing().whenever(machine).setCurrentLocation(any())
        doNothing().whenever(machine).worked()

        // Stub growable reset methods
        doNothing().whenever(growable).setWasHarvestedAtTick(any())
        doNothing().whenever(growable).setCurrentPlant(null)
        doNothing().whenever(growable).setCropsExpected(0)
        doNothing().whenever(growable).setWasSowedAtTick(-1)
        doNothing().whenever(growable).setWasWeededAtTick(mutableListOf())
        doNothing().whenever(growable).setWasCutAtTick(-1)
        doNothing().whenever(growable).setWasMowedAtTick(-1)
        doNothing().whenever(growable).setLastTickWorkedOn(any())
        val method: Method = FarmController::class.java.getDeclaredMethod(
            "harvest",
            Machine::class.java,
            Tile::class.java
        )
        method.isAccessible = true

        method.invoke(farmController, machine, tile)

        // Verify growable was updated
        verify(growable).setWasHarvestedAtTick(10)
        verify(growable).setCurrentPlant(null)
        verify(growable).setCropsExpected(0)
        verify(growable).setWasSowedAtTick(-1)
        verify(growable).setWasWeededAtTick(mutableListOf())
        verify(growable).setWasCutAtTick(-1)
        verify(growable).setWasMowedAtTick(-1)
        verify(growable).setLastTickWorkedOn(10)

        // Verify machine state updates
        verify(machine).setAmountLoadedThisTick(100)
        verify(machine).setPerformedAction(Action.HARVESTING)
        verify(machine).setWorkedOnPlant(PlantType.WHEAT)
        verify(machine).setCurrentLocation(Coordinate(7, 8))
        verify(machine).worked()
    }
}
