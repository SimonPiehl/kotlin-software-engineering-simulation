package de.unisaarland.cs.se.selab.systemtest.selab25.statistics

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Statistic test
 *
 * @constructor Create empty Statistic test
 */
class StatisticTest6 : ExampleSystemTestExtension() {
    override val name = "StatisticTest6"
    override val description = "Tests statistics at the end of the round correct all types"

    override val farms = "HarvestEstimationTest/farms.json"
    override val scenario = "HarvestEstimationTest/scenario.json"
    override val map = "HarvestEstimationTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested:")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 852869 g.")
    }
}
