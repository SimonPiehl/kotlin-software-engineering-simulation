package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Checks that sowing plan is invalid because city expansion
 *
 * @constructor Create empty Sowing plan invalid because city expansion two
 */
class SowingPlanInvalidBecauseCityExpansionTwo : ExampleSystemTestExtension() {
    override val name = "SowingPlanInvalidBecauseCityExpansionTwo"
    override val description = "Tests that a sowing plan that doesn't contain any fields, " +
        "because of City Expansion, is invalid (here: incident is after Sowing Plan)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/sowingPlanInvalidBecauseCityExpansionFarms.json"
    override val scenario = "niklas/sowingPlanInvalidBecauseCityExpansionTwoScenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: sowingPlanInvalidBecauseCityExpansionFarms.json" +
                " successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: sowingPlanInvalidBecauseCityExpansionTwoScenario.json is invalid."
        )
    }
}
