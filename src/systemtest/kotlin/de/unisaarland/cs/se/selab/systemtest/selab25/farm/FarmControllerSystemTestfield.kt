package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension
const val START = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val NOSO = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
const val EN = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val HO = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."

/**
 * Farm system test
 */
class FarmControllerSystemTestfield : CloudSystemExtension() {
    override val name = "Farm simple field"
    override val description = "Tests simple apple field"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmfield.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapfield.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 21
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        assertNextLine(START)
        assertNextLine(
            NOSO
        )
        assertNextLine(EN)
        skipUntilExcludingCloudCategory()
        assertNextLine(START)
        assertNextLine(
            NOSO
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 6 days.")
        assertNextLine(HO)
        assertNextLine(EN)
        skipUntilExcludingCloudCategory()
        assertNextLine(START)
        assertNextLine(
            NOSO
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 6 days.")
        assertNextLine(HO)
        assertNextLine(EN)
        skipUntilExcludingCloudCategory()
        assertNextLine(START)
        assertNextLine(
            NOSO
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 6 days.")
        assertNextLine(HO)
        assertNextLine(EN)
        skipUntilExcludingCloudCategory()
        assertNextLine(START)
        assertNextLine(
            NOSO
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 6 days.")
        assertNextLine(HO)
        assertNextLine(EN)
        skipUntilExcludingCloudCategory()
    }
}
