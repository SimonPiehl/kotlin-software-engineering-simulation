package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import java.io.File

/**
 * Basic plant type almond test
 *
 * @constructor Create empty Basic plant type almond test
 */
class FullFullTestTest : ExampleSystemTestExtension() {
    override val name = "Tests Full Full Test"
    override val description = "Tests Full Simulation Run"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullFullTests/farms.json"
    override val scenario = "FullFullTests/scenario.json"
    override val map = "FullFullTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 48
    override val startYearTick = 5

    override suspend fun run() {
        val file = File("src/systemtest/resources/FullFullTests/log")
        val lines = file.readLines()

        lines.forEachIndexed { index, line ->
            assertNextLine(line)
        }
    }
}
