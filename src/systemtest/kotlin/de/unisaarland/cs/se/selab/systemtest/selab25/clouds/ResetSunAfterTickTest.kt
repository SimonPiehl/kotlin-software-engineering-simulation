package de.unisaarland.cs.se.selab.systemtest.selab25.clouds

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class ResetSunAfterTickTest : SystemTestSELab25() {
    override val name = "ResetSunAfterTick"
    override val description = "test resetting of sunlight to monthly after every tick"

    override val farms = "example/farms.json"
    override val scenario = "tim/smallTests/scenarioSun.json"
    override val map = "example/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 2
    override suspend fun run() {
        skipUntilCloud()
        assertNextLine("[DEBUG] Cloud Position: Cloud 0 is on tile 1, where the amount of sunlight is 48.")
        skipUntilCloud()
        assertNextLine("[DEBUG] Cloud Position: Cloud 0 is on tile 1, where the amount of sunlight is 62.")
    }
    private suspend fun skipUntilCloud() {
        val line: String = getNextLine() ?: error("No line found")
        if (line.contains("Soil Moisture")) return
        return skipUntilCloud()
    }
}
