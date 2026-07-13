
package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val VYXULPON = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val FCOWEGXV = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val ISDYUXPG = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
const val UAZKIMFX = "[IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 3 for 2 days."
const val MRMHHVYG = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."
const val ILDXHRJO = "[IMPORTANT] Farm Action: Machine 3 performs MOWING on tile 1 for 7 days."
const val KJABIRGJ = "[IMPORTANT] Farm Action: Machine 3 performs MOWING on tile 2 for 7 days."
const val MVYTZCHQ = "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 0."
const val XJRIRUVB = "[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 3 for 2 days."
const val EEGNCYUA = "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 1 for 7 days."
const val VVUBFQEF = "[IMPORTANT] Farm Harvest: Machine 3 has collected 874800 g of CHERRY harvest."
const val NYYZWCJE = "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 2 for 7 days."
const val ULLMNIQZ = "[IMPORTANT] Farm Machine: Machine 3 unloads 1749600 g of CHERRY harvest in the shed."
const val OGGYLERY = "[IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 1 for 7 days."
const val KWFYIJZP = "[IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 2 for 7 days."
const val SBRDJDYQ = "[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 3 for 2 days."
const val HBBRPUKA = "[IMPORTANT] Farm Harvest: Machine 0 has collected 472392 g of ALMOND harvest."
const val EJRYNWLF = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 7."
const val SNBRSSEI = "[IMPORTANT] Farm Machine: Machine 0 unloads 472392 g of ALMOND harvest in the shed."

/**
 * Farm system test
 */
class FarmNextShed : CloudSystemExtension() {
    override val name = "Machine return to othershed by id"
    override val description = "Tests if machine does find a new home"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmnextshed.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapnextshed.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 20
    override val startYearTick = 10

    override suspend fun run() {
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(UAZKIMFX)
        assertNextLine(MRMHHVYG)
        assertNextLine(ILDXHRJO)
        assertNextLine(KJABIRGJ)
        assertNextLine(MVYTZCHQ)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(XJRIRUVB)
        assertNextLine(MRMHHVYG)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(EEGNCYUA)
        assertNextLine(VVUBFQEF)
        assertNextLine(NYYZWCJE)
        assertNextLine(VVUBFQEF)
        assertNextLine(MVYTZCHQ)
        assertNextLine(ULLMNIQZ)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(OGGYLERY)
        assertNextLine(KWFYIJZP)
        assertNextLine(MVYTZCHQ)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(XJRIRUVB)
        n()
    }

    suspend fun n() {
        assertNextLine(MRMHHVYG)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(SBRDJDYQ)
        assertNextLine(HBBRPUKA)
        assertNextLine(EJRYNWLF)
        assertNextLine(SNBRSSEI)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(XJRIRUVB)
        assertNextLine(EJRYNWLF)
        assertNextLine(FCOWEGXV)
        skipUntilExcludingCloudCategory()
        assertNextLine(VYXULPON)
        assertNextLine(ISDYUXPG)
        assertNextLine(OGGYLERY)
        assertNextLine(KWFYIJZP)
        assertNextLine(MVYTZCHQ)
        assertNextLine(FCOWEGXV)
    }
}
