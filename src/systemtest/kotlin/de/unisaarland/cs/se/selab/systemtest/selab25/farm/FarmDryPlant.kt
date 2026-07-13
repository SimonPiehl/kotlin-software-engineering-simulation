
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Farm system test
 */
class FarmDryPlant : CloudSystemExtension() {
    override val name = "FarmDryPlant"
    override val description = "Plant on dry field"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmdryplant.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapdryplant.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapdryplant.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farmdryplant.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 1 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 1 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLAN" +
                "TATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm" +
                ": Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it in" +
                "tends to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] F" +
                "arm: Farm 0 finished its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 2 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLA" +
                "NTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its actio" +
                "ns."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pu" +
                "rsue in this tick: 5."
        )
        n0()
        n()
    }
    suspend fun n() {
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Fa" +
                "rm 0 finished its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 5 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is b" +
                "elow threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 star" +
                "ts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowi" +
                "ng plans it intends to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Fa" +
                "rm 0 finished its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 6 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following ac" +
                "tive sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 2 days.")
        assertNextLine(
            "[IMPORTANT] Farm Sowing: Machine 0 has " +
                "sowed OAT according to sowing plan 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 3 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 0 g of OAT.")
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 7 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the fol" +
                "lowing active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
    suspend fun n0() {
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finished its actio" +
                "ns."
        )
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 3 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below thre" +
                "shold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTA" +
                "NT] Farm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the fol" +
                "lowing active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 finis" +
                "hed its actions."
        )
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 4 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIE" +
                "LD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts its ac" +
                "tions."
        )
    }
}
