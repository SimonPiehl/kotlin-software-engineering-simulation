package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tests that a FIELD lies fallow after HARVESTING.
 */
class HasToLieFallow : ExampleSystemTestExtension() {
    override val name = "HasToLieFallow Niklas"
    override val description = "Tests that a FIELD lies fallow after HARVESTING. We HARVEST Pumpkin and shouldn't " +
        "start SOWING Wheat too early."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsHasToLieFallow.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapHasToLieFallow.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 16
    override val startYearTick = YearTicks.LATE_MAY

    override suspend fun run() {
        detektOne()
        detektTwo()
        detektThree()
        detektThreeAndAHalf()
        detektFour()
        detektFourAndAHalf()
        detektFive()
        detektFiveAndAHalf()
        detektSix()

        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 174338 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 301256 g."
        )
    }

    private suspend fun detektOne() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 10 within the year.")

        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 10 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thr" +
                "eshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 s" +
                "tarts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 0 is finis" +
                "hed and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished " +
                "its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 450000 g of PUMPKIN.")

        assertNextLine(
            "[INFO]" +
                " Simulation Info: Tick 1 started at tick 11 within the year."
        )
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture " +
                "is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts" +
                " its actions."
        )
    }

    private suspend fun detektTwo() {
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing pl" +
                "ans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[" +
                "IMPORTANT] Farm: Farm 0 finished its actions."
        )
        assertNextLine(
            "[" +
                "INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 405000 g of PUMPKIN."
        )

        assertNextLine(
            "[I" +
                "NFO] Simulation Info: Tick 2 started at tick 12 within the year."
        )
        assertNextLine(
            "[I" +
                "NFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IM" +
                "PORTANT] Farm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DE" +
                "BUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IM" +
                "PORTANT] Farm: Farm 0 finished its actions."
        )
        assertNextLine(
            "[DE" +
                "BUG] Harvest Estimate: Required actions on tile 1 were not performed: WEEDING."
        )
        assertNextLine(
            "[IN" +
                "FO] Harvest Estimate: Harvest estimate on tile 1 changed to 328050 g of PUMPKIN."
        )

        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 13 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold " +
                "in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its act" +
                "io" +
                "ns."
        )
    }

    private suspend fun detektThree() {
        assertNextLine(
            "[DEB" +
                "UG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished its act" +
                "io" +
                "n" +
                "s" +
                "."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 295245 g of PUMPKIN.")

        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 14 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANT" +
                "ATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its actio" +
                "ns."
        )
        assertNextLine(
            "[DEBU" +
                "G] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished its ac" +
                "ti" +
                "ons."
        )
        assertNextLine(
            "[DEBU" +
                "G] Harvest Estimate: Required actions on tile 1 were not performed: WEEDING."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 239148 g of PUMPKIN.")

        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 15 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil mois" +
                "ture is below thresho" +
                "ld in 0 FIELD and 0 PLAN" +
                "TATION tiles."
        )
    }
    private suspend fun detektThreeAndAHalf() {
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its act" +
                "ions."
        )
        assertNextLine(
            "[DEBUG" +
                "] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished its " +
                "actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 215233 g of PUMPKIN.")

        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 16 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is bel" +
                "ow threshold in 0 FIELD and 0 PLANTATION tiles."
        )
    }

    private suspend fun detektFour() {
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 star" +
                "ts its actions."
        )
        assertNextLine(
            "[DEBUG]" +
                " Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 f" +
                "inished its actions."
        )
        assertNextLine(
            "[DEBUG]" +
                " Harvest Estimate: Required actions on tile 1 were not performed: WEEDING."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 174338 g of PUMPKIN.")

        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 17 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moi" +
                "sture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 start" +
                "s its actions."
        )
        assertNextLine(
            "[DEBUG] " +
                "Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 174338 g of PUMPKIN harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 174338 g of PUMPKIN harvest in the shed.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finishe" +
                "d its actions."
        )

        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 18 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moist" +
                "ure is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
    }

    private suspend fun detektFourAndAHalf() {
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 " +
                "starts its actions."
        )
        assertNextLine(
            "[DEBUG] F" +
                "arm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: " +
                "Farm 0 finished its actions."
        )

        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 19 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil m" +
                "oisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 sta" +
                "rts its ac" +
                "t" +
                "ions."
        )
        assertNextLine(
            "[DEBUG] Fa" +
                "rm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
    }

    private suspend fun detektFive() {
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished" +
                " its actions."
        )

        assertNextLine("[INFO] Simulation Info: Tick 10 started at tick 20 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The so" +
                "il moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Far" +
                "m 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] Far" +
                "m: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finis" +
                "hed its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 11 started at tick 21 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thr" +
                "eshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 st" +
                "arts its actions."
        )
        assertNextLine(
            "[DEBUG] Far" +
                "m: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fini" +
                "shed its actions."
        )

        assertNextLine("[INFO] Simulation Info: Tick 12 started at tick 22 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moi" +
                "sture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 st" +
                "arts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm" +
                ": Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
    }

    private suspend fun detektFiveAndAHalf() {
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fin" +
                "ished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 960000 g of WHEAT.")

        assertNextLine("[INFO] Simulation Info: Tick 13 started at tick 23 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil m" +
                "oisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its ac" +
                "tion" +
                "s."
        )
        assertNextLine(
            "[DEBUG] Farm:" +
                " Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fini" +
                "shed its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 14 started at tick 24 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thresho" +
                "ld in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its ac" +
                "tions."
        )
        assertNextLine(
            "[DEBUG] Farm:" +
                " Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fi" +
                "nished its actions."
        )
    }

    private suspend fun detektSix() {
        assertNextLine("[INFO] Simulation Info: Tick 15 started at tick 1 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 F" +
                "IELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its a" +
                "ctions."
        )
        assertNextLine(
            "[DEBUG] Farm: " +
                "Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 fin" +
                "ished its actions."
        )
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 864000 g of WHEAT.")
    }
}
