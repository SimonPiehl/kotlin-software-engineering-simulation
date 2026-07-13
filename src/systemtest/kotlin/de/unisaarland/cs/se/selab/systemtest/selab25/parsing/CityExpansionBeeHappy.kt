package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * City expansion bee happy
 *
 * @constructor Create empty City expansion bee happy
 */
class CityExpansionBeeHappy : ExampleSystemTestExtension() {
    override val name = "Example city Expansion"
    override val description = "tests bee happy after cit expansion"

    override val farms = "simon/cityExpansionBeeHappyFarms.json"
    override val scenario = "simon/cityExpansionBeeHappyScenario.json"
    override val map = "simon/cityExpansionBeeHappyMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 2

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: cityExpansionBeeHappyMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: cityExpansionBeeHappyFarms.json successfully parsed and validated.")
        assertNextLine(
            "[INFO] Initialization Info: cityExpansionBeeHappyScenario.json successfully parsed and validated."
        )
        //
    }
}
