package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class WheatCycleExtra : SystemTestSELab25() {
    override val name = "Wheat Test cycle Extra"
    override val description = "tests sowing of wheat and sowing plan"

    override val farms = "tim/fieldFarming/wheatcycextra/farms.json"
    override val scenario = "tim/fieldFarming/wheatcycextra/scenario.json"
    override val map = "tim/fieldFarming/wheatcycextra/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20 // Late October

    override suspend fun run() {
        skipUntilFarmAction()
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0,1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 2 days.")
        assertSowingPlan(0)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 2 days.")
        assertSowingPlan(0)
        // Cannot perform sowing on t4: range
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        // Cannot fulfill plan 1 as no machine left
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        skipUntilFarmAction()
        // Now try to fulfill plan 1
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        //
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 5 for 2 days.")
        assertSowingPlan(1)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 7 for 2 days.")
        assertSowingPlan(1)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 6 for 2 days.")
        assertSowingPlan(1)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
    private suspend fun assertSowingPlan(id: Int) {
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan $id.")
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }
}
