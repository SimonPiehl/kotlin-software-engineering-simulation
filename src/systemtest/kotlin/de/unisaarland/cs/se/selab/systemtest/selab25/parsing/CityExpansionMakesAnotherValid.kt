package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class CityExpansionMakesAnotherValid : SystemTestSELab25() {
    override val name = "CityExpansionMakesAnotherValid"
    override val description = "tests city expansion valid through other ce"

    override val farms = "tim/scenarioParsing/farms.json"
    override val scenario = "tim/scenarioParsing/scenario.json"
    override val map = "tim/scenarioParsing/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
    }
}
