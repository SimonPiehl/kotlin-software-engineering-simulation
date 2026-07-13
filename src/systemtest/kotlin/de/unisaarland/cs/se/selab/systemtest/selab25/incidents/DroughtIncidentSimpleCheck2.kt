package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simple Drought Incident Check
 *
 * @constructor
 */
class DroughtIncidentSimpleCheck2 : ExampleSystemTestExtension() {
    override val name = "WhenDroughtFails2"
    override val description = "Tests if drought fails " +
        "[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING."

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
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
