package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Example system test
 */
class BrokenMachineFullTest : ExampleSystemTestExtension() {
    override val name = "BrokenMachineFullTest Niklas"
    override val description = "Tests a BROKEN_MACHINE incident. " +
        "The Broken Machine is going to miss HARVESTING of the APPLE plantation. Full log output."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsBrokenMachine.json"
    override val scenario = "niklas/scenarioBrokenMachines.json"
    override val map = "niklas/mapBrokenMachine.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        lateAugust()
        earlySeptember()
        lateSeptember()
        statistics()
    }

    private suspend fun lateAugust() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 16 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 16 within the year.")
        assertNextLine(
            "[INFO] " +
                "Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] " +
                "Farm: Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] " +
                "Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[IMPORTANT] " +
                "Farm: Farm 0 finished its actions."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1115370 g of APPLE.")
    }

    private suspend fun earlySeptember() {
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 731793 g of APPLE.")
    }

    private suspend fun lateSeptember() {
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 18 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 533475 g of APPLE.")
    }

    private suspend fun statistics() {
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
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 533475 g."
        )
    }
}
