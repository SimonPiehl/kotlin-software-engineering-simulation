package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class NotMowedApple : SystemTestSELab25() {
    override val name = "AppleNotMowedSystemTest"
    override val description = "tests penalty of not mowing for apples"

    override val farms = "tim/PlantationFarming/apples/farms.json"
    override val scenario = "tim/PlantationFarming/apples/scenario.json"
    override val map = "tim/PlantationFarming/apples/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 11 // Early June

    override suspend fun run() {
        // Skip
        skipUntilPenalty()
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING.")
    }

    private suspend fun skipUntilPenalty() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilPenalty()
    }
}
