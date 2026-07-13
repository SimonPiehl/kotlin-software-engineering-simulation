package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tests a BEE_HAPPY incident on APPLE PLANTATION with effect 20.
 */
class BeeHappyBiggerNiklas : ExampleSystemTestExtension() {
    override val name = "BeeHappyBiggerNiklas Niklas"
    override val description = "Tests that a BEE_HAPPY incident in a map with multiple MEADOWS " +
        "cannot affect a PLANTATION multiple times."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsBeeHappyBiggerNiklas.json"
    override val scenario = "niklas/scenarioOneBeeHappyCannotAffectMultipleTimes.json"
    override val map = "niklas/mapBeeHappyBiggerNiklas.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_APRIL

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 8 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 8 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] " +
                "Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 1.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1487160 g of APPLE.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
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
            "[IMPORTANT] " +
                "Simulation Statistics: Total harvest estimate still in fields and plantations: 3487160 g."
        )
    }
}
