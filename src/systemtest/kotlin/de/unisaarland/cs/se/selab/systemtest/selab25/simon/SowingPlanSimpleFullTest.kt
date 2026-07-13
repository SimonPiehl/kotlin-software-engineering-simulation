package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Sowing plan simple full test
 *
 * @constructor Create empty Sowing plan simple full test
 */
class SowingPlanSimpleFullTest : ExampleSystemTestExtension() {
    override val name = "SowingPlanSimpleFullTest Simon"
    override val description = "full test"

    override val farms = "simon/SowingPlanSimpleFarm.json"
    override val scenario = "simon/CloudMergeSimpleScenario.json"
    override val map = "simon/SowingPlanSimpleMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 4

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: SowingPlanSimpleMap.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: SowingPlanSimpleFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: CloudMergeSimpleScenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 4 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 4 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 1 is on tile 1, where the amount of sunlight is 62.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: CUTTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 850000 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 5 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 1 is on tile 1, where the amount of sunlight is 76.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 765000 g of APPLE.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 2.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 765000 g."
        )
    }
}
