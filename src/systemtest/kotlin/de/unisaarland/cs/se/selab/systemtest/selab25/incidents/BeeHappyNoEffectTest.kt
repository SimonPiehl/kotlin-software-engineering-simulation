package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Bee happy no effect test
 */
class BeeHappyNoEffectTest : SystemTestSELab25() {
    override val name = "bee happy no effect"
    override val description = "test behavior of bee happy with 0 effect"

    override val farms = "example/farms.json"
    override val scenario = "tim/incidents/beeHappy/beeHappyScenario1.json"
    override val map = "tim/incidents/beeHappy/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: beeHappyScenario1.json is invalid.")
    }
}
