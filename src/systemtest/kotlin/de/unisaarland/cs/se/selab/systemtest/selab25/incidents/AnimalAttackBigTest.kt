package de.unisaarland.cs.se.selab.systemtest.selab25.incidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Bee happy no effect test
 */
class AnimalAttackBigTest : SystemTestSELab25() {
    override val name = "AnimalAttackBig"
    override val description = "tests big AA incident, also includes sowing"

    override val farms = "tim/incidents/animalAttack/farms2.json"
    override val scenario = "tim/incidents/animalAttack/scenario2.json"
    override val map = "tim/incidents/animalAttack/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 11

    override suspend fun run() {
        skipUntilFarmAction()
        // Logging of PlantSowing
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 9 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 10 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed PUMPKIN according to sowing plan 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        // Incident Part
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 5,6,9.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 6,9.")
        // Estimation Part
        // Tile 5: -10% for sun + -10% for AnimalAttack
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 972000 g of CHERRY.")
        // Tile 6: -10% for sun + -10% for AnimalAttack x2
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 583200 g of ALMOND.")
        // Tile 7: -10% for sun + -10% for Mowing
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 648000 g of ALMOND.")
        // Tile 9: -10% for sun + -50% for AnimalAttack 2x
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 112500 g of PUMPKIN.")
        // Tile 10: -10% for sun
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 450000 g of PUMPKIN.")
    }

    private suspend fun skipUntilFarmAction() {
        val line = getNextLine() ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        if (line.endsWith("starts its actions.")) return
        return skipUntilFarmAction()
    }
}
