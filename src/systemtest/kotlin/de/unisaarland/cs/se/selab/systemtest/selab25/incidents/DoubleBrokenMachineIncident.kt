package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

// import de.unisaarland.cs.se.selab.main
import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Double broken machine incident
 */
class DoubleBrokenMachineIncident : SystemTestSELab25() {
    override val name = "Two BrokenMachine Incident"
    override val description = "2 Broken Machine Incidents Overriding on same machine"

    override val farms = "tim/smallTests/farm2.json"
    override val scenario = "tim/smallTests/scenario2.json"
    override val map = "CloudSystemTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 8

    override suspend fun run() {
        // TICK 0
        skipUntilIncidentsTick0()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 6.")
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BROKEN_MACHINE happened and affected tiles 6.")
        skipUntilFarmNextTick()
        // TICK 1
        // Should do nothing bc No machine for sowing plan
        assertSowingPlan()
        assertFarmFinished()
        skipUntilFarmNextTick()
        // TICK 2
        // Should do nothing bc No machine for sowing plan
        assertSowingPlan()
        assertFarmFinished()
        skipUntilFarmNextTick()
        // TICK 3
        assertSowingPlan()
        getNextLine() // TODO
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
    }

    private suspend fun assertSowingPlan() {
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
    }
    private suspend fun assertFarmFinished() {
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
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
