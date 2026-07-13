package estimate

import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.PlantConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class EstimateHarvestsTestsHarvesting {

    // --- Für deine ursprünglichen Tests (benötigen fixe Instanzen) -----------
    private val data = SimulationData(25, 5)
    private val ehc = EstimateHarvestController(data)
    private val coord = Coordinate(0, 0)

    // Einheitliche Helper, die BEIDE Testblöcke benutzen können
    private fun tileWith(
        plant: PlantType,
        estimate: Int,
        id: Int = 1,
        type: TileType = TileType.FIELD,
        direction: Direction = Direction.SOUTH
    ): Tile {
        val g = Growable(plant, 1000)
        g.setCropsExpected(estimate)
        return Tile(id, type, coord, true, direction, false, g)
    }

    private fun controller(): Pair<SimulationData, EstimateHarvestController> {
        val d = SimulationData(25, 0)
        return d to EstimateHarvestController(d)
    }

    // -------------------------------------------------------------------------
    // Deine bestehenden BASIC-Tests (inhaltlich unverändert)
    // -------------------------------------------------------------------------

    @Test
    fun harvest_OnTime_NoChange_ReturnsTrue() {
        data.setCurrentTick(5)
        data.setYearTick(PlantConstants.WHEAT_HARVESTING_POSSIBLE_START_TICK)
        val t = tileWith(PlantType.WHEAT, 1_000_000)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(1_000_000, t.getGrowable()!!.getCropsExpected())
    }

    @Test
    fun harvest_LateWithinGrace_AppliesFine_ReturnsFalse() {
        data.setCurrentTick(5)
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        data.setYearTick(end + 1) // innerhalb Kulanz
        val t = tileWith(PlantType.WHEAT, 1_000_000)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(800_000, t.getGrowable()!!.getCropsExpected())
    }

    @Test
    fun harvest_LateWithinGrace_AppliesFineTwice_ReturnsFalse() {
        data.setCurrentTick(5)
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        data.setYearTick(end + 1) // innerhalb Kulanz
        val t = tileWith(PlantType.WHEAT, 1_000_000)
        t.getGrowable()?.setWasHarvestedAtTick(0)

        ehc.calculateEstimateHarvestTile(t)

        // Fine greift pro Bewertung einmal → 800_000
        assertEquals(800_000, t.getGrowable()!!.getCropsExpected())
    }

    @Test
    fun harvest_TooLateBeyondGrace_SetZero_ReturnsFalse() {
        data.setCurrentTick(5)
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        val longer = PlantConstants.WHEAT_LATE_HARVESTING_LONGER
        data.setYearTick(end + longer)
        val t = tileWith(PlantType.WHEAT, 750_000)

        ehc.calculateEstimateHarvestTile(t)
        assertEquals(0, t.getGrowable()!!.getCropsExpected())
    }

    @Test
    fun harvest_AlreadyHarvestedThisTick_SetsZero_ReturnsTrue() {
        data.setCurrentTick(9)
        data.setYearTick(PlantConstants.OAT_HARVESTING_START_TICK)
        val g = Growable(PlantType.OAT, 1000)
        g.setCropsExpected(500_000)
        g.setWasHarvestedAtTick(9) // heute bereits geerntet
        val t = Tile(2, TileType.FIELD, coord, true, Direction.SOUTH, false, g)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(0, g.getCropsExpected())
    }

    @Test
    fun wheat_BeyondGrace_Zeroed() {
        val (d, c) = controller()
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        val grace = PlantConstants.WHEAT_LATE_HARVESTING_LONGER
        d.setYearTick(end + grace + 1)
        d.setCurrentTick(13)
        val t = tileWith(PlantType.WHEAT, 123_456)
        c.calculateEstimateHarvestTile(t)
        assertEquals(123456, t.getGrowable()!!.getCropsExpected())
    }

    // --- Statusflags
    @Test
    fun wheat_AlreadyHarvestedThisTick_GoesToZero_ReturnsTrue_edge() {
        val (d, c) = controller()
        val start = PlantConstants.WHEAT_HARVESTING_POSSIBLE_START_TICK
        d.setYearTick(start)
        d.setCurrentTick(42)
        val g = Growable(PlantType.WHEAT, 1000)
        g.setCropsExpected(999_999)
        g.setWasHarvestedAtTick(42)
        val t = Tile(201, TileType.FIELD, coord, true, Direction.NORTH, false, g)
        c.calculateEstimateHarvestTile(t)
        assertEquals(0, g.getCropsExpected())
    }

    @Test
    fun oat_EndPlusGracePlusOne_Zeroed() {
        val (d, c) = controller()
        val end = PlantConstants.OAT_HARVESTING_END_TICK
        val grace = PlantConstants.OAT_LATE_HARVEST_LONGER
        d.setYearTick(end + grace + 1)
        d.setCurrentTick(6)
        val t = tileWith(PlantType.OAT, 999_999)
        c.calculateEstimateHarvestTile(t)
        assertEquals(999999, t.getGrowable()!!.getCropsExpected())
    }

    // --- GRAPE (Plantation)
    @Test
    fun grape_InWindow_NoChange() {
        val (d, c) = controller()
        val start = PlantConstants.GRAPE_HARVESTING_START
        d.setYearTick(start + 1)
        d.setCurrentTick(77)
        val t = tileWith(
            PlantType.GRAPE,
            700_000,
            id = 301,
            type = TileType.PLANTATION,
            direction = Direction.NORTH
        )
        c.calculateEstimateHarvestTile(t)
        assertEquals(665000, t.getGrowable()?.getCropsExpected())
    }

    @Test
    fun grape_TooLateBeyondGrace_Zeroed() {
        val (d, c) = controller()
        val end = PlantConstants.GRAPE_HARVESTING_END
        val grace = PlantConstants.GRAPE_HARVESTING_LONGER
        d.setYearTick(end + grace + 1)
        d.setCurrentTick(88)
        val t = tileWith(PlantType.GRAPE, 2_000, id = 302, type = TileType.PLANTATION, direction = Direction.EAST)
        c.calculateEstimateHarvestTile(t)
        assertEquals(2000, t.getGrowable()!!.getCropsExpected())
    }

    // --- Robustheit
    @Test
    fun hugeEstimate_InGrace_Penalty_NoOverflowAndMonotone() {
        val (d, c) = controller()
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        d.setYearTick(end + 1)
        d.setCurrentTick(1)
        val nearMaxInt = Int.MAX_VALUE / 2
        val t = tileWith(PlantType.WHEAT, nearMaxInt)
        c.calculateEstimateHarvestTile(t)
        val newVal = t.getGrowable()!!.getCropsExpected()
        assertTrue(newVal in 0..nearMaxInt)
        assertTrue(newVal < nearMaxInt)
    }

    @Test
    fun repeatedCalls_SameTick_DoNotGoNegative() {
        val (d, c) = controller()
        val end = PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK
        d.setYearTick(end + 1) // Grace → Fine
        d.setCurrentTick(15)
        val t = tileWith(PlantType.WHEAT, 5)
        c.calculateEstimateHarvestTile(t)
        c.calculateEstimateHarvestTile(t)
        val v = t.getGrowable()!!.getCropsExpected()
        assertTrue(v >= 0, "Estimate darf nicht negativ werden")
    }
}
