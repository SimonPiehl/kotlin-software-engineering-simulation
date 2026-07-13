package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simple Drought Incident Check
 *
 * @constructor
 */
class DroughtIncidentSimpleCheck3 : ExampleSystemTestExtension() {
    override val name = "WhenDroughtFails3"
    override val description = "Tests if drought fails " +
        "[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 0 g of CHERRY."

    override val farms = "simon/BeeHappyFullEffectFarms.json"
    override val scenario = "simon/DroughtOnFieldsAndPlantationsScenario.json"
    override val map = "simon/BeeHappyFullEffectMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 3,4,5.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 0 g of CHERRY.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
