package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that farm names can be the empty string
 *
 * @constructor Create empty Sowing plan invalid because city expansion two
 */
class FarmNameCanBeEmpty : ExampleSystemTestExtension() {
    override val name = "FarmNameCanBeEmpty"
    override val description = "Tests that Farm Names Can Be Empty"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmNameCanBeEmptyFarms.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: farmNameCanBeEmptyFarms.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: scenario.json successfully parsed and validated."
        )
    }
}
