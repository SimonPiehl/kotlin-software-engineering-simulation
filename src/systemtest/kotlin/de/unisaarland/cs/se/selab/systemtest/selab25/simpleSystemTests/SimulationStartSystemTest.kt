package de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simulation start system test
 *
 * @constructor Create empty Simulation start system test
 */
class SimulationStartSystemTest : ExampleSystemTestExtension() {
    override val name = "SimulationStartSystemTest"
    override val description = "tests logging at the beginning of Simulation"

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
    }
}
