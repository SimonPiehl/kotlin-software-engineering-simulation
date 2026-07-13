package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Tests that two Cloud Creations that happen in the same Tick cannot intersect in the Tiles they affect
 *
 * @constructor
 */
class CloudCreationsIntersectInvalidScenario : ExampleSystemTestExtension() {
    override val name = "CloudCreationsIntersectInvalidScenario"
    override val description = "Tests that two Cloud Creations that happen in the same Tick" +
        " cannot intersect in the Tiles they affect"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/cloudCreationsIntersectInvalidScenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: farms.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: cloudCreationsIntersectInvalidScenario.json is invalid."
        )
    }
}
