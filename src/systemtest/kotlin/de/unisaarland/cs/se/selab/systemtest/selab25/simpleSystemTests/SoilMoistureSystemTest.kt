package de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Soil moisture system test
 *
 * @constructor Create empty Soil moisture system test
 */
class SoilMoistureSystemTest : SystemTestSELab25() {
    override val name = "Soil Moisture Reduction Test"
    override val description = "tests reduction of soil moisture with and without plants on Growable-Tile"

    override val farms = "SoilMoistureControllerSystemTest/farm.json"
    override val scenario = "SoilMoistureControllerSystemTest/scenario.json"
    override val map = "SoilMoistureControllerSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilSoilMoisture()
        skipUntilSoilMoisture()
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        skipUntilSoilMoisture()
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
    }

    // Helper to skip to soil Moisture
    suspend fun skipUntilSoilMoisture() {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached.")
        if (line.endsWith("within the year.")) return
        skipUntilSoilMoisture()
    }
}
