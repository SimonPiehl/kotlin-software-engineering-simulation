package de.unisaarland.cs.se.selab.systemtest.selab25.simon

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * City expansion full test
 *
 * @constructor Create empty City expansion full test
 */
class CityExpansionFullTest : ExampleSystemTestExtension() {
    override val name = "CityExpansionFullTest Simon"
    override val description = "full test"

    override val farms = "simon/CityExpansionHarvestChangeFarm.json"
    override val scenario = "simon/CityExpansionHarvestChangeScenario.json"
    override val map = "simon/CityExpansionHarvestChangeMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: CityExpansionHarvestChangeMap.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: CityExpansionHarvestChangeFarm.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: CityExpansionHarvestChangeScenario.json successfully parsed and validated."
        )
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 1 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 1 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type CITY_EXPANSION happened and affected tiles 3.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1530000 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 2 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles 2.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1377000 g of APPLE.")
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
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1377000 g."
        )
    }
}
