package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class CloudCreationDurationTest : SystemTestSELab25() {
    override val name = "CloudCreationDuration"
    override val description = "test duration of a created cloud"

    override val farms = "example/farms.json"
    override val scenario = "tim/smallTests/scenarioCC.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type CLOUD_CREATION happened and affected tiles 1.")
        skipUntilClouds()
        // Dissipate due to Duration
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 1.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("finished its actions.")) return
        return skipUntilIncidents()
    }
    private suspend fun skipUntilClouds() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Soil Moisture")) return
        return skipUntilClouds()
    }
}
