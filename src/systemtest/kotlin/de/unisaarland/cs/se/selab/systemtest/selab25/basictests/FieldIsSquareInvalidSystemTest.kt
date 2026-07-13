package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that a field cannot be a square
 *
 * @constructor Create empty Field is square invalid system test
 */
class FieldIsSquareInvalidSystemTest : ExampleSystemTestExtension() {
    override val name = "FieldIsSquareInvalidSystemTest"
    override val description = "Tests that fields cannot be square"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/fieldIsSquareInvalidSystemTestMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Initialization Info: fieldIsSquareInvalidSystemTestMap.json is invalid.")
    }
}
