package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud raining all down test
 *
 */
class CloudSimpleMergeDurationInfTwo : SystemTestSELab25() {
    override val name = "CloudSimpleMergeDuration"
    override val description = "tests duration of merged clouds from two with dur -1 and -1"

    override val farms = "tim/merge/farms.json"
    override val scenario = "tim/merge/scenario3.json"
    override val map = "tim/merge/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilCloudMovement()
        // Tick 0:
        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 10 L water moved from tile 1 to tile 2.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 0 united to cloud 2 with 20 L water and duration -1 on tile 2."
        )
        skipUntilCloudMovement()
        // Tick 1: Cloud Should not dissipate here due to duration -1
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
    }

    private suspend fun skipUntilCloudMovement() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Soil Moisture")) return
        return skipUntilCloudMovement()
    }
}
