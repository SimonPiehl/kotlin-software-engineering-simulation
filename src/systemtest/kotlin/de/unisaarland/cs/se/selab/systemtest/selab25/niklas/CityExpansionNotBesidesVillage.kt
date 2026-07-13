package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Scenario should be invalid. City expansion is not besides VILLAGE
 *
 * @constructor
 */
class CityExpansionNotBesidesVillage : ExampleSystemTestExtension() {
    override val name = "CityExpansionNotBesidesVillage Niklas"
    override val description = "Scenario should be invalid. City expansion is not besides VILLAGE."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/scenarioCityExpansionNotBesidesVillage.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: farms.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: scenarioCityExpansionNotBesidesVillage.json is invalid."
        )
    }
}
