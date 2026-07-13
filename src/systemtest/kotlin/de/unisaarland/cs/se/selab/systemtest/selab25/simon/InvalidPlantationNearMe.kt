package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
* Invalid neighbour forest village
*
* @constructor Create empty Invalid neighbour forest village
*/
class InvalidPlantationNearMe : ExampleSystemTestExtension() {
    override val name = "InvalidPlantationNearMe Simon"
    override val description = "tests no other field or plantations near Farmstead except owned ones"

    override val farms = "example/farms.json"
    override val scenario = "example/scenario.json"
    override val map = "simon/InvalidPlantationNearMe.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: InvalidPlantationNearMe.json is invalid."
        )
    }
}
