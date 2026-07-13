package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud raining all down test
 *
 */
class CloudRainingAllDownTest : SystemTestSELab25() {
    override val name = "CloudRainingAllDown"
    override val description = "tests behavior of cloud after raining full amount"

    override val farms = "tim/cloudRainAllDown/farms.json"
    override val scenario = "tim/cloudRainAllDown/scenario.json"
    override val map = "tim/cloudRainAllDown/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilCloudMovement()
        // Cloud Should Rain All
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 0 rained down 10000 L water.")
        // Assert Dissipation
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 0.")
    }

    private suspend fun skipUntilCloudMovement() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Soil Moisture")) return
        return skipUntilCloudMovement()
    }
}
