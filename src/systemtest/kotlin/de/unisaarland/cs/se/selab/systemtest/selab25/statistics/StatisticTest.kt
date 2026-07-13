package de.unisaarland.cs.se.selab.systemtest.selab25.statistics

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Statistic test
 *
 * @constructor Create empty Statistic test
 */
class StatisticTest : ExampleSystemTestExtension() {
    override val name = "StatisticTest"
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
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 4820008 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 348677 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 343149 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 145889 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 174338 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 852869 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1628850 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 932576 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 393660 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate " +
                "still in fields and plantations: 0 g."
        )
    }
}
