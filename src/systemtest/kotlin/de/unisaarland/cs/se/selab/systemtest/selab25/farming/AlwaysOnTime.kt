package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class AlwaysOnTime : SystemTestSELab25() {
    override val name = "AppleNotMowedSystemTest"
    override val description = "tests penalty of not mowing for apples"

    override val farms = "tim/alwaysOnTime/farms.json"
    override val scenario = "tim/alwaysOnTime/scenario.json"
    override val map = "tim/alwaysOnTime/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1 // Early June

    override suspend fun run() {
        // Skip
        skip(6)
        assertFarmBegin()
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1530000 g of APPLE.")
        assertFarmEnd()
    }

    private suspend fun assertFarmBegin() {
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
    }
    private suspend fun assertFarmEnd() {
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
    private suspend fun skip(times: Int) {
        repeat(times) {
            getNextLine()
        }
    }
    private suspend fun skipUntilPenalty() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilPenalty()
    }
}
