package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * A Farm has elements in the key farmsteads, that belong to another farm.
 *
 * @constructor
 */
class FarmClaimsFarmsteadOfOtherFarm : ExampleSystemTestExtension() {
    override val name = "FarmClaimsFarmsteadOfOtherFarm Niklas"
    override val description = "A Farm has elements in the key farmsteads, that belong to another farm."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsFarmClaimsFarmsteadOfOtherFarm.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapFarmClaimsFieldOfOtherFarm.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: mapFarmClaimsFieldOfOtherFarm.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: farmsFarmClaimsFarmsteadOfOtherFarm.json is invalid."
        )
    }
}
