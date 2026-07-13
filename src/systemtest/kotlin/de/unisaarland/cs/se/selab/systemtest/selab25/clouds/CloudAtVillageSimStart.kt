package de.unisaarland.cs.se.selab.systemtest.selab25.clouds
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Cloud at village sim start
 *
 * @constructor
 */
class CloudAtVillageSimStart : SystemTestSELab25() {
    override val name = "Cloud at Village Sim Start"
    override val description = "tests cloud location on Village in json"

    override val farms = "CloudSystemTest/farm0.json"
    override val scenario = "tim/smallTests/scenario1.json"
    override val map = "CloudSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        getNextLine()
        getNextLine()
        assertNextLine("[IMPORTANT] Initialization Info: scenario1.json is invalid.")
    }
}
