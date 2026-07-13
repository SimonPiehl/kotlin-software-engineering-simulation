package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Field owner not exists test
 *
 */
class NoGrowableExistsTest : SystemTestSELab25() {
    override val name = "No Growable exists"
    override val description = "tests map/ farm without fields or plantations"

    override val farms = "tim/smallTests/farmNoGrow.json"
    override val scenario = "example/scenario.json"
    override val map = "tim/smallTests/mapNoGrow.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapNoGrow.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farmNoGrow.json is invalid.")
    }
}
