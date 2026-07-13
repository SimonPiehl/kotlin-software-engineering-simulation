package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val CKHDLPRW = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val SWVJECDT = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val FXQBEZDE = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
const val OCXFRNTC = "[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 1 for 1 days."
const val VWZBPVXY = "[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 2 for 1 days."
const val WFZBCZRS = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."
const val GELQIYEG = "[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 5 for 1 days."
const val CLIVLDJE = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 1 for 1 days."
const val QNHQIYUZ = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 2 for 1 days."
const val RIWTTJCI = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 5 for 1 days."
const val NBPOXJZH = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 8 for 1 days."
const val VZBUJZSW = "[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 1 days."
const val MPOMXDAH = "[IMPORTANT] Farm Harvest: Machine 1 has collected 7881 g of APPLE harvest."
const val BYIBLCNA = "[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 2 for 1 days."
const val WIYCIOQQ = "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 0."
const val QISYUGUX = "[IMPORTANT] Farm Machine: Machine 1 unloads 15762 g of APPLE harvest in the shed."

/**
 * Farm system test
 */
class FarAway : CloudSystemExtension() {
    override val name = "FarAway"
    override val description = "You can only continue two tiles distance"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmfaraway.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapfaraway.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 20
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(OCXFRNTC)
        assertNextLine(VWZBPVXY)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(GELQIYEG)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(CLIVLDJE)
        assertNextLine(QNHQIYUZ)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        n0()
        n1()
    }
    suspend fun n0() {
        assertNextLine(RIWTTJCI)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(NBPOXJZH)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
    }
    suspend fun n1() {
        assertNextLine(CLIVLDJE)
        assertNextLine(QNHQIYUZ)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(RIWTTJCI)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(NBPOXJZH)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(VZBUJZSW)
        assertNextLine(MPOMXDAH)
        assertNextLine(BYIBLCNA)
        assertNextLine(MPOMXDAH)
        assertNextLine(WIYCIOQQ)
        assertNextLine(QISYUGUX)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(CLIVLDJE)
        assertNextLine(QNHQIYUZ)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(RIWTTJCI)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
        skipUntilExcludingCloudCategory()
        assertNextLine(CKHDLPRW)
        assertNextLine(FXQBEZDE)
        assertNextLine(NBPOXJZH)
        assertNextLine(WFZBCZRS)
        assertNextLine(SWVJECDT)
    }
}
