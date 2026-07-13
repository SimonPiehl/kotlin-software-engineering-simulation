package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Two merged clouds full test
 *
 * @constructor Create empty Two merged clouds full test
 */
class TwoMergedCloudsFullTest : ExampleSystemTestExtension() {
    override val name = "TwoMergedCloudFullTest Simon"
    override val description = "checks performance of 2 merged clouds to one cloud"

    override val farms = "simon/CloudFinalFullFarms.json"
    override val scenario = "simon/CloudFinalFullScenario.json"
    override val map = "simon/CloudFinalFullMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilCloudPart()
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 1 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 9999900 L water moved from tile 1 to tile 2.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 95.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 0 united to cloud 4 with 19999900 L water and duration 8 on tile 2."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 3 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 9999900 L water moved from tile 3 to tile 4.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 3, the amount of sunlight is 95.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 3 and 2 united to cloud 5 with 19999900 L water and duration 4 on tile 4."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 4 on tile 2 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 4 with 19999800 L water moved from tile 2 to tile 4.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 95.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 5 and 4 united to cloud 6 with 39999700 L water and duration 4 on tile 4."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 6 on tile 4 rained down 100 L water.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 6 is on tile 4, where the amount of sunlight is 48.")
        skipUntilCloudPart()
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 6 on tile 4 rained down 100 L water.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 6 is on tile 4, where the amount of sunlight is 48.")
    }

    private suspend fun skipUntilCloudPart() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("PLANTATION tiles.")) return
        skipUntilCloudPart()
    }
}
