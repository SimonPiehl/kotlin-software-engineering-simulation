package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * A Farm has elements in the key fields, that don't exist in the map
 *
 * @constructor
 */
class FarmsteadHasOtherFarmsFieldAsNeighbour : ExampleSystemTestExtension() {
    override val name = "FarmsteadHasOtherFarmsFieldAsNeighbour Niklas"
    override val description = "The FARMSTEAD of one farm has a FIELD of another farm as neighbour"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "niklas/farmsFarmsteadHasOtherFarmsFieldAsNeighbour.json"
    override val scenario = "example/scenario.json"
    override val map = "niklas/mapFarmsteadHasOtherFarmsFieldAsNeighbour.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.LATE_AUGUST

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: mapFarmsteadHasOtherFarmsFieldAsNeighbour.json is invalid."
        )
    }
}
