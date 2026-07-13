package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud creation on village test
 */
class CloudCreationOnVillageTest : SystemTestSELab25() {
    override val name = "CloudCreationOnVillage"
    override val description = "test parsing of cloudCreation only on Village Tiles"

    override val farms = "tim/CloudCreationOnVillageTest/farms.json"
    override val scenario = "tim/CloudCreationOnVillageTest/scenario.json"
    override val map = "tim/CloudCreationOnVillageTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
