package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Cloud creation cloud system test
 *
 * @constructor
 */
class IncidentCloudSystemTest : CloudSystemExtension() {
    override val name = "Cloud Creation And Cloud System Test"
    override val description = "test interaction of Clouds And Incidents CityExp & CloudCreation"

    override val farms = "cloudCreationSystemTest/farm.json"
    override val scenario = "cloudCreationSystemTest/scenario.json"
    override val map = "cloudCreationSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        // Cloud 0
        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 2000 L water moved from tile 1 to tile 2.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 95.")

        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 0 united to cloud 2 with 4000 L water and " +
                "duration 3 on tile 2."
        )
        // Cloud 2 (should be able to Move 10 Tiles now)
        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 2 to tile 3.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 3 to tile 4.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 3, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 4 to tile 5.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 5 to tile 6.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 6 to tile 7.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 6, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 7 to tile 8.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 8 to tile 9.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 8, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 9 to tile 10.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 10 to tile 11.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.")

        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 4000 L water moved from tile 11 to tile 12.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95.")
        // Cloud Movement ends as maxMoves Performed
        assertNextLine("[DEBUG] Cloud Position: Cloud 2 is on tile 12, where the amount of sunlight is 48.")

        skipUntilExcludingIncidentCategory()
        // Incident 0 (Cloud Creation) at tick 0
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type CLOUD_CREATION happened and affected tiles 12.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 2 and 3 united to cloud 4 with 5000 L water and duration 2 on tile 12."
        )
/*
        // Next Tick (Tick 1)
        skipUntilExcludingCloudCategory()
        // Check new Merged Cloud  (Cloud 4)
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 4 on tile 12 rained down 200 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 4 with 4800 L water moved from tile 12 to tile 13.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 12, the amount of sunlight is 95.")
        // Movement ends as No Airflow

        skipUntilExcludingIncidentCategory()
        // Incident 1 (City Expansion)
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type City Expansion happened and affected tiles 13.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 4 got stuck on tile 13.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 4 dissipates on tile 13.")
 */
    }
}
