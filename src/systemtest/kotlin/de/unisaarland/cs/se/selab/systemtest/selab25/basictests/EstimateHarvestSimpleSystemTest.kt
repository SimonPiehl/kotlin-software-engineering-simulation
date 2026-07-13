package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.EstimateHarvestSystemExtension

/**
 * Json files
 *
 * @property farms
 * @property scenario
 * @property map
 * @constructor Create empty Json files
 */
data class JsonFiles(val farms: String, val scenario: String, val map: String, val logLevel: String = "DEBUG")

fun exampleJsonFiles() = JsonFiles(
    farms = "example/farms.json",
    scenario = "example/scenario.json",
    map = "example/map.json"
)

fun twofieldsJsonFiles() = JsonFiles(
    farms = "EstimateHarvestExample/farms.json",
    scenario = "EstimateHarvestExample/scenario.json",
    map = "EstimateHarvestExample/map.json"
)

fun twofieldslatesowingJsonFiles() = JsonFiles(
    farms = "EstimateHarvestExample/farmLateSowing.json",
    scenario = "EstimateHarvestExample/scenario.json",
    map = "EstimateHarvestExample/map.json"
)

/**
 * Estimate harvest simple system test
 *
 * @constructor Create empty Estimate harvest simple system test
 */
class EstimateHarvestSimpleSystemTest : CloudSystemExtension() {
    override val name = "EstimateHarvestTestEstimationoneroundwithApples"
    override val description = "Tests a round in oktober"

    val files = exampleJsonFiles()
    override val farms = files.farms
    override val scenario = files.scenario
    override val map = files.map
    override val logLevel = files.logLevel

    override val maxTicks = 2
    override val startYearTick = 18

    override suspend fun run() {
        skipUntilExcludingIncidentCategory()
        skipUntilExcludingIncidentCategory()
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: HARVESTING."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 501916 g of APPLE."
        )
    }
}

/**
 * Estimate harvest simple system test first round simulation
 *
 * @constructor Create empty Estimate harvest simple system test first round simulation
 */
class EstimateHarvestSimpleSystemTestFirstRoundSimulation : EstimateHarvestSystemExtension() {
    override val name = "EstimateHarvestTestfirstroundEstimation"
    override val description = "Tests the first Round Estimation and apple is reseted"

    val files = exampleJsonFiles()
    override val farms = files.farms
    override val scenario = files.scenario
    override val map = files.map
    override val logLevel = files.logLevel

    override val maxTicks = 0
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilSimulationStatistics()

        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 850000 g."
        )
    }
}

/**
 * Estimate harvest simple system test cutting missed apple
 *
 * @constructor Create empty Estimate harvest simple system test cutting missed apple
 */
class EstimateHarvestSimpleSystemTestCuttingMissedApple : CloudSystemExtension() {
    override val name = "EstimateHarvestSimpleSystemTestCuttingMissedApple"
    override val description = "Teststwo Round Estimation and apple is not cutted"

    val files = exampleJsonFiles()
    override val farms = files.farms
    override val scenario = files.scenario
    override val map = files.map
    override val logLevel = files.logLevel

    override val maxTicks = 2
    override val startYearTick = 3

    override suspend fun run() {
        skipUntilExcludingIncidentCategory()
        skipUntilExcludingIncidentCategory()

        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: CUTTING."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 557685 g of APPLE."
        )
    }
}

/**
 * Estimate harvest simple system test one year
 *
 * @constructor Create empty Estimate harvest simple system test one year
 */
class EstimateHarvestSimpleSystemTestOneYear : EstimateHarvestSystemExtension() {
    override val name = "EstimateHarvestSimpleSystemOneYear"
    override val description = "WholeYear is done"

    val files = twofieldsJsonFiles()
    override val farms = files.farms
    override val scenario = files.scenario
    override val map = files.map
    override val logLevel = files.logLevel

    override val maxTicks = 46
    override val startYearTick = 1

    override suspend fun run() {
        var i = 0
        while (i < 14) {
            skipUntilExcludingIncidentCategory()
            i = i + 1
        }
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: WEEDING."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 478296 g of POTATO."
        )

        skipUntilSimulationStatisticsfirst()
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Farm 0 collected 2878296 g of harvest."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 478296 g."
        )

        skipUntilSimulationStatistics()
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1200000 g."
        )
    }
}

/**
 * Estimate harvest simple system test late sowing
 *
 * @constructor Create empty Estimate harvest simple system test late sowing
 */
class EstimateHarvestSimpleSystemTestLateSowingMoreRounds : EstimateHarvestSystemExtension() {
    override val name = "EstimateHarvestSimpleSystemMoreRounds"
    override val description = "Tests one Year Estimation and latesowing"

    val files = twofieldslatesowingJsonFiles()
    override val farms = "EstimateHarvestExample/farmLateSowing.json"
    override val scenario = files.scenario
    override val map = "EstimateHarvestExample/mapbig.json"
    override val logLevel = files.logLevel

    override val maxTicks = 70
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilSowingPlan2()

        assertNextLine(
            "[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 4 for 10 days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 2."
        )
        assertNextLine(
            "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 " +
                "finished its actions."
        )
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1080000 g of GRAPE."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 777600 g of OAT."
        )
        assertNextLine(
            "[INFO] Simulation Info: Tick 7 started at tick 8 within the year."
        )
        skipUntilSimulationStatisticsfirst()
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Farm 0 collected 4169974 g of harvest."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 898200 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 355774 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 2916000 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g."
        )
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2000000 g."
        )
    }

    /**
     * Estimate harvest simple system w h e a t
     *
     * @constructor Create empty Estimate harvest simple system w h e a t
     */
    class EstimateHarvestSimpleSystemMutants : EstimateHarvestSystemExtension() {
        override val name = "EstimateHarvestSimpleSystemMutants"
        override val description = "Tests one Year Estimation and latesowing"

        override val farms = "src/systemtest/resources/tim/fieldFarming/wheat/farms.json"
        override val scenario = "src/systemtest/resources/tim/fieldFarming/wheat/scenario.json"
        override val map = "src/systemtest/resources/tim/fieldFarming/wheat/map.json"
        override val logLevel = "DEBUG"

        override val maxTicks = 24
        override val startYearTick = 1

        override suspend fun run() {
            assertNextLine("[INFO] Initialization Info: map2.json successfully parsed and validated.")
            assertNextLine("[INFO] Initialization Info: farms2.json successfully parsed and validated.")
            assertNextLine("[INFO] Initialization Info: scenario2.json successfully parsed and validated.")
            assertNextLine("[INFO] Simulation Info: Simulation started at tick 16 within the year.")
            assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 16 within the year.")
            assertNextLine(
                "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION" +
                    " tiles."
            )
            assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
            assertNextLine(
                "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to " +
                    "pursue in this tick: ."
            )
            assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
            assertNextLine("[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 0.")
            assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 1003833 g of APPLE.")
            assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 17 within the year.")
            assertNextLine(
                "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 " +
                    "PLANTATION tiles."
            )
            assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
            assertNextLine(
                "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to" +
                    " pursue in this tick: ."
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
}
