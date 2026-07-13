package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Bee happy effect test
 *
 * @constructor Create empty Bee happy effect test
 */
class BeeHappyEffectTest : ExampleSystemTestExtension() {
    override val name = "bee happy effect on Plantation "
    override val description = "test behavior of bee happy with effect on one tile"

    override val farms = "simon/BeeHappyFullEffectFarms.json"
    override val scenario = "simon/BeeHappyFullEffectScenario.json"
    override val map = "simon/BeeHappyFullEffectMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 8

    override suspend fun run() {
        skipUntilIncidents()
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 3.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
