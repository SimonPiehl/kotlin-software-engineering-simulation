package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.view.Logger
/**
 * Simulator
 *
 * @property SimulationData
 * @constructor Create empty Simulator
 */
class Simulator(private val data: SimulationData) {

    private val harvestController: EstimateHarvestController = EstimateHarvestController(data)
    private val soilAndSunController: SoilMoistureAndSunController = SoilMoistureAndSunController(data)
    private val cloudController: CloudController = CloudController(data)
    private val farmController: FarmController = FarmController(data, PathFinderController(data))
    private val incidentController: IncidentController = IncidentController(data)

    /**
     * Starts the Simulation and does oneRound until Current Tick = Max_Tick or 1000
     *
     */
    fun simulate() {
        // Starts simulation of first round
        harvestController.firstroundsimulation()
        // Logs start
        Logger.logSimStart(data.getYearTick())
        // Just to ensure tick correct set to 0
        data.setCurrentTick(0)
        // While not max tick or max_max_tick
        while (data.getCurrentTick() < data.getMaxTick() &&
            data.getCurrentTick() < GeneralConstants.MAX_MAX_TICK
        ) {
            oneRound()
            data.setCurrentTick(data.getCurrentTick() + 1)
            data.setYearTick((data.getYearTick() % GeneralConstants.AMOUNT_24) + 1)
        }
        Logger.logSimEnd(data.getCurrentTick())
        calculateStatistic()
    }

    /**
     * Starts simulation of round calls every single controller
     *
     */
    private fun oneRound() {
        Logger.logTickStart(data.getCurrentTick(), data.getYearTick())
        soilMoistureAct()
        cloudsAct()
        farmsAct()
        incidentsAct()
        computeHarvest()
    }

    private fun soilMoistureAct() {
        soilAndSunController.reduceSoilMoistureAndAdaptSun()
    }

    private fun cloudsAct() {
        cloudController.cloudMovement()
    }

    private fun farmsAct() {
        farmController.farmsAction()
    }

    private fun incidentsAct() {
        incidentController.checkActiveIncidents()
        incidentController.checkIncidents()
    }

    private fun computeHarvest() {
        harvestController.calculateEstimateHarvest()
    }

    /**
     * Calculate statistic of farms and total
     *
     */
    private fun calculateStatistic() {
        var totalAmountOfPotatoHarvested = 0
        var totalAmountOfWheatHarvested = 0
        var totalAmountOfOatHarvested = 0
        var totalAmountOfPumpkinHarvested = 0
        var totalAmountOfAppleHarvested = 0
        var totalAmountOfGrapeHarvested = 0
        var totalAmountOfAlmondHarvested = 0
        var totalAmountOfCherryHarvested = 0
        var totalEstimateLeftInPlants = 0

        Logger.logStatisticsAreCalculated()
        // Iterates through every farm to get statistics of every plant type
        for (farm in data.getFarms()) {
            Logger.logFarmStatistic(farm.getID(), farm.getStatistic().computeCombinedHarvest())
            totalAmountOfPotatoHarvested += farm.getStatistic().getPotatoAmount()
            totalAmountOfWheatHarvested += farm.getStatistic().getWheatAmount()
            totalAmountOfOatHarvested += farm.getStatistic().getOatAmount()
            totalAmountOfPumpkinHarvested += farm.getStatistic().getPumpkinAmount()
            totalAmountOfAppleHarvested += farm.getStatistic().getAppleAmount()
            totalAmountOfGrapeHarvested += farm.getStatistic().getGrapeAmount()
            totalAmountOfAlmondHarvested += farm.getStatistic().getAlmondAmount()
            totalAmountOfCherryHarvested += farm.getStatistic().getCherryAmount()
        }
        // Iterates through every tile to get remaining estimation
        for (tile in data.getMap().getAllGrowable()) {
            totalEstimateLeftInPlants += tile.getGrowable()?.getCropsExpected() ?: 0
        }
        Logger.logHarvestOfPlantStatistic(PlantType.POTATO, totalAmountOfPotatoHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.WHEAT, totalAmountOfWheatHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.OAT, totalAmountOfOatHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.PUMPKIN, totalAmountOfPumpkinHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.APPLE, totalAmountOfAppleHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.GRAPE, totalAmountOfGrapeHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.ALMOND, totalAmountOfAlmondHarvested)
        Logger.logHarvestOfPlantStatistic(PlantType.CHERRY, totalAmountOfCherryHarvested)

        Logger.logRemainingEstimateHarvest(totalEstimateLeftInPlants)
    }
}
