package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Scenario should be invalid. City expansion is besides FOREST.
 *
 * @constructor
 */
class CityExpansionBesidesForest : ExampleSystemTestExtension() {
    override val name = "CityExpansionBesidesForest Niklas"
    override val description = "Scenario should be invalid. City expansion is besides FOREST."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/scenarioCityExpansionBesidesForest.json"
    override val map = "niklas/mapCityExpansionBesidesForest.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: mapCityExpansionBesidesForest.json " +
                "successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: farms.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: scenarioCityExpansionBesidesForest.json is invalid."
        )
    }
}
