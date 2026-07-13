package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Bee happy no effect test
 */
class AnimalAttackSmallTest : SystemTestSELab25() {
    override val name = "AnimalAttackSmall"
    override val description = "tests rather small AA incident"

    override val farms = "tim/incidents/animalAttack/farms.json"
    override val scenario = "tim/incidents/animalAttack/scenario1.json"
    override val map = "tim/incidents/animalAttack/map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilIncidents()
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 1,2,4.")
        // Check Log changes
        // Apple - 10% + -10% for sun
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1377000 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1377000 g of APPLE.")
        // Tile 3 only penalty for too much Sun
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1530000 g of APPLE.")
        // Grape -50%, no sun pen
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 600000 g of GRAPE.")
    }

    private suspend fun skipUntilIncidents() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("finished its actions.")) return
        return skipUntilIncidents()
    }
}
