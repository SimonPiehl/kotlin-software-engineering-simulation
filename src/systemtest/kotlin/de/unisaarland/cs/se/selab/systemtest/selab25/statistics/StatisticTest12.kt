package de.unisaarland.cs.se.selab.systemtest.selab25.statistics

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Statistic test
 *
 * @constructor Create empty Statistic test
 */
class StatisticTest12 : ExampleSystemTestExtension() {
    override val name = "StatisticTest12"
    override val description = "Tests statistics at the end of the round correct all types"

    override val farms = "HarvestEstimationTest/farms.json"
    override val scenario = "HarvestEstimationTest/scenario.json"
    override val map = "HarvestEstimationTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_INFO)
        assertCurrentLine("[IMPORTANT] Simulation Info: Simulation ended at tick 24.")
    }
}
