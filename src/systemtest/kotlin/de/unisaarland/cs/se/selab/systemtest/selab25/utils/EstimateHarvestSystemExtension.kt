package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Estimate harvest system extension
 *
 * @constructor Create empty Estimate harvest system extension
 */
abstract class EstimateHarvestSystemExtension : SystemTestSELab25() {
    suspend fun skipUntilSimulationStatistics() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Simulation Statistics: Total amount of CHERRY harvested: 0 g.")) return
        skipUntilSimulationStatistics()
    }

    suspend fun skipUntilExcludingIncidentCategory() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        skipUntilExcludingIncidentCategory()
    }

    suspend fun skipUntilSimulationStatisticsfirst() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End  of log reached when there should be more.")
        if (line.contains("Simulation Info: Simulation statistics are calculated.")) return
        skipUntilSimulationStatisticsfirst()
    }

    suspend fun skipUntilSowingPlan2() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End  of log reached when there should be more.")
        if (line.contains("it intends to pursue in this tick: 2.")) return
        skipUntilSowingPlan2()
    }
}
