package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Drought moisture reduction system test
 *
 * @constructor Create empty Drought moisture reduction system test
 */
class DroughtMoistureReductionSystemTest : ExampleSystemTestExtension() {
    override val name = "DroughtMoistureReductionSystemTest Simon"
    override val description = "Moisture reduction shouldn't be logged"

    override val farms = "example/farms.json"
    override val scenario = "simon/DroughtMoistureReductionScenario.json"
    override val map = "simon/example.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilMoistureMessage()
        skipUntilMoistureMessage()
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
        skipUntilMoistureMessage()
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        skipUntilMoistureMessage()
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
    }

    private suspend fun skipUntilMoistureMessage() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("within the year.")) return
        return skipUntilMoistureMessage()
    }
}
