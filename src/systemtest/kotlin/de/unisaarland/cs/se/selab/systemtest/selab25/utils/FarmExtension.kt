package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud system extension
 *
 * @constructor Create empty Cloud system extension
 */
abstract class FarmExtension : SystemTestSELab25() {
    suspend fun skipUntilExcludingCloudCategory() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Soil Moisture:")) return
        skipUntilExcludingCloudCategory()
    }

    suspend fun skipUntilExcludingIncidentCategory() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        skipUntilExcludingIncidentCategory()
    }

    suspend fun skipUntilFarm() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("you messed up your farm check buddy")
        if (line.contains("[IMPORTANT] Farm: Farm 0 starts its actions.")) return
        skipUntilFarm()
    }
}
