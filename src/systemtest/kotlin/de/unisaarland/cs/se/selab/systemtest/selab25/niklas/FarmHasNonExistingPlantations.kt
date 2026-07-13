package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * A Farm has elements in the key plantations, that don't exist in the map
 *
 * @constructor
 */
class FarmHasNonExistingPlantations : ExampleSystemTestExtension() {
    override val name = "FarmHasNonExistingPlantations Niklas"
    override val description = "A Farm has elements in the key plantations, that don't exist in the map"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmHasNonExistingPlantations.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine(
            "[IMPORTANT] Initialization Info: farmHasNonExistingPlantations.json is invalid."
        )
    }
}
