package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Farm system test
 */
class MachineChoice : CloudSystemExtension() {
    override val name = "Machine choice order"
    override val description = "tests if the machines are chosen in the right order"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmmachineorder.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapmachineorder.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapmachineorder.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmmachineorder.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 17 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pu" +
                "rsue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 4 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 2 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 4 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 unloads 3400000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 3 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 6 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 4 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 6 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 unloads 3400000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 5 for 7 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 3 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 6 for 7 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 3 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 unloads 3400000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 7 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 1700000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 8 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 1115370 g of APPLE.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 11900000 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 11900000 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estim" +
                "ate still in fields and plantations: 1115370 g."
        )
    }
}
