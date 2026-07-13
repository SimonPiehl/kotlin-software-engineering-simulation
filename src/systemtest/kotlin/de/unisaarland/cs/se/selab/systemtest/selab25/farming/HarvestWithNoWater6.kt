package de.unisaarland.cs.se.selab.systemtest.selab25.farming

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25
/** Test */
class HarvestWithNoWater6 : SystemTestSELab25() {
    override val name = "HarvestNoWater6"
    override val description = "tests harvest if no water"

    override val farms = "tim/PlantationFarming/noWater/farms.json"
    override val scenario = "tim/PlantationFarming/noWater/scenario.json"
    override val map = "tim/PlantationFarming/noWater/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17 // Early Sep

    // Cherry cannot be harvested after year tick 15
    override suspend fun run() {
        skipUntilHarvesting()
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.")
    }

    private suspend fun skipUntilHarvesting() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.contains("sowing plans")) return
        return skipUntilHarvesting()
    }

    private suspend fun skipUntilStat() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line == "[IMPORTANT] Simulation Info: Simulation statistics are calculated.") return
        return skipUntilStat()
    }
}
