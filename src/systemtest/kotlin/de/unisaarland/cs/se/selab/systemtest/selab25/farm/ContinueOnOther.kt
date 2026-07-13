package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

/**
 * Farm system test
 */
class ContinueOnOther : CloudSystemExtension() {
    override val name = "ContinueOnOther"
    override val description = "Continue on other kinds"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmcontinueother.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapcontinueother.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 20
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: mapcontinueother.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: farmcontinueother.json is invalid.")
    }
}
