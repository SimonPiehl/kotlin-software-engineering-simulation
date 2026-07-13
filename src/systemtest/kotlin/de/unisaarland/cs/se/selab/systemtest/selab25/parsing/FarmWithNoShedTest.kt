package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Field owner not exists test
 *
 */
class FarmWithNoShedTest : SystemTestSELab25() {
    override val name = "FarmWithNoShedTest"
    override val description = "tests farm without farmsteads "

    override val farms = "tim/smallTests/farmInvalid2.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 8

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farmInvalid2.json is invalid.")
    }
}
