package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Adds skips to basic systemtest class
 */
abstract class NeSystemTestExtension : SystemTestSELab25() {
    /**
     * Checks if anything is found
     */
    suspend fun getIfAnyOutput(): String {
        return getNextLine() ?: "No Line Found"
    }
}
