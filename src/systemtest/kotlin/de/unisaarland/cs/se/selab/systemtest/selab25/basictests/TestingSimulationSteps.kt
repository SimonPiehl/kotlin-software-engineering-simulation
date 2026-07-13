package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Checks, that the Simulation exactly runs for 10 ticks
 *
 * @constructor Create 10 Testing simulation steps
 */
class TestingSimulationSteps : ExampleSystemTestExtension() {
    override val name = "TestingSimulationSteps"
    override val description = "Checks, that the Simulation exactly runs for 10 ticks"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_INFO)
        assertCurrentLine("[IMPORTANT] Simulation Info: Simulation ended at tick 10.")
    }
}
