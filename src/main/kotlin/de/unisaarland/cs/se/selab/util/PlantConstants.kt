package de.unisaarland.cs.se.selab.util

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.PlantType

/**
 * Plant schedule
 *
 * @property activities
 * @constructor
 */
data class PlantSchedule(
    val activities: Map<Action, List<Int>>
) {
    /**
     * Get ticks for given Action
     *
     * @param action
     * @return
     */
    fun getTicks(action: Action): List<Int> = activities[action].orEmpty()

// helper sets
    private val plantationPlant = setOf(PlantType.APPLE, PlantType.GRAPE, PlantType.ALMOND, PlantType.CHERRY)

    // helper function
    private fun inRange(x: Int, start: Int, endInclusive: Int) = x in start..endInclusive

    /**
     * Can perform: Returns whether a given action can be performed, only regarding yearTick or relative to SowingTime
     *
     * @param action
     * @return
     */
    fun canPerform(
        action: Action,
        yearTick: Int,
        currentTick: Int,
        sowed: Int,
        plant: PlantType
    ): Boolean {
        val ticks = plant.schedule.getTicks(action)
        return when (action) {
            Action.HARVESTING -> canHarvest(yearTick, ticks)
            Action.SOWING -> canSow(yearTick, plant, ticks)
            Action.WEEDING -> canWeed(currentTick, sowed, plant, ticks)
            Action.CUTTING -> canCut(yearTick, plant, ticks)
            Action.MOWING -> canMow(yearTick, plant, ticks)
            else -> false
        }
    }

// --- pro Action eine sehr kleine Funktion ---

    private fun canHarvest(yearTick: Int, ticks: List<Int>): Boolean {
        // list[0]=start, list[1]=end, list[2]=grace/longer
        val start = ticks[0]
        val end = ticks[1] + ticks[2]
        return inRange(yearTick, start, end)
    }

    private fun canSow(yearTick: Int, plant: PlantType, ticks: List<Int>): Boolean {
        if (plant in plantationPlant) return false
        val start = ticks[0]
        val end = ticks[1] + PlantConstants.LATE_SOWING
        return inRange(yearTick, start, end)
    }

    private fun canWeed(currentTick: Int, sowed: Int, plant: PlantType, ticks: List<Int>): Boolean {
        val diff = currentTick - sowed
        return when (plant) {
            in plantationPlant -> false
            PlantType.POTATO, PlantType.PUMPKIN -> diff > 0 && diff % 2 == 0
            PlantType.WHEAT -> diff == ticks[0] || diff == ticks[1] // Pflichtzeitpunkte
            PlantType.OAT -> diff in 1..PlantConstants.THREE // Fenster ab 1 bis inkl. 3
            else -> false
        }
    }

    private fun canCut(yearTick: Int, plant: PlantType, ticks: List<Int>): Boolean {
        // Nicht schneidbare Pflanzen (hier deine einjährigen/Nicht-Obst)
        if (plant in setOf(PlantType.POTATO, PlantType.PUMPKIN, PlantType.WHEAT, PlantType.OAT)) return false

        return when (plant) {
            PlantType.GRAPE -> {
                inRange(yearTick, ticks[0], ticks[1])
            }
            PlantType.ALMOND, PlantType.CHERRY, PlantType.APPLE -> {
                // zwei Zeitfenster: [0..1] oder [2..3]
                inRange(yearTick, ticks[0], ticks[1]) || inRange(
                    yearTick, ticks[2],
                    ticks[3]
                )
            }

            else -> {
                false
            }
        }
    }

    private fun canMow(yearTick: Int, plant: PlantType, ticks: List<Int>): Boolean {
        // Nicht mähbare Pflanzen
        if (plant in setOf(PlantType.POTATO, PlantType.PUMPKIN, PlantType.WHEAT, PlantType.OAT)) return false

        return when (plant) {
            PlantType.CHERRY -> yearTick == ticks[0]
            PlantType.ALMOND, PlantType.GRAPE, PlantType.APPLE -> yearTick == ticks[0] || yearTick == ticks[1]
            else -> false
        }
    }
}

/**
 * Plant constants
 */
object PlantConstants {

    const val SUNLIGHT_DIFFERENCE = 25
    const val SUNLIGHT_PERCENTAGE = 0.9
    const val MOISTURE_DIFFERENCE = 100
    const val MOISTURE_LOSS = 50
    const val RESET_MONTH = 21

    // reductions
    const val REDUCTION_FOR_LATE_SOWING_FIRST = 0.8
    const val REDUCTION_FOR_LATE_SOWING_SECOND = 0.6
    const val REDUCTION_FOR_LATE_CUTTING = 0.5
    const val REDUCTION_FOR_MISSED_MOWING = 0.9

    // helper
    const val LATE_SOWING = 2
    const val HUNDRED_FOR_PERCENTAGE = 100.0
    const val THREE = 3
    const val NINE = 9
    const val FINE_LATE_WEEDING = 90

    // Potato
    const val POTATO_IDEAL_ESTIMATE = 1000000
    const val POTATO_IDEAL_MOISTURE = 500
    const val POTATO_IDEAL_SUN = 130
    const val POTATO_BLOOM_AFTER_SOWING = 3
    const val POTATO_WEEDING_TICKS_AFTER_SOWING = 2
    const val POTATO_SOWING_POSSIBLE_START_TICK = 7
    const val POTATO_SOWING_POSSIBLE_END_TICK = 10
    const val POTATO_HARVESTING_POSSIBLE_START_TICK = 17
    const val POTATO_HARVESTING_POSSIBLE_END_TICK = 20
    const val POTATO_HARVESTING_LONGER = 0
    const val POTATO_HARVESTING_PENALTY = 0

    // Wheat
    const val WHEAT_IDEAL_ESTIMATE = 1500000
    const val WHEAT_IDEAL_MOISTURE = 450
    const val WHEAT_IDEAL_SUN = 90
    const val WHEAT_SOWING_POSSIBLE_START_TICK = 19
    const val WHEAT_SOWING_POSSIBLE_END_TICK = 20
    const val WHEAT_WEEDING_TICKS_AFTER_SOWING_FIRST = 3
    const val WHEAT_WEEDING_TICKS_AFTER_SOWING_SECOND = 9
    const val WHEAT_BLOOMING = 9
    const val WHEAT_HARVESTING_POSSIBLE_START_TICK = 11
    const val WHEAT_HARVESTING_POSSIBLE_END_TICK = 13
    const val WHEAT_LATE_HARVESTING_LONGER = 2
    const val WHEAT_LATE_HARVEST_PENALTY = 80

    // OAT
    const val OAT_IDEAL_ESTIMATE = 1200000
    const val OAT_IDEAL_MOISTURE = 300
    const val OAT_IDEAL_SUN = 90
    const val OAT_SOWING_POSSIBLE_START_TICK = 6
    const val OAT_SOWING_POSSIBLE_END_TICK = 6
    const val OAT_WEEDING_TICKS_AFTER_SOWING = 3
    const val OAT_HARVESTING_START_TICK = 13
    const val OAT_HARVESTING_END_TICK = 16
    const val OAT_LATE_HARVEST_PENALTY = 80
    const val OAT_LATE_HARVEST_LONGER = 2

    // Plant Fines

    // PUMPKIN
    const val PUMPKIN_IDEAL_ESTIMATE = 500000
    const val PUMPKIN_IDEAL_MOISTURE = 600
    const val PUMPKIN_IDEAL_SUN = 120
    const val PUMPKIN_SOWING_POSSIBLE_START_TICK = 10
    const val PUMPKIN_SOWING_POSSIBLE_END_TICK = 12
    const val PUMPKIN_BLOOMING_TICKS_AFTER_SOWING = 2
    const val PUMPKIN_BLOOMING_DURATION = 2
    const val PUMPKIN_WEEDING_EACH_TICKS_AFTER_SOWING = 2
    const val PUMPKIN_HARVESTING_START_TICK = 17
    const val PUMPKIN_HARVESTING_END_TICK = 20
    const val PUMPKIN_HARVESTING_LONGER = 0
    const val PUMPKIN_HARVESTING_PENALTY = 0

    // APPLE
    const val APPLE_IDEAL_ESTIMATE = 1700000
    const val APPLE_IDEAL_MOISTURE = 100
    const val APPLE_IDEAL_SUN = 50
    const val APPLE_CUTTING_FIRST_START_TICK = 21
    const val APPLE_CUTTING_FIRST_END_TICK = 22
    const val APPLE_CUTTING_SECOND_START_TICK = 3
    const val APPLE_CUTTING_SECOND_END_TICK = 4
    const val APPLE_BLOOMING_START = 8
    const val APPLE_BLOOMING_END = 9
    const val APPLE_MOWING_FIRST = 11
    const val APPLE_MOWING_SECOND = 17
    const val APPLE_HARVESTING_START = 17
    const val APPLE_HARVESTING_END = 19
    const val APPLE_HARVESTING_LONGER = 1
    const val APPLE_HARVESTING_FINE = 50

    // ALMOND
    const val ALMOND_IDEAL_ESTIMATE = 800000
    const val ALMOND_IDEAL_MOISTURE = 400
    const val ALMOND_IDEAL_SUN = 130
    const val ALMOND_CUTTING_FIRST_START_TICK = 21
    const val ALMOND_CUTTING_FIRST_END_TICK = 22
    const val ALMOND_CUTTING_SECOND_START_TICK = 3
    const val ALMOND_CUTTING_SECOND_END_TICK = 4
    const val ALMOND_BLOOMING_START = 4
    const val ALMOND_BLOOMING_END = 5
    const val ALMOND_MOWING_FIRST = 11
    const val ALMOND_MOWING_SECOND = 17
    const val ALMOND_HARVESTING_START = 16
    const val ALMOND_HARVESTING_END = 19
    const val ALMOND_HARVESTING_LONGER = 1
    const val ALMOND_HARVESTING_FINE = 90

    // Cherry
    const val CHERRY_IDEAL_ESTIMATE = 1200000
    const val CHERRY_IDEAL_MOISTURE = 150
    const val CHERRY_IDEAL_SUN = 120
    const val CHERRY_CUTTING_FIRST_START_TICK = 21
    const val CHERRY_CUTTING_FIRST_END_TICK = 22
    const val CHERRY_CUTTING_SECOND_START_TICK = 3
    const val CHERRY_CUTTING_SECOND_END_TICK = 4
    const val CHERRY_BLOOMING_START = 8
    const val CHERRY_BLOOMING_END = 9
    const val CHERRY_MOWING_FIRST = 11
    const val CHERRY_HARVESTING_START = 13
    const val CHERRY_HARVESTING_END = 14
    const val CHERRY_HARVESTING_LONGER = 1
    const val CHERRY_HARVESTING_FINE = 30

    // Grape
    const val GRAPE_IDEAL_ESTIMATE = 1200000
    const val GRAPE_IDEAL_MOISTURE = 250
    const val GRAPE_IDEAL_SUN = 150
    const val GRAPE_CUTTING_FIRST_START_TICK = 14
    const val GRAPE_CUTTING_FIRST_END_TICK = 16
    const val GRAPE_BLOOMING_START = 12
    const val GRAPE_BLOOMING_END = 13
    const val GRAPE_MOWING_FIRST = 7
    const val GRAPE_MOWING_SECOND = 13
    const val GRAPE_HARVESTING_START = 17
    const val GRAPE_HARVESTING_END = 17
    const val GRAPE_HARVESTING_LONGER = 3
    const val GRAPE_HARVESTING_FINE = 95
}
