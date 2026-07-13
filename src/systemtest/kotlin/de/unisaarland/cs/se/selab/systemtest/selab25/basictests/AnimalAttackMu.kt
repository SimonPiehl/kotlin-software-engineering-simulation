package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Basic plant type almond test
 *
 * @constructor Create empty Basic plant type almond test
 */
class AnimalAttackMu : ExampleSystemTestExtension() {
    override val name = "AnimalAttackMu"
    override val description = "Tests if WuffWuff mows"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mathis/farms2.json"
    override val scenario = "mathis/scenario2.json"
    override val map = "mathis/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 16

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map2.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms2.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario2.json successfully parsed and validated.")
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 16 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 16 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 0.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 1003833 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 731793 g of APPLE.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 2.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 731793 g."
        )
    }
}
