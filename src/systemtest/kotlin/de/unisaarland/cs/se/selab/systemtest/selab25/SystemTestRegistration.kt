package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.AnimalAttackMu
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BasicPlantTypeAlmondTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BasicPlantTypeAlmondTest2
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.BasicPlantTypeAlmondTest3
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.CloudCreationsIntersectInvalidScenario
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EstimateHarvestSimpleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EstimateHarvestSimpleSystemTestCuttingMissedApple
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EstimateHarvestSimpleSystemTestFirstRoundSimulation
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EstimateHarvestSimpleSystemTestLateSowingMoreRounds
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.EstimateHarvestSimpleSystemTestOneYear
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FarmNameCanBeEmpty
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FieldIsSquareInvalidSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.FullFullTestTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.PlantationIsSquareInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanInvalidBecauseCityExpansion
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.SowingPlanInvalidBecauseCityExpansionTwo
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.TestingSimulationSteps
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.TwoCloudsLocationOnSameTileInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudAtVillageSimStart
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudNoRainJustDissipate
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudRainingAllDownTest
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudSimpleMergeDuration
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudSimpleMergeDurationInfOne
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudSimpleMergeDurationInfTwo
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.CloudSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.IncidentCloudSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.clouds.ResetSunAfterTickTest
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.ContinueOnOther
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarAway
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmControllerSystemTestfield
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmDryPlant
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmFallow
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmNextShed
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmOtherShed
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmSimpleSow
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmSowOrder
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmTravel
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmdrought
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmeveryfield
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmeveryplantationOne
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmeveryplantationThree
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.FarmeveryplantationTwo
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmjump
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmsowone
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmsowthree
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.Farmsowtwo
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.MachineChoice
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.NoForest
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.NoTimeLeft
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.OtherBlocks
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.SowNoPlan
import de.unisaarland.cs.se.selab.systemtest.selab25.farm.StuckCity
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.CherryHarvestStuckTest
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.CherryTooLateTest
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.HarvestWithNoWater2
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.IrrigationChaos
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.NotMowedApple
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.NovemberReset
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.NovemberResetAccurate
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.SowingWheatTest
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.WheatCycle
import de.unisaarland.cs.se.selab.systemtest.selab25.farming.WheatCycleExtra
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.AnimalAttackBigTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.AnimalAttackSmallTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.BeeHappy2Times
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.BeeHappyEffectTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.BeeHappyNoEffectTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.BrokenMachineDurationOne
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.CityExpansionOnCloud
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.CloudCreationDurationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.DoubleBrokenMachineIncident
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.DroughtIncidentSimpleCheck
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.DroughtIncidentSimpleCheck5
import de.unisaarland.cs.se.selab.systemtest.selab25.incidents.DroughtIncidentSimpleCheck7
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.BeeHappyBiggerNiklas
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.BeeHappyIncidentTestNiklas
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.BigSystemTestFullNiklas
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.BrokenMachineFullTest
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.CityExpansionBesidesCityExpansion
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.CityExpansionBesidesForest
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.CityExpansionBesidesForestButAfterMaxTick
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.CityExpansionNotBesidesVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.DroughtTestNiklas
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmClaimsFarmsteadOfOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmClaimsFieldOfOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmClaimsPlantationOfOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmHasNonExistingFarmsteads
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmHasNonExistingFields
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmHasNonExistingPlantations
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FarmsteadHasOtherFarmsFieldAsNeighbour
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.FieldHasNoFarmOwner
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.HasToLieFallow
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.OneBeeHappyCannotAffectMultipleTimes
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.OwnedTileInMapNotInFieldsInFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.PlantationHasNoOwner
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.SeveralBeeHappyNiklas
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.SmallBrokenMachine
import de.unisaarland.cs.se.selab.systemtest.selab25.niklas.TestBrokenMachine
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CityExpansionBeeHappy
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CityExpansionMakesAnotherValid
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CityExpansionMakesAnotherValidNot
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CityExpansionMakesAnotherValidTwo
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CloudCreationMixedTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CloudCreationOnVillageTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.CloudCreationOverlapOnVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.FarmWithNoShedTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.FarmWithNonExistingFieldTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.FieldOwnerNotExistsTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.NoGrowableExistsTest
import de.unisaarland.cs.se.selab.systemtest.selab25.parsing.NoShedForMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.CityExpansionFullTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.CloudMergeCreationTen
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.CloudMergeDurationMinusOne
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.CloudMovementSteps
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.DroughtMoistureReductionSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.InvalidNeighbourFarmsteadMeadow
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.InvalidNeighbourForestVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.InvalidPlantationNearMe
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.SowingPlanSimpleFullTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simon.TwoMergedCloudsFullTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests.SimulationEndSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests.SimulationStartSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.simpleSystemTests.SoilMoistureSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest10
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest11
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest12
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest2
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest3
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest4
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest5
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest6
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest7
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest8
import de.unisaarland.cs.se.selab.systemtest.selab25.statistics.StatisticTest9

/**
 * Used for test registration
 */
object SystemTestRegistration {
    /**
     * Register your tests to run against the reference implementation!
     * This can also be used to debug our system test, or to see if we
     * understood something correctly or not (everything should work
     * the same as their reference implementation)
     */
    fun registerSystemTestsForReferenceImplementation(testSuite: SELab25TestSuite) {
        // Sys tests that passed the reference the last time
        callSubFunctions(testSuite)
        // failingSysTests(testSuite)
        // New
        testSuite.registerTest(FullFullTestTest())
        testSuite.registerTest(AnimalAttackMu())
        testSuite.registerTest(AnimalAttackBigTest())
        testSuite.registerTest(BigSystemTestFullNiklas())
        testSuite.registerTest(CityExpansionMakesAnotherValidTwo())
        // Real New
        testSuite.registerTest(HarvestWithNoWater2())
    }
    fun callSubFunctions(testSuite: SELab25TestSuite) {
        // Parsing
        mapParsing(testSuite)
        farmParsing(testSuite)
        scenarioParsing(testSuite)
        // General
        simulationRun(testSuite)
        soilAndSunnyTests(testSuite)
        // Sim
        cloudTests(testSuite)
        harvestEstimation(testSuite)
        testPlantTypes(testSuite)
        incidentsTests(testSuite)
        harvestingSysTests(testSuite)
        farmSysTests(testSuite)
        sowingSysTests(testSuite)
        statisticTests(testSuite)
    }

    fun mapParsing(testSuite: SELab25TestSuite) {
        // Simon
        testSuite.registerTest(InvalidPlantationNearMe())
        testSuite.registerTest(InvalidNeighbourForestVillage())
        testSuite.registerTest(InvalidNeighbourFarmsteadMeadow())
        testSuite.registerTest(CityExpansionFullTest())
        // Niklas
        testSuite.registerTest(FieldIsSquareInvalidSystemTest())
        testSuite.registerTest(PlantationIsSquareInvalid())
        testSuite.registerTest(FieldHasNoFarmOwner())
        testSuite.registerTest(PlantationHasNoOwner())
        testSuite.registerTest(FarmsteadHasOtherFarmsFieldAsNeighbour())
    }
    fun farmParsing(testSuite: SELab25TestSuite) {
        // Tim
        testSuite.registerTest(FarmWithNonExistingFieldTest())
        testSuite.registerTest(FarmWithNoShedTest())
        testSuite.registerTest(FieldOwnerNotExistsTest())
        testSuite.registerTest(NoShedForMachineTest())
        testSuite.registerTest(NoGrowableExistsTest())
        testSuite.registerTest(FarmNameCanBeEmpty())
        testSuite.registerTest(FarmHasNonExistingFields())
        testSuite.registerTest(FarmHasNonExistingPlantations())
        testSuite.registerTest(OwnedTileInMapNotInFieldsInFarm())
        testSuite.registerTest(FarmHasNonExistingFarmsteads())
        testSuite.registerTest(FarmClaimsFieldOfOtherFarm())
        testSuite.registerTest(FarmClaimsPlantationOfOtherFarm())
        testSuite.registerTest(FarmClaimsFarmsteadOfOtherFarm())
    }
    fun scenarioParsing(testSuite: SELab25TestSuite) {
        // Tim
        testSuite.registerTest(CloudAtVillageSimStart())
        testSuite.registerTest(CloudCreationOnVillageTest())
        testSuite.registerTest(CloudCreationMixedTest())
        testSuite.registerTest(CloudCreationOverlapOnVillage())
        testSuite.registerTest(CityExpansionMakesAnotherValid())
        testSuite.registerTest(CityExpansionMakesAnotherValidNot())
        testSuite.registerTest(BeeHappyNoEffectTest())
        // Niklas
        testSuite.registerTest(CloudCreationsIntersectInvalidScenario())
        testSuite.registerTest(SowingPlanInvalidBecauseCityExpansion())
        testSuite.registerTest(SowingPlanInvalidBecauseCityExpansionTwo())
        testSuite.registerTest(TwoCloudsLocationOnSameTileInvalid())
        testSuite.registerTest(CityExpansionNotBesidesVillage())
        testSuite.registerTest(CityExpansionBesidesForest())
        testSuite.registerTest(CityExpansionBesidesCityExpansion())
        testSuite.registerTest(CityExpansionBesidesForestButAfterMaxTick())
    }
    fun simulationRun(testSuite: SELab25TestSuite) {
        // Tim
        testSuite.registerTest(SimulationStartSystemTest())
        testSuite.registerTest(SimulationEndSystemTest())
        // Simon
        testSuite.registerTest(TestingSimulationSteps())
    }
    fun soilAndSunnyTests(testSuite: SELab25TestSuite) {
        // Tim
        testSuite.registerTest(SoilMoistureSystemTest())
        testSuite.registerTest(ResetSunAfterTickTest())
        // Niklas
        testSuite.registerTest(HasToLieFallow())
    }
    fun cloudTests(testSuite: SELab25TestSuite) {
        // Tim
        testSuite.registerTest(CloudSystemTest())
        testSuite.registerTest(IncidentCloudSystemTest()) // also Incident
        testSuite.registerTest(CloudRainingAllDownTest())
        testSuite.registerTest(CloudNoRainJustDissipate())
        // Simon
        testSuite.registerTest(CloudMovementSteps())
        testSuite.registerTest(CloudSimpleMergeDuration())
        testSuite.registerTest(CloudSimpleMergeDurationInfOne())
        testSuite.registerTest(CloudSimpleMergeDurationInfTwo())
        testSuite.registerTest(CloudMergeDurationMinusOne())
        testSuite.registerTest(CloudMergeCreationTen())
        testSuite.registerTest(TwoMergedCloudsFullTest())
    }
    fun harvestEstimation(testSuite: SELab25TestSuite) {
        // Harvest Estimation
        testSuite.registerTest(EstimateHarvestSimpleSystemTest()) // T ?
        testSuite.registerTest(EstimateHarvestSimpleSystemTestFirstRoundSimulation()) // T ?
        testSuite.registerTest(EstimateHarvestSimpleSystemTestCuttingMissedApple())
        testSuite.registerTest(EstimateHarvestSimpleSystemTestOneYear())
        testSuite.registerTest(EstimateHarvestSimpleSystemTestLateSowingMoreRounds())
    }
    fun testPlantTypes(testSuite: SELab25TestSuite) {
        // Mathis
        testSuite.registerTest(BasicPlantTypeAlmondTest())
        testSuite.registerTest(BasicPlantTypeAlmondTest2())
        testSuite.registerTest(BasicPlantTypeAlmondTest3())
    }
    fun incidentsTests(testSuite: SELab25TestSuite) {
        // Niklas
        testSuite.registerTest(TestBrokenMachine())
        testSuite.registerTest(BrokenMachineFullTest())
        testSuite.registerTest(SmallBrokenMachine())
        testSuite.registerTest(BeeHappyIncidentTestNiklas())
        testSuite.registerTest(SeveralBeeHappyNiklas())
        testSuite.registerTest(OneBeeHappyCannotAffectMultipleTimes())
        testSuite.registerTest(BeeHappyBiggerNiklas())
        testSuite.registerTest(DroughtTestNiklas())
        // Tim
        testSuite.registerTest(AnimalAttackSmallTest())
        testSuite.registerTest(DoubleBrokenMachineIncident())
        testSuite.registerTest(BrokenMachineDurationOne())
        testSuite.registerTest(CityExpansionOnCloud())
        testSuite.registerTest(CloudCreationDurationTest())
        testSuite.registerTest(CityExpansionBeeHappy())
        testSuite.registerTest(BeeHappyEffectTest())
        testSuite.registerTest(BeeHappy2Times())
        // Matthis
        testSuite.registerTest(DroughtIncidentSimpleCheck7())
        testSuite.registerTest(DroughtIncidentSimpleCheck())
        testSuite.registerTest(DroughtIncidentSimpleCheck5())
        // Simon
        testSuite.registerTest(DroughtMoistureReductionSystemTest())
    }
    fun farmSysTests(testSuite: SELab25TestSuite) {
        testSuite.registerTest(NotMowedApple())
        testSuite.registerTest(FarmControllerSystemTestfield())
        testSuite.registerTest(OtherBlocks())
        testSuite.registerTest(NoForest())
        testSuite.registerTest(FarmNextShed())
        testSuite.registerTest(ContinueOnOther())
        testSuite.registerTest(FarAway())
        testSuite.registerTest(SowNoPlan())
        testSuite.registerTest(Farmeveryfield())
        testSuite.registerTest(FarmeveryplantationOne())
        testSuite.registerTest(FarmeveryplantationTwo())
        testSuite.registerTest(Farmsowone())
        testSuite.registerTest(FarmSimpleSow())
        testSuite.registerTest(Farmsowthree())
        testSuite.registerTest(Farmsowtwo())
        testSuite.registerTest(Farmjump())
        testSuite.registerTest(FarmDryPlant())
        testSuite.registerTest(FarmTravel())
        testSuite.registerTest(FarmSowOrder())
        testSuite.registerTest(MachineChoice())
        testSuite.registerTest(StuckCity())
        testSuite.registerTest(Farmdrought())
        testSuite.registerTest(FarmFallow())
    }
    fun sowingSysTests(testSuite: SELab25TestSuite) {
        testSuite.registerTest(SowingWheatTest())
    }
    fun harvestingSysTests(testSuite: SELab25TestSuite) {
        testSuite.registerTest(CherryHarvestStuckTest())
        testSuite.registerTest(IrrigationChaos())
        // Reset and November
        testSuite.registerTest(CherryTooLateTest())
        testSuite.registerTest(NovemberReset())
        testSuite.registerTest(NovemberResetAccurate())
        // Wheat
        testSuite.registerTest(WheatCycle())
        testSuite.registerTest(WheatCycleExtra())
        // Simon
        testSuite.registerTest(SowingPlanSimpleFullTest())
    }
    fun statisticTests(testSuite: SELab25TestSuite) {
        testSuite.registerTest(StatisticTest())
        testSuite.registerTest(StatisticTest2())
        testSuite.registerTest(StatisticTest3())
        testSuite.registerTest(StatisticTest4())
        testSuite.registerTest(StatisticTest5())
        testSuite.registerTest(StatisticTest6())
        testSuite.registerTest(StatisticTest7())
        testSuite.registerTest(StatisticTest8())
        testSuite.registerTest(StatisticTest9())
        testSuite.registerTest(StatisticTest10())
        testSuite.registerTest(StatisticTest11())
        testSuite.registerTest(StatisticTest12())
    }
    fun failingSysTests(testSuite: SELab25TestSuite) {
        testSuite.registerTest(FarmOtherShed())
        testSuite.registerTest(FarmeveryplantationThree())
    }

    /**
     * Register the tests you want to run against the validation mutants here!
     * The test only check validation, so they log messages will only possibly
     * be incorrect during the parsing/validation.
     * Everything after 'Simulation start' works correctly
     */
    fun registerSystemTestsMutantValidation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(InvalidPlantationNearMe())
        testSuite.registerTest(InvalidNeighbourForestVillage())
        testSuite.registerTest(InvalidNeighbourFarmsteadMeadow())
        testSuite.registerTest(ExampleSystemTest())
        // FarmParser Simple Test
        testSuite.registerTest(NoGrowableExistsTest()) // F ?
        // Scenario Parser (Incident Cloud)
        testSuite.registerTest(CloudAtVillageSimStart()) // T ?
        testSuite.registerTest(CloudCreationOnVillageTest()) // T ?
        testSuite.registerTest(CloudCreationMixedTest()) // T ?
        testSuite.registerTest(CloudCreationOverlapOnVillage()) // T ?
        testSuite.registerTest(CityExpansionMakesAnotherValidNot())

        // Niklas
        testSuite.registerTest(CloudCreationsIntersectInvalidScenario()) // T ?
        testSuite.registerTest(FarmNameCanBeEmpty()) // T ?
        testSuite.registerTest(FieldIsSquareInvalidSystemTest()) // T ?
        testSuite.registerTest(PlantationIsSquareInvalid()) // T ?
        testSuite.registerTest(SowingPlanInvalidBecauseCityExpansion()) // T ?
        testSuite.registerTest(SowingPlanInvalidBecauseCityExpansionTwo()) // T ?
        testSuite.registerTest(TwoCloudsLocationOnSameTileInvalid()) // T ?
        testSuite.registerTest(FieldHasNoFarmOwner())
        testSuite.registerTest(PlantationHasNoOwner())
        testSuite.registerTest(FarmHasNonExistingFields())
        testSuite.registerTest(FarmHasNonExistingPlantations())
        testSuite.registerTest(FarmHasNonExistingFarmsteads())
        testSuite.registerTest(FarmsteadHasOtherFarmsFieldAsNeighbour()) // KEEEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(CityExpansionNotBesidesVillage())
        testSuite.registerTest(CityExpansionBesidesForest())
        testSuite.registerTest(OwnedTileInMapNotInFieldsInFarm())
        testSuite.registerTest(FarmClaimsFieldOfOtherFarm())
        testSuite.registerTest(FarmClaimsPlantationOfOtherFarm())
        testSuite.registerTest(FarmClaimsFarmsteadOfOtherFarm())
        testSuite.registerTest(CityExpansionBesidesCityExpansion())
        // End Niklas
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(SoilMoistureSystemTest()) // KEEEEEEEEEEP
        testSuite.registerTest(NoTimeLeft()) // KEEEEEEEEEP
        // testSuite.registerTest(FarmControllerSystemTeststuck()) // KEEEEEEEEEP
        testSuite.registerTest(NoForest()) // KEEEEEEEEP
        testSuite.registerTest(SoilMoistureSystemTest()) // T ? // KEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(CloudSystemTest()) // KEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(DoubleBrokenMachineIncident()) // T ? // KEEEEEEEEEEEEP
        testSuite.registerTest(CityExpansionOnCloud()) // T ? // KEEEEEEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(CloudCreationDurationTest()) // KEEEEEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(EstimateHarvestSimpleSystemTestCuttingMissedApple()) // KEEEEEEP
        testSuite.registerTest(EstimateHarvestSimpleSystemTestOneYear()) // KEEEEEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(FarAway()) // KEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(Farmeveryfield()) // KEEEEEEEEEEEEEEEEEEEEP
        testSuite.registerTest(FarmNextShed()) // KEEEEEEEEEEP

        // NEW TESTS BELOW THIS LINE ___________________________________________________________________________-
        testSuite.registerTest(DroughtMoistureReductionSystemTest())
        testSuite.registerTest(FarmNextShed())
        testSuite.registerTest(FarAway())
        testSuite.registerTest(SowNoPlan())
        testSuite.registerTest(Farmeveryfield())
        testSuite.registerTest(Farmsowone())
        testSuite.registerTest(FarmSimpleSow())
        testSuite.registerTest(Farmsowthree())
        testSuite.registerTest(Farmsowtwo())
        testSuite.registerTest(Farmjump())
        testSuite.registerTest(FarmTravel())
        testSuite.registerTest(MachineChoice())
        testSuite.registerTest(StuckCity())
        testSuite.registerTest(Farmdrought())
        testSuite.registerTest(EstimateHarvestSimpleSystemTestLateSowingMoreRounds())
        testSuite.registerTest(DoubleBrokenMachineIncident()) // Passes Ref
        testSuite.registerTest(BrokenMachineDurationOne()) // Passes Ref
        // City Expansion
        testSuite.registerTest(CityExpansionOnCloud()) // ?
        // Cloud Creation
        testSuite.registerTest(CloudCreationDurationTest()) // ?

        // Niklas
        testSuite.registerTest(SmallBrokenMachine())
        testSuite.registerTest(BeeHappyIncidentTestNiklas())
        testSuite.registerTest(SeveralBeeHappyNiklas())
        testSuite.registerTest(OneBeeHappyCannotAffectMultipleTimes())
        testSuite.registerTest(BeeHappyBiggerNiklas())
        testSuite.registerTest(BrokenMachineFullTest())
        testSuite.registerTest(TestBrokenMachine())
        testSuite.registerTest(DroughtTestNiklas())
        testSuite.registerTest(BigSystemTestFullNiklas())
    }
}
