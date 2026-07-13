package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud creation on village test
 */
class CloudCreationMixedTest : SystemTestSELab25() {
    override val name = "CloudCreationMixedTest"
    override val description = "test parsing of cloudCreation with village and other tiles in radius"

    override val farms = "tim/CloudCreationOnVillageTest/farms.json"
    override val scenario = "tim/CloudCreationOnVillageTest/scenario2.json"
    override val map = "tim/CloudCreationOnVillageTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario2.json successfully parsed and validated.")
    }
}
