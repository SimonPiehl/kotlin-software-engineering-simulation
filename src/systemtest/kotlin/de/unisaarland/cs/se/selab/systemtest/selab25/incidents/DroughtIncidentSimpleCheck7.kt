package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simple Drought Incident Check
 *
 * @constructor
 */
class DroughtIncidentSimpleCheck7 : ExampleSystemTestExtension() {
    override val name = "WhenDroughtFails5"
    override val description = "Tests if drought fails " +
        "[INFO] Simulation Info: Tick 1 started at tick 2 within the year."

    override val farms = "simon/BeeHappyFullEffectFarms.json"
    override val scenario = "simon/DroughtOnFieldsAndPlantationsScenario.json"
    override val map = "simon/BeeHappyFullEffectMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 3,4,5.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 0 g of CHERRY.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 2 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
