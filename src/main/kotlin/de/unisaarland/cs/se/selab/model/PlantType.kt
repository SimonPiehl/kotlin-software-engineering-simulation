package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.PlantConstants
import de.unisaarland.cs.se.selab.util.PlantSchedule

/**
 * Plant type
 *
 * @property idealEstimate the initial Harvest of the Plant
 * @property idealMoisture the ideal soil moisture
 * @property idealSunlight the ideal sunlight exposure
 * @property schedule the possible yearTicks where certain actions are Possible
 * @constructor
 */
enum class PlantType(
    val idealEstimate: Int,
    val idealMoisture: Int,
    val idealSunlight: Int,
    val schedule: PlantSchedule,
    val effectedByPollination: Boolean
) {
    /**
     * Potato
     *
     * @constructor Create Potato
     */
    POTATO(
        PlantConstants.POTATO_IDEAL_ESTIMATE,
        PlantConstants.POTATO_IDEAL_MOISTURE,
        PlantConstants.POTATO_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.POTATO_HARVESTING_POSSIBLE_START_TICK,
                    PlantConstants.POTATO_HARVESTING_POSSIBLE_END_TICK,
                    PlantConstants.POTATO_HARVESTING_LONGER,
                    PlantConstants.POTATO_HARVESTING_PENALTY
                ),
                Action.SOWING to listOf(
                    PlantConstants.POTATO_SOWING_POSSIBLE_START_TICK,
                    PlantConstants.POTATO_SOWING_POSSIBLE_END_TICK
                ),
                Action.WEEDING to listOf(
                    PlantConstants.POTATO_WEEDING_TICKS_AFTER_SOWING
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.POTATO_BLOOM_AFTER_SOWING
                )
            )
        ),
        true
    ),

    /**
     * Wheat
     *
     * @constructor Create Wheat
     */
    WHEAT(
        PlantConstants.WHEAT_IDEAL_ESTIMATE,
        PlantConstants.WHEAT_IDEAL_MOISTURE,
        PlantConstants.WHEAT_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.WHEAT_HARVESTING_POSSIBLE_START_TICK,
                    PlantConstants.WHEAT_HARVESTING_POSSIBLE_END_TICK,
                    PlantConstants.WHEAT_LATE_HARVESTING_LONGER,
                    PlantConstants.WHEAT_LATE_HARVEST_PENALTY
                ),
                Action.SOWING to listOf(
                    PlantConstants.WHEAT_SOWING_POSSIBLE_START_TICK,
                    PlantConstants.WHEAT_SOWING_POSSIBLE_END_TICK
                ),
                Action.WEEDING to listOf(
                    PlantConstants.WHEAT_WEEDING_TICKS_AFTER_SOWING_FIRST,
                    PlantConstants.WHEAT_WEEDING_TICKS_AFTER_SOWING_SECOND
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.WHEAT_BLOOMING
                )
            )
        ),
        false
    ),

    /**
     * Oat
     *
     * @constructor Create Oat
     */
    OAT(
        PlantConstants.OAT_IDEAL_ESTIMATE,
        PlantConstants.OAT_IDEAL_MOISTURE,
        PlantConstants.OAT_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.OAT_HARVESTING_START_TICK,
                    PlantConstants.OAT_HARVESTING_END_TICK,
                    PlantConstants.OAT_LATE_HARVEST_LONGER,
                    PlantConstants.OAT_LATE_HARVEST_PENALTY
                ),
                Action.SOWING to listOf(
                    PlantConstants.OAT_SOWING_POSSIBLE_START_TICK,
                    PlantConstants.OAT_SOWING_POSSIBLE_END_TICK
                ),
                Action.WEEDING to listOf(
                    PlantConstants.OAT_WEEDING_TICKS_AFTER_SOWING
                )
            )
        ),
        false
    ),

    /**
     * Pumpkin
     *
     * @constructor Create Pumpkin
     */
    PUMPKIN(
        PlantConstants.PUMPKIN_IDEAL_ESTIMATE,
        PlantConstants.PUMPKIN_IDEAL_MOISTURE,
        PlantConstants.PUMPKIN_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.PUMPKIN_HARVESTING_START_TICK,
                    PlantConstants.PUMPKIN_HARVESTING_END_TICK,
                    PlantConstants.PUMPKIN_HARVESTING_LONGER,
                    PlantConstants.PUMPKIN_HARVESTING_PENALTY
                ),
                Action.SOWING to listOf(
                    PlantConstants.PUMPKIN_SOWING_POSSIBLE_START_TICK,
                    PlantConstants.PUMPKIN_SOWING_POSSIBLE_END_TICK
                ),
                Action.WEEDING to listOf(
                    PlantConstants.PUMPKIN_WEEDING_EACH_TICKS_AFTER_SOWING
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.PUMPKIN_BLOOMING_TICKS_AFTER_SOWING,
                    PlantConstants.PUMPKIN_BLOOMING_TICKS_AFTER_SOWING + PlantConstants.PUMPKIN_BLOOMING_DURATION
                )
            )
        ),
        true
    ),

    /**
     * Apple
     *
     * @constructor Create Apple
     */
    APPLE(
        PlantConstants.APPLE_IDEAL_ESTIMATE,
        PlantConstants.APPLE_IDEAL_MOISTURE,
        PlantConstants.APPLE_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.APPLE_HARVESTING_START,
                    PlantConstants.APPLE_HARVESTING_END,
                    PlantConstants.APPLE_HARVESTING_LONGER,
                    PlantConstants.APPLE_HARVESTING_FINE
                ),
                Action.CUTTING to listOf(
                    PlantConstants.APPLE_CUTTING_FIRST_START_TICK,
                    PlantConstants.APPLE_CUTTING_FIRST_END_TICK,
                    PlantConstants.APPLE_CUTTING_SECOND_START_TICK,
                    PlantConstants.APPLE_CUTTING_SECOND_END_TICK
                ),
                Action.MOWING to listOf(
                    PlantConstants.APPLE_MOWING_FIRST,
                    PlantConstants.APPLE_MOWING_SECOND
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.APPLE_BLOOMING_START,
                    PlantConstants.APPLE_BLOOMING_END
                )
            )
        ),
        true
    ),

    /**
     * Almond
     *
     * @constructor Create empty Almond
     */
    ALMOND(
        PlantConstants.ALMOND_IDEAL_ESTIMATE,
        PlantConstants.ALMOND_IDEAL_MOISTURE,
        PlantConstants.ALMOND_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.ALMOND_HARVESTING_START,
                    PlantConstants.ALMOND_HARVESTING_END,
                    PlantConstants.ALMOND_HARVESTING_LONGER,
                    PlantConstants.ALMOND_HARVESTING_FINE
                ),
                Action.CUTTING to listOf(
                    PlantConstants.ALMOND_CUTTING_FIRST_START_TICK,
                    PlantConstants.ALMOND_CUTTING_FIRST_END_TICK,
                    PlantConstants.ALMOND_CUTTING_SECOND_START_TICK,
                    PlantConstants.ALMOND_CUTTING_SECOND_END_TICK
                ),
                Action.MOWING to listOf(
                    PlantConstants.ALMOND_MOWING_FIRST,
                    PlantConstants.ALMOND_MOWING_SECOND
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.ALMOND_BLOOMING_START,
                    PlantConstants.ALMOND_BLOOMING_END
                )
            )
        ),
        true
    ),

    /**
     * Cherry
     *
     * @constructor Create empty Cherry
     */
    CHERRY(
        PlantConstants.CHERRY_IDEAL_ESTIMATE,
        PlantConstants.CHERRY_IDEAL_MOISTURE,
        PlantConstants.CHERRY_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.CHERRY_HARVESTING_START,
                    PlantConstants.CHERRY_HARVESTING_END,
                    PlantConstants.CHERRY_HARVESTING_LONGER,
                    PlantConstants.CHERRY_HARVESTING_FINE
                ),
                Action.CUTTING to listOf(
                    PlantConstants.CHERRY_CUTTING_FIRST_START_TICK,
                    PlantConstants.CHERRY_CUTTING_FIRST_END_TICK,
                    PlantConstants.CHERRY_CUTTING_SECOND_START_TICK,
                    PlantConstants.CHERRY_CUTTING_SECOND_END_TICK
                ),
                Action.MOWING to listOf(
                    PlantConstants.CHERRY_MOWING_FIRST
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.CHERRY_BLOOMING_START,
                    PlantConstants.CHERRY_BLOOMING_END
                )
            )
        ),
        true
    ),

    /**
     * Grape
     *
     * @constructor Create empty Grape
     */
    GRAPE(
        PlantConstants.GRAPE_IDEAL_ESTIMATE,
        PlantConstants.GRAPE_IDEAL_MOISTURE,
        PlantConstants.GRAPE_IDEAL_SUN,
        PlantSchedule(
            mapOf(
                Action.HARVESTING to listOf(
                    PlantConstants.GRAPE_HARVESTING_START,
                    PlantConstants.GRAPE_HARVESTING_END,
                    PlantConstants.GRAPE_HARVESTING_LONGER,
                    PlantConstants.GRAPE_HARVESTING_FINE
                ),
                Action.CUTTING to listOf(
                    PlantConstants.GRAPE_CUTTING_FIRST_START_TICK,
                    PlantConstants.GRAPE_CUTTING_FIRST_END_TICK
                ),
                Action.MOWING to listOf(
                    PlantConstants.GRAPE_MOWING_FIRST,
                    PlantConstants.GRAPE_MOWING_SECOND
                ),
                Action.BLOOMING to listOf(
                    PlantConstants.GRAPE_BLOOMING_START,
                    PlantConstants.GRAPE_BLOOMING_END
                )
            )
        ),
        false
    )
}
