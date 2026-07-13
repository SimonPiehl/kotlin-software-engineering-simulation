package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val SOW = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5."
const val END = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val STAR = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val SOI = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."

/**
 * Farm system test
 */
class Farmdrought : CloudSystemExtension() {
    override val name = "Farmdrought"
    override val description = "Farmdrought"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmdrought.json"
    override val scenario = "FarmControllertest/scenariodrought.json"
    override val map = "FarmControllertest/mapdrought.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 20
    override val startYearTick = 16

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapdrought.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmdrought.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenariodrought.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 16 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 16 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PL" +
                "ANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its ac" +
                "tions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it in" +
                "tends to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1115370 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1115370 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1115370 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1115370 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 17 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thr" +
                "eshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(STAR)
        n0()
        n1()
        n2()
        n3()
    }
    suspend fun n0() {
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pu" +
                "rsue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 1 for 2 days.")
        assertNextLine(
            "[IMPORTANT] Farm Harvest: Machine 6 has collecte" +
                "d 1115370 g of APPLE harvest."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 2 for 2 days.")
        assertNextLine(
            "[IMPORTANT] Farm Harvest: Machine 6 has collected 111" +
                "5370 g of APPLE harvest."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 6 has collected 1115370 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 4 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 6 has collected 1115370 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 unloads 4461480 g of APPLE harvest in the shed.")
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 18 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thres" +
                "hold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing p" +
                "lans it intends to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 19 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it inte" +
                "nds to pursue in this tick: ."
        )
        assertNextLine(END)
    }
    suspend fun n1() {
        assertNextLine(
            "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and " +
                "affected tiles 1,2,3,4,5,6,7."
        )
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 20 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 21 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 22 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 23 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 24 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 1 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 10 started at tick 2 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
    }
    suspend fun n2() {
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 11 started at tick 3 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 12 started at tick 4 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 13 started at tick 5 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 14 started at tick 6 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(SOW)
        assertNextLine("[IMPORTANT] Farm Action: Machine 7 performs SOWING on tile 5 for 2 days.")
        assertNextLine(
            "[IMPORTANT] Farm Sowing: Machine 7 has sowe" +
                "d OAT according to sowing plan 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 7 performs SOWING on tile 6 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 7 has sowed OAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 7 performs SOWING on tile 7 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 7 has sowed OAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 7 is finished and returns to the shed at 0.")
        assertNextLine(END)
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 5 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of OAT.")
        assertNextLine("[INFO] Simulation Info: Tick 15 started at tick 7 within the year.")
        assertNextLine(SOI)
    }
    suspend fun n3() {
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intend" +
                "s to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 16 started at tick 8 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the follo" +
                "wing active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 17 started at tick 9 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 18 started at tick 10 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing pla" +
                "ns it intends to pursue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[INFO] Simulation Info: Tick 19 started at tick 11 within the year.")
        assertNextLine(SOI)
        assertNextLine(STAR)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pur" +
                "sue in this tick: ."
        )
        assertNextLine(END)
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 20.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 4461480 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 4461480 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate st" +
                "ill in fields and plantations: 0 g."
        )
    }
}
