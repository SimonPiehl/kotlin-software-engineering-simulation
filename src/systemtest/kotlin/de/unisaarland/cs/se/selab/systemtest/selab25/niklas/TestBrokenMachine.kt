package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Example system test
 */
class TestBrokenMachine : ExampleSystemTestExtension() {
    override val name = "TestBrokenMachine Niklas"
    override val description = "Tests a BROKEN_MACHINE incident. " +
        "The Broken Machine is going to miss HARVESTING of the APPLE plantation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsBrokenMachine.json"
    override val scenario = "niklas/scenarioBrokenMachines.json"
    override val map = "niklas/mapBrokenMachine.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        // First tick starts (Late August)
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.")

        // Next Tick starts (Early September)
        skipUntilIncidents()
        // No more incidents, so we are at Harvest Estimation
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 731793 g of APPLE.")

        // Next Tick starts (Late September)
        skipUntilIncidents()
        // No more incidents, so we are at Harvest Estimation

        // Next Tick starts (Early October)
        skipUntilIncidents()
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 216057 g of APPLE.")

        // No more incidents, so we are at Harvest Estimation

        // Next Tick starts (Late October)
        skipUntilIncidents()
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of APPLE.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
