
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val MSPLVGTG = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val RXDFOJVK = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val ILIRHSUE = "[IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 3 for 2 days."
const val TCKDCPYC = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."
const val ZXPCMNCD = "[IMPORTANT] Farm Action: Machine 6 performs MOWING on tile 6 for 6 days."
const val SBWDPFZW = "[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0."
const val XSATMPSG = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 2 days."
const val MAJPNZXX = "[IMPORTANT] Farm Action: Machine 6 performs IRRIGATING on tile 6 for 6 days."
const val QOMNQLSW = "[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 3 for 2 days."
const val TSKHCWEP = "[IMPORTANT] Farm Harvest: Machine 0 has collected 472392 g of ALMOND harvest."
const val UJHKSBYX = "[IMPORTANT] Farm Machine: Machine 0 unloads 472392 g of ALMOND harvest in the shed."
const val IEYFMVTM = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 6 for 6 days."
const val LFPKNZWC = "[IMPORTANT] Farm Harvest: Machine 6 has collected 88965 g of APPLE harvest."
const val UFMECGLC = "[IMPORTANT] Farm Machine: Machine 6 unloads 88965 g of APPLE harvest in the shed."

/**
 * Farm system test
 */
class NoForest : CloudSystemExtension() {
    override val name = "Can Machines pass forests"
    override val description = "Tests if machines can go through forests"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmnoforest.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapnoforest.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 10
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine(MSPLVGTG)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(ILIRHSUE)
        assertNextLine(TCKDCPYC)
        assertNextLine(ZXPCMNCD)
        assertNextLine(SBWDPFZW)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(XSATMPSG)
        assertNextLine(TCKDCPYC)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(XSATMPSG)
        assertNextLine(TCKDCPYC)
        assertNextLine(MAJPNZXX)
        assertNextLine(SBWDPFZW)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(QOMNQLSW)
        assertNextLine(TSKHCWEP)
        assertNextLine(TCKDCPYC)
        assertNextLine(UJHKSBYX)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(IEYFMVTM)
        assertNextLine(LFPKNZWC)
        assertNextLine(SBWDPFZW)
        assertNextLine(UFMECGLC)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(XSATMPSG)
        assertNextLine(TCKDCPYC)
        assertNextLine(RXDFOJVK)
        assertNextLine(MSPLVGTG)
        assertNextLine(RXDFOJVK)
    }
}
