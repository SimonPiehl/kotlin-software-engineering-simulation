package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Invalid neighbour farmstead meadow
 *
 * @constructor Create empty Invalid neighbour farmstead meadow
 */
class InvalidNeighbourFarmsteadMeadow : ExampleSystemTestExtension() {
    override val name = "InvalidNeighbourFarmsteadMeadow Simon"
    override val description = "tests Meadow not near Farmstead"

    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "simon/InvalidNeighbourFarmsteadMeadow.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: InvalidNeighbourFarmsteadMeadow.json is invalid."
        )
    }
}
