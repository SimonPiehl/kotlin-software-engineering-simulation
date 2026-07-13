package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Farm Controller system extension
 *
 * @constructor Create empty Cloud system extension
 */
abstract class FarmControllerSystemTestExtension : SystemTestSELab25() {
    suspend fun skipUntilFarmStartsItsAction(farmID: Int): String? {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith("[IMPORTANT] Farm: Farm $farmID starts its actions.")) {
            line
        } else {
            skipUntilFarmStartsItsAction(farmID)
        }
    }
}
