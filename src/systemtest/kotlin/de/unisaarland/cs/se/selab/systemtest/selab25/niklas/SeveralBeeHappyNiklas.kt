package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tests several BEE_HAPPY incident on APPLE PLANTATION with effect 20 then 10 then 5.
 */
class SeveralBeeHappyNiklas : ExampleSystemTestExtension() {
    override val name = "SeveralBeeHappyNiklas Niklas"
    override val description = "Tests several BEE_HAPPY incident on APPLE PLANTATION with effect 20 then 10 then 5."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/scenarioSeveralBeeHappyNiklas.json"
    override val map = "niklas/mapBeeHappyIncidentTestNiklas.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_APRIL

    override suspend fun run() {
        // Testing
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
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles 1.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1717669 g of APPLE.")
    }
}
