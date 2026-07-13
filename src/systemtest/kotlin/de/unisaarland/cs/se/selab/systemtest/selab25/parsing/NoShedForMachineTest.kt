package de.unisaarland.cs.se.selab.systemtest.selab25.parsing

import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/** No Shed */
class NoShedForMachineTest : SystemTestSELab25() {
    override val name = "No Shed For Machine Test"
    override val description = "test parsing of no shed at farmsteads for machine"

    override val farms = "tim/farmParsing/NoShedForMachine/farms.json"
    override val scenario = "tim/farmParsing/NoShedForMachine/scenario.json"
    override val map = "tim/farmParsing/NoShedForMachine/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farms.json is invalid.")
    }
}
