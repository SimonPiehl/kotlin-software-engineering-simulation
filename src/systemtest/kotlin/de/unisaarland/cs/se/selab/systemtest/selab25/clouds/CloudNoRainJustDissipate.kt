package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud raining all down test
 *
 */
class CloudNoRainJustDissipate : SystemTestSELab25() {
    override val name = "CloudNoRainJustDissipate"
    override val description = "tests dissipation with duration 2"

    override val farms = "tim/cloudRainAllDown/farms.json"
    override val scenario = "tim/smallTests/scenarioNoRainDiss.json"
    override val map = "tim/cloudRainAllDown/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilCloudMovement()
        // Tick 0: nothing
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        skipUntilCloudMovement()
        // Tick 1: Dissipate due to duration
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 0.")
    }

    private suspend fun skipUntilCloudMovement() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Soil Moisture")) return
        return skipUntilCloudMovement()
    }
}
