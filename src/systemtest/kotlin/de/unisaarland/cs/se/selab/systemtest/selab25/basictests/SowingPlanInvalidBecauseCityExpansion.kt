package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that a field cannot be a square
 *
 * @constructor Create empty Field is square invalid system test
 */
class SowingPlanInvalidBecauseCityExpansion : ExampleSystemTestExtension() {
    override val name = "SowingPlanInvalidBecauseCityExpansion"
    override val description = "Tests that a sowing plan that doesnt contain any fields, " +
        "because of City Expansion, is invalid (here: incident is before Sowing Plan)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/sowingPlanInvalidBecauseCityExpansionFarms.json"
    override val scenario = "niklas/sowingPlanInvalidBecauseCityExpansionScenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: map.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: sowingPlanInvalidBecauseCityExpansionFarms.json " +
                "successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: sowingPlanInvalidBecauseCityExpansionScenario.json " +
                "is invalid."
        )
    }
}
