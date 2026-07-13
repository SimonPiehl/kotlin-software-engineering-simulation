package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Basic plant type almond test
 *
 * @constructor Create empty Basic plant type almond test
 */
class BasicPlantTypeAlmondTest : ExampleSystemTestExtension() {
    override val name = "BasicPlantTypeAlmondTest"
    override val description = "Tests if CUTTING not performed logged start in late february"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "BasicPlantTypeAlmondTest/farms.json"
    override val scenario = "BasicPlantTypeAlmondTest/scenario.json"
    override val map = "BasicPlantTypeAlmondTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 4

    override suspend fun run() {
        // Skip to Line with first message containing harvest estimate
        skipUntilLogType(LogLevel.DEBUG, LogType.HARVEST_ESTIMATE)
        // Check if current line equal
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: CUTTING.")
    }
}
