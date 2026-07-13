package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Checks if a Field has no farm key
 *
 * @constructor
 */
class FieldHasNoFarmOwner : ExampleSystemTestExtension() {
    override val name = "FieldHasNoFarmOwner Niklas"
    override val description = "A FIELD doesnt have the farm key."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapFieldHasNoOwner.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: mapFieldHasNoOwner.json is invalid."
        )
    }
}
