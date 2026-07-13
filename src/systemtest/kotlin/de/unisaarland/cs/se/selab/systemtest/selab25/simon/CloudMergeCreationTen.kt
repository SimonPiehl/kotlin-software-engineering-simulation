package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Cloud merge creation ten
 *
 * @constructor Create empty Cloud merge creation ten
 */
class CloudMergeCreationTen : ExampleSystemTestExtension() {
    override val name = "CloudMergeDurationTen Simon"
    override val description = "tests Cloud Union through Incident new Duration"

    override val farms = "simon/CloudMergeSimpleFarm.json"
    override val scenario = "simon/CloudMergeCreationSimpleScenario.json"
    override val map = "simon/CloudMergeSimpleMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilCloudUnion()
        skipUntilCloudUnion()
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 0 and 1 united to cloud 2 with 6400 L water and duration 10 on tile 1."
        )
    }

    private suspend fun skipUntilCloudUnion() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("tile 0 to tile 1.")) return
        return skipUntilCloudUnion()
    }
}
