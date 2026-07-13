package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val STARY = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val ENDY = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val NODEBU = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
const val PLAN = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5."
const val TRES = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."

/**
 * Farm system test
 */
class FarmFallow : CloudSystemExtension() {
    override val name = "FarmFallow"
    override val description = "Are we rally fallow enough"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmfallow.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapfallow.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 15
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapfallow.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmfallow.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 10 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 10 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it int" +
                "ends to pursue in this tick: 7."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs SOWING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 6 has sowed PUMPKIN according to sowing plan 7.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 6 is finished and ret" +
                "urns to the shed at 0."
        )
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 450000 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 11 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(NODEBU)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 405000 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 12 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(
            "[IMPORTANT] Farm Action: Machine 6 performs WEEDING on ti" +
                "le 1 for 6 days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 6 is fin" +
                "ished and returns to the shed at 0."
        )
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 364500 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 13 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 328050 g of PUMPKIN.")
        n1()
        n2()
    }
    suspend fun n1() {
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 14 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs WEEDING on tile 1 for 6 days.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 6 is fin" +
                "ished and returns to the shed at 0."
        )
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 295245 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 15 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 265720 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 16 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs WEEDING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0.")
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 239148 g of PUMPKIN.")
        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 17 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 6 has collected 239148 g of PUMPKIN harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 6 unloads 239148 g of PUMPKIN harvest in the shed.")
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 18 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
    }
    suspend fun n2() {
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 19 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 10 started at tick 20 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 11 started at tick 21 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 12 started at tick 22 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(PLAN)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine(ENDY)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 960000 g of WHEAT.")
        assertNextLine("[INFO] Simulation Info: Tick 13 started at tick 23 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(NODEBU)
        assertNextLine(ENDY)
        assertNextLine("[INFO] Simulation Info: Tick 14 started at tick 24 within the year.")
        assertNextLine(TRES)
        assertNextLine(STARY)
        assertNextLine(NODEBU)
        assertNextLine(ENDY)
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 15.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 239148 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 239148 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
    }
}
