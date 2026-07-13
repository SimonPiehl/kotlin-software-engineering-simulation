package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.FarmExtension

const val DFWZMBGR = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val QFHNSFQL = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val LVUYSLKT = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 1 for 1 days."
const val TKPFFNVQ = "[IMPORTANT] Farm Harvest: Machine 6 has collected 1700000 g of APPLE harvest."
const val KAXIBGSB = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 2 for 1 days."
const val AEISJYJX = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 3 for 1 days."
const val NEPKRZNR = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 4 for 1 days."
const val CNEONAPW = "[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0."
const val CUTBCOUM = "[IMPORTANT] Farm Machine: Machine 6 unloads 6800000 g of APPLE harvest in the shed."
const val EBNEPJWC = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 1 for 1 days."
const val PXRKMAUZ = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 2 for 1 days."
const val GIIJMOTM = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 3 for 1 days."
const val GSSLYKKC = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 4 for 1 days."

/**
 * Farm system test
 */
class OtherBlocks : FarmExtension() {
    override val name = "another farms tile blocks"
    override val description = "tests if the machine does not cross tiles of others"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmotherblocks.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapotherblocks.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 5
    override val startYearTick = 17

    override suspend fun run() {
        assertNextLine(DFWZMBGR)
        assertNextLine(LVUYSLKT)
        assertNextLine(TKPFFNVQ)
        assertNextLine(KAXIBGSB)
        assertNextLine(TKPFFNVQ)
        assertNextLine(AEISJYJX)
        assertNextLine(TKPFFNVQ)
        assertNextLine(NEPKRZNR)
        assertNextLine(TKPFFNVQ)
        assertNextLine(CNEONAPW)
        assertNextLine(CUTBCOUM)
        assertNextLine(QFHNSFQL)
        skipUntilFarm()
        assertNextLine(QFHNSFQL)
        skipUntilFarm()
        assertNextLine(QFHNSFQL)
        skipUntilFarm()
        assertNextLine(QFHNSFQL)
        skipUntilFarm()
        assertNextLine(EBNEPJWC)
        assertNextLine(PXRKMAUZ)
        assertNextLine(GIIJMOTM)
        assertNextLine(GSSLYKKC)
        assertNextLine(CNEONAPW)
        assertNextLine(QFHNSFQL)
    }
}
