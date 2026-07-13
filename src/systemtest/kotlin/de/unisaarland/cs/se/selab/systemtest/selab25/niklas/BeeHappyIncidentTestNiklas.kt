package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tests a BEE_HAPPY incident on APPLE PLANTATION with effect 20.
 */
class BeeHappyIncidentTestNiklas : ExampleSystemTestExtension() {
    override val name = "BeeHappyIncidentTestNiklas Niklas"
    override val description = "Tests a BEE_HAPPY incident on APPLE PLANTATION with effect 20."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/scenarioBeeHappyIncidentTestNiklas.json"
    override val map = "niklas/mapBeeHappyIncidentTestNiklas.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = YearTicks.LATE_APRIL

    override suspend fun run() {
        lateApril()
        earlyMay()
        lateMay()
        statistics()
    }

    private suspend fun lateApril() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 8 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 8 within the year.")
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
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 1.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1487160 g of APPLE.")
    }

    private suspend fun earlyMay() {
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 9 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 975725 g of APPLE.")
    }

    private suspend fun lateMay() {
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 10 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 640171 g of APPLE.")
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
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 640171 g."
        )
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
