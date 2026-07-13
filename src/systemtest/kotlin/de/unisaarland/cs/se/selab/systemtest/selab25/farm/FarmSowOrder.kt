
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Farm system test
 */
class FarmSowOrder : CloudSystemExtension() {
    override val name = "Farm sowing order"
    override val description = "Tests sowing order"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmsoworder.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapsoworder.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 4

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapsoworder.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmsoworder.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 4 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 4 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FI" +
                "ELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 sta" +
                "rts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it i" +
                "ntends to pursue in this tick: 9."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished its ac" +
                "tions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 5 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following acti" +
                "ve sowing plans it intends to pursue in this tick: 5,6,9."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 6 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it inte" +
                "nds to pursue in this tick: 5,6,9."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 9.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 9.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 3 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs SOWING on tile 4 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 6 has sowed OAT according to sowing plan 6.")
        n1()
    }
    suspend fun n1() {
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fini" +
                "shed its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1080000 g of OAT.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 3.")
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
            "[IMPORTANT] Simulation Statistics: Total harvest esti" +
                "mate still in fields and plantations: 4320000 g."
        )
    }
}
