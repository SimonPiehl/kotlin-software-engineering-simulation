package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Field owner not exists test
 *
 */
class FarmWithNonExistingFieldTest : SystemTestSELab25() {
    override val name = "FarmWithNonExistingFieldTest"
    override val description = "Tests validation of farm with no farmstead in farm json"

    override val farms = "tim/smallTests/farmInvalid1.json"
    override val scenario = "example/scenario.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 8

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farmInvalid1.json is invalid.")
    }
}
