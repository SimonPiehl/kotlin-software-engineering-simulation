package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * City expansion on cloud
 */
class CityExpansionOnCloud : SystemTestSELab25() {
    override val name = "City Expansion On Cloud"
    override val description = "test merging when city expansion on cloud"

    override val farms = "tim/smallTests/farm4.json"
    override val scenario = "tim/smallTests/scenario4.json"
    override val map = "tim/smallTests/map4.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        // assertNextLine("[IMPORTANT] Incident: Incident 0 of type CITY_EXPANSION happened and affected tiles [0].")
        getNextLine() // TODO
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 got stuck on tile 0.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("finished its actions.")) return
        return skipUntilIncidents()
    }
}
