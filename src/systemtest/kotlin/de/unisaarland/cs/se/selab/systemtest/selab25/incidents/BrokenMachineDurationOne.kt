package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Double broken machine incident
 */
class BrokenMachineDurationOne : SystemTestSELab25() {
    override val name = "BrokenMachine Duration 1"
    override val description = "Broken Machine Incident with Duration 1"

    override val farms = "tim/smallTests/farm2.json"
    override val scenario = "tim/smallTests/scenario3.json"
    override val map = "CloudSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 9

    override suspend fun run() {
        // TICK 0
        skipUntilIncidentsTick0()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 6.")
        skipUntilFarmNextTick()
        // TICK 1
        assertSowingPlan()
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        skipUntilFarmNextTick()
        // TICK 2
        // Should act bc duration ended
        assertSowingPlan()
        getNextLine()
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
    }

    private suspend fun assertSowingPlan() {
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
    }
    private suspend fun skipUntilIncidentsTick0() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("finished its actions.")) return
        return skipUntilIncidentsTick0()
    }
    private suspend fun skipUntilFarmNextTick() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("starts its action")) return
        return skipUntilFarmNextTick()
    }
}
