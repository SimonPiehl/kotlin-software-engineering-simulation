package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class SowingWheatTest : SystemTestSELab25() {
    override val name = "Sowing Wheat Test"
    override val description = "tests sowing of wheat and further actions"

    override val farms = "tim/fieldFarming/wheat/farms.json"
    override val scenario = "tim/fieldFarming/wheat/scenario.json"
    override val map = "tim/fieldFarming/wheat/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 20 // Late October

    override suspend fun run() {
        skipUntilFarmAction()
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 2 days.")
        assertSowingPlan(0)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 4 for 2 days.")
        assertSowingPlan(0)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 5 for 2 days.")
        assertSowingPlan(0)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        // Machine 1
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 7 for 2 days.")
        assertSowingPlan(1)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
    private suspend fun assertSowingPlan(id: Int) {
        assertNextLine("[IMPORTANT] Farm Sowing: Machine $id has sowed WHEAT according to sowing plan 0.")
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }
}
