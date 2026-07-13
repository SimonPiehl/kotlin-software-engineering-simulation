package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.SunConstants
import de.unisaarland.cs.se.selab.util.YearTicks
import de.unisaarland.cs.se.selab.view.Logger

/**
 * Controls soil moisture and sunlight for growable tiles.
 *
 * Uses simulation data to reduce soil moisture and adapt sun values over time.
 */
class SoilMoistureAndSunController(private val simulationData: SimulationData) {

    /** Maps each year tick to the corresponding sunlight value. */
    private val sunByYearTick: Map<Int, Int> = mapOf(
        YearTicks.EARLY_JANUARY to SunConstants.JANUARY_MONTHLY_SUN,
        YearTicks.LATE_JANUARY to SunConstants.JANUARY_MONTHLY_SUN,
        YearTicks.EARLY_FEBRUARY to SunConstants.FEBRUARY_MONTHLY_SUN,
        YearTicks.LATE_FEBRUARY to SunConstants.FEBRUARY_MONTHLY_SUN,
        YearTicks.EARLY_MARCH to SunConstants.MARCH_MONTHLY_SUN,
        YearTicks.LATE_MARCH to SunConstants.MARCH_MONTHLY_SUN,
        YearTicks.EARLY_APRIL to SunConstants.APRIL_MONTHLY_SUN,
        YearTicks.LATE_APRIL to SunConstants.APRIL_MONTHLY_SUN,
        YearTicks.EARLY_MAY to SunConstants.MAY_MONTHLY_SUN,
        YearTicks.LATE_MAY to SunConstants.MAY_MONTHLY_SUN,
        YearTicks.EARLY_JUNE to SunConstants.JUNE_MONTHLY_SUN,
        YearTicks.LATE_JUNE to SunConstants.JUNE_MONTHLY_SUN,
        YearTicks.EARLY_JULY to SunConstants.JULY_MONTHLY_SUN,
        YearTicks.LATE_JULY to SunConstants.JULY_MONTHLY_SUN,
        YearTicks.EARLY_AUGUST to SunConstants.AUGUST_MONTHLY_SUN,
        YearTicks.LATE_AUGUST to SunConstants.AUGUST_MONTHLY_SUN,
        YearTicks.EARLY_SEPTEMBER to SunConstants.SEPTEMBER_MONTHLY_SUN,
        YearTicks.LATE_SEPTEMBER to SunConstants.SEPTEMBER_MONTHLY_SUN,
        YearTicks.EARLY_OCTOBER to SunConstants.OCTOBER_MONTHLY_SUN,
        YearTicks.LATE_OCTOBER to SunConstants.OCTOBER_MONTHLY_SUN,
        YearTicks.EARLY_NOVEMBER to SunConstants.NOVEMBER_MONTHLY_SUN,
        YearTicks.LATE_NOVEMBER to SunConstants.NOVEMBER_MONTHLY_SUN,
        YearTicks.EARLY_DECEMBER to SunConstants.DECEMBER_MONTHLY_SUN,
        YearTicks.LATE_DECEMBER to SunConstants.DECEMBER_MONTHLY_SUN
    )

    private var underThresholdFieldCount = 0
    private var underThresholdPlantationCount = 0

    /**
     * Reduces soil moisture for all growable tiles and updates sun values.
     * Also resets plantation tiles in early november
     *
     * Iterates through all growable tiles and calls
     * [reduceMoistureOfGrowable] and [adaptSunOfGrowable] for each tile.
     */
    fun reduceSoilMoistureAndAdaptSun() {
        simulationData.clearTilesHarvestChanged()
        val map = simulationData.getMap()
        val growableTileList = map.getAllGrowable()
        // resets plantation tiles in early november
        if (simulationData.getYearTick() == YearTicks.EARLY_NOVEMBER) resetPlantationsInNovember()
        // reduction of soil moisture and sun adaption for each tile with a growable
        growableTileList.forEach { tile ->
            reduceMoistureOfGrowable(tile)
            adaptSunOfGrowable(tile)
            // resets [changedInTickByIncident] field for this tick (preparation step)
            tile.getGrowable()?.resetIncidentsThisTick()
        }
        Logger.logGrowableLowMoisture(underThresholdFieldCount, underThresholdPlantationCount)
        underThresholdFieldCount = 0
        underThresholdPlantationCount = 0
    }

    /**
     * Reduces the soil moisture of a single growable tile.
     *
     * @param tile the [Tile] to reduce moisture for
     */
    private fun reduceMoistureOfGrowable(tile: Tile) {
        tile.reduceMoisture()
        if (tile.amIBelowMoistureThreshold() && !tile.amIPermanentlyDisabled()) {
            if (tile.getTileType() == TileType.FIELD) {
                underThresholdFieldCount++
            } else {
                underThresholdPlantationCount++
            }
        }
    }

    /**
     * Resets all plantation tiles at the beginning of November.
     *
     * This simulates the seasonal reset of plantations for the next cultivation cycle.
     */
    private fun resetPlantationsInNovember() {
        val tiles = simulationData.getMap().getAllGrowable()
        for (tile in tiles) {
            if (tile.getTileType() == TileType.PLANTATION && tile.getGrowable()?.getPermanentDisabled() == false) {
                val plant = tile.getGrowable()?.getCurrentPlant()
                tile.getGrowable()?.setWasCutAtTick(-1)
                tile.getGrowable()?.setWasHarvestedAtTick(-1)
                tile.getGrowable()?.setWasMowedAtTick(-1)
                tile.getGrowable()?.setCropsExpected(plant?.idealEstimate ?: 0)
            }
        }
    }

    /**
     * Adjusts the sun value of a single growable tile based on the current yearTick.
     *
     * @param tile the [Tile] to adapt sun for
     */
    private fun adaptSunOfGrowable(tile: Tile) {
        val growable = tile.getGrowable()
        val yearTick = simulationData.getYearTick()
        val sunValue = sunByYearTick[yearTick]
        if (sunValue != null) {
            growable?.setSunlightExposure(sunValue)
        }
    }
}
