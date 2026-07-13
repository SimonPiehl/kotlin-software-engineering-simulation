package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.NeSystemTestExtension

/**
 * Example system test
 */
class TestForOutputSystemTest : NeSystemTestExtension() {
    override val name = "ExampleTest"
    override val description = "Tests statistics after 0 ticks."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        val text = getIfAnyOutput()
        assert(text != "No Line Found")
        assert(text == "No Line Found")
    }
}
