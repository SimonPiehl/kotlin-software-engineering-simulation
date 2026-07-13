package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Field owner not exists test
 *
 */
class FieldOwnerNotExistsTest : SystemTestSELab25() {
    override val name = "FieldOwnerNotExistsTest"
    override val description = "tests field Tile with non existing farmOwnerID in map"

    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "tim/smallTests/map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map1.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farms.json is invalid.")
    }
}
