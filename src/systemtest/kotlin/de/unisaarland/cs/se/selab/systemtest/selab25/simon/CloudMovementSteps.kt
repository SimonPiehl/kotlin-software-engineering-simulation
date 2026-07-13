package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Cloud movement steps
 *
 * @constructor Create empty Cloud movement steps
 */
class CloudMovementSteps : ExampleSystemTestExtension() {
    override val name = "CloudMovementSteps Simon"
    override val description = "Cloud Movement Test"

    override val farms = "simon/CloudRainFarm.json"
    override val scenario = "simon/CloudRainScenario.json"
    override val map = "simon/CloudRainMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 10

    override suspend fun run() {
        skipUntilCloudPart()
        skipUntilCloudPart()
        skipUntilCloudPart()
        skipUntilCloudPart()
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 6 to tile 1.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 1 to tile 2.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 165.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 2 to tile 3.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 165.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 3 to tile 4.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 4 to tile 5.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 165.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 5 to tile 6.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 6 to tile 1.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 1 to tile 2.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 162.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 2 to tile 3.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 162.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 4999 L water moved from tile 3 to tile 4.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 1 dissipates on tile 4.")
    }

    private suspend fun skipUntilCloudPart() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("PLANTATION tiles.")) return
        skipUntilCloudPart()
    }
}
