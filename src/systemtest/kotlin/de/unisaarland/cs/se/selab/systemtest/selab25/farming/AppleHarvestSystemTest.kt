package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class AppleHarvestSystemTest : SystemTestSELab25() {
    override val name = "AppleHarvestSystemTest"
    override val description = "tests harvesting apples 1 tick later"

    override val farms = "tim/PlantationFarming/apples/farms.json"
    override val scenario = "tim/PlantationFarming/apples/scenario.json"
    override val map = "tim/PlantationFarming/apples/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilFarmAction()
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 850000 g of APPLE harvest.")
        // Action continue
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 2 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 850000 g of APPLE harvest.")
        // No continue as maxDays reached
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 4.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 1700000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        // TODO statistics
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }
}
