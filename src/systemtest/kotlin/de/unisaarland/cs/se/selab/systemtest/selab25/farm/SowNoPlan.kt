
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val FHMGMUIB = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val PRPFMBLB = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val MHXEPILC = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5."
const val VQZJGOQV = "[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 6 days."
const val WYCMJJTJ = "[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 5."
const val DUOCHHGU = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."

/**
 * Farm system test
 */
class SowNoPlan : CloudSystemExtension() {
    override val name = "SowNoPlan"
    override val description = "Sowing on possible field without sowing plan"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmsownoplan.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapsownoplan.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        assertNextLine(FHMGMUIB)
        assertNextLine(MHXEPILC)
        assertNextLine(VQZJGOQV)
        assertNextLine(WYCMJJTJ)
        assertNextLine(DUOCHHGU)
        assertNextLine(PRPFMBLB)
    }
}
