package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Cloud system test
 *
 * @constructor
 */
class CloudSystemTest : CloudSystemExtension() {
    override val name = "CloudSystemTest"
    override val description = "test raining, moving, merging & all 3 Dissipation reasons"

    override val farms = "CloudSystemTest/farm0.json"
    override val scenario = "CloudSystemTest/scenario.json"
    override val map = "CloudSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1
    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        // Cloud 0
        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4900 L water moved from tile 0 to tile 1.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 0, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4900 L water moved from tile 1 to tile 2.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4900 L water moved from tile 2 to tile 3.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4900 L water moved from tile 3 to tile 4.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 3, the amount of sunlight is 95.")

        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 0 united to cloud 5 with 5050 L water and duration 1 on tile 4."
        )

        // Cloud 2
        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 500 L water moved from tile 7 to tile 8.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 2 got stuck on tile 8.")

        // Cloud 4
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 4 on tile 10 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 4 with 5900 L water moved from tile 10 to tile 9.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 4 on tile 9 rained down 5900 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 4 dissipates on tile 9.")

        // Merged Cloud (Cloud 5)
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 5 on tile 4 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 5 with 4950 L water moved from tile 4 to tile 5.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 95.")
        // assertNextLine("[DEBUG] Cloud Position: Cloud 5 is on tile 5, where the amount of sunlight is 48.")
        // Should Dissipate now
        assertNextLine("[INFO] Cloud Dissipation: Cloud 5 dissipates on tile 5.")
    }
}
