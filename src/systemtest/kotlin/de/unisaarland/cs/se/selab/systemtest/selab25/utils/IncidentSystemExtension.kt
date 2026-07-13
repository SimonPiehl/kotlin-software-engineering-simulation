package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Incident system extension
 *
 * @constructor Create empty Cloud system extension
 */
abstract class IncidentSystemExtension : SystemTestSELab25() {
    suspend fun skipUntilIncidentOccurs(): String? {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith("[IMPORTANT] Incident:")) {
            line
        } else {
            skipUntilIncidentOccurs()
        }
    }
}
