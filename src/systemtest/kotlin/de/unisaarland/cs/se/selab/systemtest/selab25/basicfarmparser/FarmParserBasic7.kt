package de.unisaarland.cs.se.selab.systemtest.selab25.basicfarmparser

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that a field cannot be a square
 *
 * @constructor Create empty Field is square invalid system test
 */
class FarmParserBasic7 : ExampleSystemTestExtension() {
    override val name = "hasallfields4"
    override val description = "hasallfields4.json"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmParserSimple/FarmParserjson/hasallfields4.json"
    override val scenario = "FarmParserSimple/FarmParserjson/Scenario.json"
    override val map = "FarmParserSimple/FarmParserjson/Map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: Map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: hasallfields4.json is invalid.")
    }
}
