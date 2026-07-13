package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class NovemberResetAccurate : SystemTestSELab25() {
    override val name = "NovemberResetAccurate"
    override val description = "tests reset of harvest in NOV acc"

    override val farms = "tim/PlantationFarming/novReset/farms.json"
    override val scenario = "tim/PlantationFarming/novReset/scenario.json"
    override val map = "tim/PlantationFarming/novReset/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20 // Late Oct

    override suspend fun run() {
        skipUntilFarmActionEnd()
        // Animal Attack
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 2,3,4,5.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of APPLE.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 0 g of GRAPE.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 5 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of ALMOND.")
        skipUntilFarmActionEnd()
        // Tick 1
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1530000 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1200000 g of GRAPE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1200000 g of CHERRY.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 800000 g of ALMOND.")
        skipUntilStat()
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 4730000 g."
        )
    }

    private suspend fun skipUntilFarmActionEnd() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilFarmActionEnd()
    }

    private suspend fun skipUntilStat() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line == "[IMPORTANT] Simulation Info: Simulation statistics are calculated.") return
        return skipUntilStat()
    }
}
