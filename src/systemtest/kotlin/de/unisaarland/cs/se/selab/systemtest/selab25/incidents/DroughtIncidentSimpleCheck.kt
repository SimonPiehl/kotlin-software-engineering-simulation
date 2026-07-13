package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Simple Drought Incident Check
 *
 * @constructor
 */
class DroughtIncidentSimpleCheck : ExampleSystemTestExtension() {
    override val name = "drought on 3 tiles"
    override val description = "testing for correct format"

    override val farms = "simon/BeeHappyFullEffectFarms.json"
    override val scenario = "simon/DroughtOnFieldsAndPlantationsScenario.json"
    override val map = "simon/BeeHappyFullEffectMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 3,4,5.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
