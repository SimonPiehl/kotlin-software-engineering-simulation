package general

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.PlantType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Can Perform Unit Test
 * tests canPerform, which says if a given action can be performed at a given time, only considering the timing
 * Tests if we have used correct timeRanges because of some failing tests
 */
class CanPerformActionTest {

    @Test
    fun canPerformHarvestingInIdealRange() {
        val scheduleOfWheat = PlantType.WHEAT.schedule
        assertTrue(scheduleOfWheat.canPerform(Action.HARVESTING, 12, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformLateHarvesting() {
        val scheduleOfWheat = PlantType.WHEAT.schedule
        assertTrue(scheduleOfWheat.canPerform(Action.HARVESTING, 15, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformHarvestingFalse() {
        val scheduleOfWheat = PlantType.WHEAT.schedule
        assertFalse(scheduleOfWheat.canPerform(Action.HARVESTING, 16, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformSowingInIdealRange() {
        assertTrue(PlantType.WHEAT.schedule.canPerform(Action.SOWING, 19, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformLateSowing() {
        assertTrue(PlantType.WHEAT.schedule.canPerform(Action.SOWING, 21, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformSowingFalse() {
        assertFalse(PlantType.WHEAT.schedule.canPerform(Action.SOWING, 18, 0, 0, PlantType.WHEAT))
    }

    @Test
    fun canPerformWeedingTrue() {
        assertTrue(PlantType.PUMPKIN.schedule.canPerform(Action.WEEDING, 0, 15, 13, PlantType.PUMPKIN))
    }

    @Test
    fun canPerformWeedingFalse() {
        assertFalse(PlantType.PUMPKIN.schedule.canPerform(Action.WEEDING, 0, 13, 13, PlantType.PUMPKIN))
    }

    @Test
    fun canPerformMowingTrue() {
        assertTrue(PlantType.APPLE.schedule.canPerform(Action.MOWING, 11, 0, 0, PlantType.APPLE))
    }

    @Test
    fun canPerformMowingFalse() {
        assertFalse(PlantType.APPLE.schedule.canPerform(Action.MOWING, 10, 0, 0, PlantType.APPLE))
    }

    @Test
    fun canPerformCuttingTrue() {
        assertTrue(PlantType.APPLE.schedule.canPerform(Action.CUTTING, 3, 0, 0, PlantType.APPLE))
    }

    @Test
    fun canPerformCuttingFalse() {
        assertFalse(PlantType.APPLE.schedule.canPerform(Action.CUTTING, 5, 0, 0, PlantType.APPLE))
    }
}
