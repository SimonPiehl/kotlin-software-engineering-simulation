package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class CherryTooLateTest : SystemTestSELab25() {
    override val name = "CherryTooLateTest"
    override val description = "tests harvest estimate of cherry starting after harvest period"

    override val farms = "tim/PlantationFarming/tooLate/farms.json"
    override val scenario = "tim/PlantationFarming/tooLate/scenario.json"
    override val map = "tim/PlantationFarming/tooLate/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17 // Early Sep

    // Cherry cannot be harvested after year tick 15
    override suspend fun run() {
        skipUntilEnd()
        // Penalty for late harvest???
        // assertNextLine()
        skipUntilStat()
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }

    private suspend fun skipUntilEnd() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilEnd()
    }

    private suspend fun skipUntilStat() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line == "[IMPORTANT] Simulation Info: Simulation statistics are calculated.") return
        return skipUntilStat()
    }
}
