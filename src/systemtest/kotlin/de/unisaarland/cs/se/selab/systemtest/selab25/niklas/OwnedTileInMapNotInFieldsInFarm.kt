package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * A tile has key ids that the farm.json doesnt have in its fields list.
 *
 * @constructor
 */
class OwnedTileInMapNotInFieldsInFarm : ExampleSystemTestExtension() {
    override val name = "OwnedTileInMapNotInFieldsInFarm Niklas"
    override val description = "A tile has key ids that the farm.json doesnt have in its fields list."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapOwnedTileInMapNotInFieldsInFarm.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[INFO] " +
                "Initialization Info: mapOwnedTileInMapNotInFieldsInFarm.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: farms.json is invalid."
        )
    }
}
