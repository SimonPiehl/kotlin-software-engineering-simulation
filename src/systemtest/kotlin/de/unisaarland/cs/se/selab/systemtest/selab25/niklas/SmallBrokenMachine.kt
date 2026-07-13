package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Example system test
 */
class SmallBrokenMachine : ExampleSystemTestExtension() {
    override val name = "SmallBrokenMachine Niklas"
    override val description = "Tests a BROKEN_MACHINE incident. " +
        "Only the logger message for Incident"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsBrokenMachine.json"
    override val scenario = "niklas/scenarioBrokenMachines.json"
    override val map = "niklas/mapBrokenMachine.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        // First tick starts (Late August)
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
