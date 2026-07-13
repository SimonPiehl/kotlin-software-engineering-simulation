package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Basic plant type apple test2
 *
 * @constructor Create empty Basic plant type apple test2
 */
class BasicPlantTypeAlmondTest3 : ExampleSystemTestExtension() {
    override val name = "BasicPlantTypeAlmondTest3"
    override val description = "Tests if MOWING almond not performed logged, start" +
        " in early june and estimation right"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "BasicPlantTypeAlmondTest/farms.json"
    override val scenario = "BasicPlantTypeAlmondTest/scenario.json"
    override val map = "BasicPlantTypeAlmondTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 12
    override val startYearTick = 11

    override suspend fun run() {
        // Skip to Line with first message containing harvest estimate
        skipUntilLogType(LogLevel.DEBUG, LogType.HARVEST_ESTIMATE)
        // Check if current line equal
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 648000 g of ALMOND.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 648000 g of ALMOND.")
        // Skip to early September
        skipUntilLogType(LogLevel.DEBUG, LogType.HARVEST_ESTIMATE)
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 425152 g of ALMOND.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 425152 g of ALMOND.")
    }
}
