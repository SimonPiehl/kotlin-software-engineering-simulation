package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class IrrigationChaos : SystemTestSELab25() {
    override val name = "IRRIGATION_CHAOS"
    override val description = "tests irrigation of Grape, 2 farms on map"

    override val farms = "tim/PlantationFarming/irrigate/farms.json"
    override val scenario = "tim/PlantationFarming/irrigate/scenario.json"
    override val map = "tim/PlantationFarming/irrigate/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilFarmAction()
        assertCurrentLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 4 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 5 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        // Farm 1
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        getNextLine() // Sowing Plan
        // Cannot irrigate bc no way
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        // Penalty for Irrigation missed
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 1199950 g of GRAPE.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of GRAPE.")
        // Sim ended
        getNextLine()
        // Statistic
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.")
        skipUntilEnd()
        assertCurrentLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 3599950 g."
        )
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }

    private suspend fun skipUntilEnd() {
        val line = getNextLine() ?: error("h")
        if (line.contains("[IMPORTANT] Simulation Statistics: Total harvest estimate still in")) return
        return skipUntilEnd()
    }
}
