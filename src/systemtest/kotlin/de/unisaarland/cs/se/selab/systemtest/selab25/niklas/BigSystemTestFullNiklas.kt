package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Big Map, many incidents, 2 farms, several ticks, several sowing plans.
 */
class BigSystemTestFullNiklas : ExampleSystemTestExtension() {
    override val name = "BigSystemTestFullNiklas Niklas"
    override val description = "Big Map, many incidents, 2 farms, several ticks, several sowing plans."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsBigSystemTestFullNiklas.json"
    override val scenario = "niklas/scenarioBigSystemTestFullNiklas.json"
    override val map = "niklas/mapBigSystemTestFullNiklas.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = YearTicks.EARLY_MARCH

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 5 within the year.")

        // The Ticks
        earlyMarch()
        lateMarch()
        earlyApril()
        lateApril()
        earlyMay()
        lateMay()
        earlyJune()
        lateJune()
        earlyJuly()
        lateJuly()
        statistics()
    }

    private suspend fun earlyMarch() {
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 5 within the year.")
        assertNextLine(
            "[INFO] " +
                "Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Far" +
                "m: Farm 0 starts its actions."
        )
        assertNextLine(
            "[D" +
                "EBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,3."
        )
        assertNextLine(
            "[IMPORTANT] Fa" +
                "rm: Farm 0 finished its actions."
        )
        assertNextLine(
            "[IMPOR" +
                "TANT] Farm: Farm 1 starts its actions."
        )
        assertNextLine(
            "[DEBU" +
                "G] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 4,5."
        )
        assertNextLine(
            "[IMPORTAN" +
                "T] Farm: Farm 1 finished its actions."
        )
    }

    private suspend fun lateMarch() {
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 6 within the year.")
        assertNextLine(
            "[INFO]" +
                " Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IM" +
                "PORTANT] Farm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[D" +
                "EBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,3."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 5 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 2.")
        assertNextLine(
            "[IMP" +
                "ORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMP" +
                "ORTANT] Farm: Farm 0 finished its actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1" +
                " starts its actions."
        )
        assertNextLine(
            "[DEB" +
                "UG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 4,5."
        )
        assertNextLine(
            "[I" +
                "MPORTANT] Farm: Farm 1 finished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1080000 g of OAT.")
    }

    private suspend fun earlyApril() {
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 7 within the year.")
        assertNextLine(
            "[INFO]" +
                " Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] F" +
                "arm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG]" +
                " Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,3."
        )
        assertNextLine(
            "[IMPORTANT] Farm Action" +
                ": Machine 0 performs WEEDING on tile 5 for 5 days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Machine" +
                ": Machine 0 is finished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished" +
                " its actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1 starts it" +
                "s actions."
        )
        assertNextLine(
            "[DEBUG]" +
                " Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 4,5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 12 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 13 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.")
        assertNextLine(
            "[IMPORTANT]" +
                " Farm Machine: Machine 1 is finished and returns to the shed at 14."
        )
        assertNextLine(
            "[IMPORTANT]" +
                " Farm: Farm 1 finished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 874800 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1080000 g of GRAPE.")
    }

    private suspend fun lateApril() {
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 8 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine(
            "[IMPORTANT] Farm" +
                ": Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] " +
                "Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,3."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 5 for 5 days.")
        assertNextLine(
            "[IMPORTANT] Farm Machine" +
                ": Machine 0 is finished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm" +
                ": Farm 0 finished its actions."
        )
        assertNextLine(
            "[IMPORTANT] " +
                "Farm: Farm 1 starts its actions."
        )
        assertNextLine(
            "[DEBUG] " +
                "Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] " +
                "Farm: Farm 1 finished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 708588 g of OAT.")
    }

    private suspend fun earlyMay() {
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 9 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine(
            "[IMPORTANT] Fa" +
                "rm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,3."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 5 for 5 days.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 0 is finished and returns" +
                " to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished i" +
                "ts actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1 st" +
                "arts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends" +
                " to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] Farm Action: Machine 1 performs WE" +
                "EDING on tile 12 for 5 days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Action: Machine 1 performs W" +
                "EEDING on tile 13 for 5 days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 1 is finished a" +
                "nd returns to the shed at 14."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1 finished its " +
                "actions."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles .")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 720000 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 516560 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 720000 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 900000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 900000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 720000 g of ALMOND.")
    }

    private suspend fun lateMay() {
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 10 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 2 FIELD and 0 PLANTATION tiles.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm " +
                "0 starts its actions."
        )
        assertNextLine(
            "[DEBUG" +
                "] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,3."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 6 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 3.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 0 is fi" +
                "nished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 " +
                "finished its actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1 sta" +
                "rts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 12 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 13 for 5 days.")
        assertNextLine(
            "[IMPORTANT" +
                "] Farm Machine: Machine 1 is finished and returns to the shed at 14."
        )
        assertNextLine(
            "[IMPORTANT" +
                "] Farm: Farm 1 finished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 648000 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 376571 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 450000 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 648000 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 810000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 810000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 648000 g of ALMOND.")
    }

    private suspend fun earlyJune() {
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 11 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 3 PLANTATION tiles.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 start" +
                "s its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 6 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 9 for 5 days.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 0 is fin" +
                "ished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 f" +
                "inished its actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 1 sta" +
                "rts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 12 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 13 for 5 days.")
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 1 is finishe" +
                "d and returns to the shed at 14."
        )
        assertNextLine(
            "[IMPORT" +
                "ANT] Farm: Farm 1 finished its actions."
        )
        assertNextLine(
            "[IMPORT" +
                "ANT] Incident: Incident 3 of type ANIMAL_ATTACK happened and affected tiles 2,6,9,10,12,13."
        )
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING,IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 524835 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 274518 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 202500 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 524880 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 540000 g of GRAPE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 364500 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 364500 g of POTATO.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 15 were not performed: MOWING,IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 524835 g of ALMOND.")
    }

    private suspend fun lateJune() {
        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 12 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 3 PLANTATION tiles.")
        assertNextLine(
            "[I" +
                "MPORTANT] Farm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEB" +
                "UG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 5 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine(
            "[IMPO" +
                "RTANT] Farm: Farm 0 finished its actions."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Fa" +
                "rm 1 starts its actions."
        )
        assertNextLine(
            "[DEBU" +
                "G] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 15 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 14.")
        assertNextLine(
            "[IMPORTANT] Farm: F" +
                "arm 1 finished its actions."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 472351 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 200123 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 164025 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 472392 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 328050 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 328050 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 472351 g of ALMOND.")
    }

    private suspend fun earlyJuly() {
        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 13 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the fol" +
                "lowing active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 5 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 200123 g of OAT harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 200123 g of OAT harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the fol" +
                "lowing active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 12 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 13 for 5 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 14.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 4 of type BROKEN_MACHINE happened and affected tiles 0.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 425115 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 147622 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 425152 g of ALMOND.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: MOWING,IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 485955 g of GRAPE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 295245 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 295245 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 425115 g of ALMOND.")
    }

    private suspend fun lateJuly() {
        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 14 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following acti" +
                "ve sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine(
            "[" +
                "DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine(
            "[" +
                "IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 2,5,6,9,10,12,13,15."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 382603 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 0 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 0 g of GRAPE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 0 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 13 changed to 0 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 0 g of ALMOND.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 10.")
    }

    private suspend fun statistics() {
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 200123 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 200123 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 382603 g."
        )
    }
}
