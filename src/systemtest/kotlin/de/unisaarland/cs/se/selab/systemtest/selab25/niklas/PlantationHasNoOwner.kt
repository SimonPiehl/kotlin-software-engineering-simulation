package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Checks if a Plantation has no farm key
 *
 * @constructor
 */
class PlantationHasNoOwner : ExampleSystemTestExtension() {
    override val name = "PlantationHasNoOwner Niklas"
    override val description = "A Plantation doesnt have the farm key."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapPlantationHasNoOwner.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: mapPlantationHasNoOwner.json is invalid."
        )
    }
}
