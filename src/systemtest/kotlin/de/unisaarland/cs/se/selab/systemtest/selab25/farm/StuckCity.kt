
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Farm system test
 */
class StuckCity : CloudSystemExtension() {
    override val name = "Machine stuck"
    override val description = "Tests if machine can get stuck"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmstuck.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapstuck.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapstuck.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmstuck.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 17 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it in" +
                "tends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 3400000 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 3400000 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fi" +
                "elds and plantations: 0 g."
        )
    }
}
