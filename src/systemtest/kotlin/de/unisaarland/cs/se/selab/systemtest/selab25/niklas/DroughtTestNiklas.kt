package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tests that a DROUGHT incident.
 */
class DroughtTestNiklas : ExampleSystemTestExtension() {
    override val name = "DroughtTestNiklas Niklas"
    override val description = "Tests that a DROUGHT incident."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsDroughtTestNiklas.json"
    override val scenario = "niklas/scenarioDroughtTestNiklas.json"
    override val map = "niklas/mapBeeHappyBiggerNiklas.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = YearTicks.EARLY_AUGUST

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 15 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 15 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1,2,5,6.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of GRAPE.")

        // Tick after DROUGHT
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 16 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Statistics
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
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
