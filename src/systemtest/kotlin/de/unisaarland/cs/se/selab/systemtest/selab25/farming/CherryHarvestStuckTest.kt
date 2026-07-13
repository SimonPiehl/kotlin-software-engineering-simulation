package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class CherryHarvestStuckTest : SystemTestSELab25() {
    override val name = "CherryHarvestStuckLoadedSystemTest"
    override val description = "tests harvesting of Cherry with Stuck Machine afterwards"

    override val farms = "tim/PlantationFarming/stuck/farms.json"
    override val scenario = "tim/PlantationFarming/stuck/scenario.json"
    override val map = "tim/PlantationFarming/stuck/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 14

    override suspend fun run() {
        skipUntilFarmAction()
        // assertNextLine(
        //    "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        // )
        getNextLine()
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1200000 g of CHERRY harvest.")
        // Action continue
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1200000 g of CHERRY harvest.")
        // No continue as til 4 not in 2 range
        // Log STuck as machine cannot move through VILLAGE with loaded harvest
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        // Log not performed harvesting on tile 4 + penalty
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 324000 g of CHERRY.")
        // Sim Ended
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        // Statistics
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 2400000 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 2400000 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 324000 g."
        )
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }
}
