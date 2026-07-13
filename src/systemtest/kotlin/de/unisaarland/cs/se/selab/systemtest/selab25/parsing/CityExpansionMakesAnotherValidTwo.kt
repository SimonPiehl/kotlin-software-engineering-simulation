package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class CityExpansionMakesAnotherValidTwo : SystemTestSELab25() {
    override val name = "CityExpansionMakesAnotherValid2"
    override val description = "tests city expansion valid bc id order"

    override val farms = "tim/scenarioParsing/farms.json"
    override val scenario = "tim/scenarioParsing/scenario2.json"
    override val map = "tim/scenarioParsing/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario2.json is invalid.")
    }
}
