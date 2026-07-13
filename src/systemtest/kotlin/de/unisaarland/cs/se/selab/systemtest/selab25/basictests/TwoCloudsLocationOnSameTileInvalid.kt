package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that two clouds cannot have the same location at initialization
 *
 * @constructor Create empty Sowing plan invalid because city expansion two
 */
class TwoCloudsLocationOnSameTileInvalid : ExampleSystemTestExtension() {
    override val name = "TwoCloudsLocationOnSameTileInvalid"
    override val description = "Checks that two clouds cannot have the same location at initialization"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/twoCloudsLocationOnSameTileInvalidScenario.json"
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
            "[IMPORTANT] Initialization Info: twoCloudsLocationOnSameTileInvalidScenario.json is invalid."
        )
    }
}
