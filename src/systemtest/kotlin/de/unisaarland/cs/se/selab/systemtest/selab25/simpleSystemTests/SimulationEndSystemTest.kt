package de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simulation start system test
 *
 * @constructor Create empty Simulation start system test
 */
class SimulationEndSystemTest : ExampleSystemTestExtension() {
    override val name = "SimulationEndSystemTest"
    override val description = "tests logging at the begin + end of Simulation"

    override val farms = "tim/SimStart/farms.json"
    override val scenario = "tim/SimStart/scenario.json"
    override val map = "tim/SimStart/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 20

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        //
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 20 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 20 within the year.")
        skipUntilSimEnd()
        assertCurrentLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
    }

    private suspend fun skipUntilSimEnd() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("Simulation ended at tick")) return
        return skipUntilSimEnd()
    }
}
