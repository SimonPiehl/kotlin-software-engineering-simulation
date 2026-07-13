package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Invalid neighbour forest village
 *
 * @constructor Create empty Invalid neighbour forest village
 */
class InvalidNeighbourForestVillage : ExampleSystemTestExtension() {
    override val name = "InvalidNeighbourForestVillage Simon"
    override val description = "tests Forest not near Village"

    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "simon/InvalidNeighbourForestVillage.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: InvalidNeighbourForestVillage.json is invalid."
        )
    }
}
