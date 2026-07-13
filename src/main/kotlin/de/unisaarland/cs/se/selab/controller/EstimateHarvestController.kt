package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.util.PlantConstants
import de.unisaarland.cs.se.selab.util.PlantConstants.FINE_LATE_WEEDING
import de.unisaarland.cs.se.selab.util.PlantConstants.HUNDRED_FOR_PERCENTAGE
import de.unisaarland.cs.se.selab.view.Logger

/**
 * Estimate harvest controller
 *
 * @property data
 * @constructor Create empty Estimate harvest controller
 * goes over the Tiles, gives them forard and resets Updates the Fields which are unusable
 */
class EstimateHarvestController(private var data: SimulationData) {

    /**
     * Calculate estimate harvest
     *
     */
    fun calculateEstimateHarvest() {
        val map = data.getMap()
        val tiles = map.getAllGrowable().sortedBy { it.getID() }

        // iterate ove not disabled Tiles
        for (element in tiles) {
            if (!(element.getGrowable()?.getPermanentDisabled() ?: true)) {
                calculateEstimateHarvestTile(element)
            }
        }
        // adding the newly disabled Tiles
        droughtCheck()
    }

    /**
     * Firstroundsimulation
     * simulation in the first round, checks the tiles and gives them to lower functions
     *
     */
    fun firstroundsimulation() {
        val map = data.getMap()
        val tiles = map.getAllGrowable()
        val yeartick = data.getYearTick()
        // setting the year tick one lowe, because it is once applied before the Round
        data.setYearTick(yeartick - 1)
        // default default value
        data.setCurrentTick(GeneralConstants.MINUS_TWO)

        // iterate over all Plantations
        for (element in tiles) {
            if (element.getTileType() == TileType.PLANTATION) {
                // iterate over plantation and filter out Grape, since they are Special
                if (element.getGrowable()?.getCurrentPlant() == PlantType.GRAPE) {
                    estimateGrapeFirstRound(element)
                    firstRoundZero(element)
                } else {
                    calculateHarvestEstimation(element)
                    firstRoundZero(element)
                }
            }
        }
        // reset to the of the correct
        data.setCurrentTick(0)
        data.setYearTick(yeartick)
        return
    }

    /**
     * Estimate grape first round
     * Grape calculation for the first round
     *
     * @param tile
     */
    private fun estimateGrapeFirstRound(tile: Tile) {
        val yearTick = data.getYearTick() + 1
        // estimation reduction if in that window
        if (yearTick >= PlantConstants.GRAPE_HARVESTING_END + 1 &&
            yearTick <= PlantConstants.GRAPE_HARVESTING_END + 3
        ) {
            var help = PlantConstants.GRAPE_HARVESTING_END + 1
            var res = PlantConstants.GRAPE_IDEAL_ESTIMATE
            // apply the penalties help times
            while (help <= yearTick) {
                res = (res * (PlantConstants.GRAPE_HARVESTING_FINE.toDouble() / PlantConstants.HUNDRED_FOR_PERCENTAGE))
                    .toInt()
                help += 1
            }

            tile.getGrowable()?.setCropsExpected(res)
        }
    }

    /**
     * First round zero setting of the crop
     *
     * @param tile
     */
    private fun firstRoundZero(tile: Tile) {
        val yearTick = data.getYearTick() + 1
        val g = tile.getGrowable() ?: null
        val plant = g?.getCurrentPlant()
        // iterate over plants and find the right Fine
        when (plant) {
            PlantType.APPLE, PlantType.ALMOND, PlantType.GRAPE -> return
            PlantType.CHERRY -> if (yearTick in PlantConstants.CHERRY_HARVESTING_END + 2..<PlantConstants.RESET_MONTH) {
                g.setCropsExpected(0)
            }
            else -> return
        }
    }

    /**
     * Calculate estimate harvest of one Tile
     * Big first Function, which iterates over the tiles, applies the fines and then calls the Function which
     * estimates the Log
     *
     * @param tile which has a growable as received from map.getAllGrowable()
     */
    fun calculateEstimateHarvestTile(tile: Tile) {
        val actionsNotPerformed = mutableListOf<Action>()
        // Boolean Variables for needed actions
        val expBefore = tile.getGrowable()?.getCropsExpected()
        val irr: Boolean
        var weed = true
        var harvest: Boolean
        var cut = true
        var mowed = true

        val id = tile.getID()
        // selecting which functios will be used
        when (tile.getTileType()) {
            TileType.FIELD -> {
                calculateSowingEstimation(tile)
                irr = calculateMoistureAndSunReduction(tile)
                weed = calculateWeedingEstimation(tile)
                harvest = calculateHarvestEstimation(tile)
            }
            TileType.PLANTATION -> {
                irr = calculateMoistureAndSunReduction(tile)
                cut = calculateCutting(tile)
                mowed = calculateMowingEstimation(tile)
                harvest = calculateHarvestEstimation(tile)
            }
            else -> {
                return
            }
        }

        // Get Harvest
        val expAfter = tile.getGrowable()?.getCropsExpected()
        val plant = tile.getGrowable()?.getCurrentPlant()

        // if it could not find growable or plant return
        if (expAfter == null || expBefore == null || plant == null) {
            return
        }

        if (!weed) {
            actionsNotPerformed.add(Action.WEEDING)
        }
        if (!cut) {
            actionsNotPerformed.add(Action.CUTTING)
        }
        if (!mowed) {
            actionsNotPerformed.add(Action.MOWING)
        }
        if (!irr) {
            actionsNotPerformed.add(Action.IRRIGATING)
        }
        if (!harvest) {
            actionsNotPerformed.add(Action.HARVESTING)
        }

        // Log only if harvest was changed
        if (!droughtCheckTile(tile)) {
            if (actionsNotPerformed.isNotEmpty()) {
                Logger.logRequiredActionNotPerformed(id, actionsNotPerformed)
            }
        }

        addIncidentscalculationandlogging(tile, expBefore)
    }

    /**
     * Add log messages for harvest Changed by Incidents
     * addes Differewnces of Incidents and Loggs the Changed HArvest
     *
     * @param tile
     */
    private fun addIncidentscalculationandlogging(tile: Tile, expBefore: Int) {
        val incList = data.getTilesHarvestChanged()
        val plant = tile.getGrowable()?.getCurrentPlant() ?: return
        val id = tile.getID()
        var res = tile.getGrowable()?.getCropsExpected() ?: 0

        for ((element, liste) in incList) {
            if (element == tile) {
                for (value in liste) {
                    res = (res * value).toInt()
                    tile.getGrowable()?.setCropsExpected(res)
                }
            }
        }

        var expAfter = tile.getGrowable()?.getCropsExpected() ?: return

        if (expAfter == expBefore) {
            if (data.getYearTick() == PlantConstants.RESET_MONTH && tile.getTileType() == TileType.PLANTATION) {
                Logger.logChangedHarvestEstimate(id, expAfter, plant)
            }
            return
        }
        if (expAfter == 0 || expAfter < 0) {
            expAfter = 0
            Logger.logChangedHarvestEstimate(id, expAfter, plant)
            reset(tile)
            return
        }

        if (expAfter != expBefore) {
            Logger.logChangedHarvestEstimate(id, expAfter, plant)
        }
        return
    }

    /**
     * Drought check
     * checks if there are droughts and disables Tiles
     *
     * @param tile
     */
    private fun droughtCheck() {
        val droughts = data.getTilesWhereDrought()
        for (element in droughts) {
            if (element.getTileType() == TileType.PLANTATION) {
                element.getGrowable()?.setPermanentDisabled(true)
            }
        }
    }

    /**
     * Drought check tile
     * checks if there was a drought on this tiile and returns true
     *
     * @param tile
     * @return
     */
    private fun droughtCheckTile(tile: Tile): Boolean {
        val droughts = data.getTilesWhereDrought()
        if (droughts.contains(tile)) {
            return true
        }
        return false
    }

    /**
     * Calculate moisture and sun reduction
     * Reduces the corps for the sun and soilmoisture
     * outputs true if reduced and irrigation should be logged
     *
     * @param tile
     * @return
     */
    private fun calculateMoistureAndSunReduction(tile: Tile): Boolean {
        // Sun and Moisture
        // zero check
        val growable = tile.getGrowable() ?: return true // TODO Logik Prüfen
        if (growable.getWasHarvestedAtTick() == data.getCurrentTick()) return true
        val plant = tile.getGrowable()?.getCurrentPlant()
        val idealSoilMoisture = plant?.idealMoisture ?: 0
        val idealSunlight = plant?.idealSunlight ?: 0
        val exposureMoisture = tile.getGrowable()?.getMoistureExposure() ?: 0
        val exposureSunlight = tile.getGrowable()?.getSunlightExposureCurrentTick() ?: 0

        var ret = true

        // Sunlight reduction
        var sunDiff = exposureSunlight - idealSunlight
        while (sunDiff >= PlantConstants.SUNLIGHT_DIFFERENCE) {
            val res = growable.getCropsExpected()
            val nintyPercent = (res * PlantConstants.SUNLIGHT_PERCENTAGE).toInt()
            growable.setCropsExpected(nintyPercent)
            sunDiff -= PlantConstants.SUNLIGHT_DIFFERENCE
        }

        val estimated = growable.getCropsExpected()

        // SoilMoisture reduction
        // check if exposure Moisture == 0
        if (exposureMoisture <= 0) {
            growable.setCropsExpected(0)
            return false
        }
        var moistureDiff = idealSoilMoisture - exposureMoisture
        while (moistureDiff >= PlantConstants.MOISTURE_DIFFERENCE) {
            val curr = growable.getCropsExpected()
            var res = curr - PlantConstants.MOISTURE_LOSS
            if (res <= 0) {
                res = 0
            }
            ret = false
            growable.setCropsExpected(res)
            moistureDiff -= PlantConstants.MOISTURE_DIFFERENCE
        }

        ret = estimateBeforAfterCheck(estimated, growable.getCropsExpected())
        return ret
    }

    /**
     * Reset
     * resets the tile to the defaultvalues
     *
     * @param tile
     */
    private fun reset(tile: Tile) {
        val g = tile.getGrowable()
        val currTick = data.getCurrentTick()
        if (g != null) {
            g.setCropsExpected(0)
            g.setWasHarvestedAtTick(-1)

            if (tile.getTileType() == TileType.FIELD) {
                g.setWasWeededAtTick(mutableListOf())
                if (g.getCurrentPlant() != null) {
                    g.setLiesFallowSinceTick(currTick)
                }
                g.setWasSowedAtTick(-1)
                g.setCurrentPlant(null)
            } else {
                g.setWasCutAtTick(-1)
                g.setWasMowedAtTick(-1)
            }
        }
    }

    /**
     * Calculate sowing estimation
     * galculate the sowing for each tile, reduces in late and +1
     *
     * @param tile
     */
    private fun calculateSowingEstimation(tile: Tile) {
        val currTick = data.getCurrentTick()
        val yearTick = data.getYearTick()
        val growable = tile.getGrowable() ?: return
        val plant = growable.getCurrentPlant() ?: return
        // estimate only ≠ idealEstimate if incident already influenced it
        val currEstimate = growable.getCropsExpected()
        val sowingList = plant.schedule.getTicks(Action.SOWING)
        val regularEndTick = sowingList[1]
        // sowing
        val sowed = growable.getWasSowedAtTick()
        // sowing computation only when sowed is in the current tick. Initialization of estimate harvest at this moment
        if (sowed == currTick) {
            // If Plant was sowed at current Tick we need to calculate a penalty if sowing was too late
            // -20% for one delayed tick
            if (yearTick == (regularEndTick + 1) % GeneralConstants.AMOUNT_24) {
                growable.setCropsExpected(
                    kotlin.math.floor(currEstimate * PlantConstants.REDUCTION_FOR_LATE_SOWING_FIRST).toInt()
                )
            }
            // 2x -20* for two delayed ticks
            if (yearTick == (regularEndTick + 2) % GeneralConstants.AMOUNT_24) {
                var res = kotlin.math.floor(currEstimate * PlantConstants.REDUCTION_FOR_LATE_SOWING_FIRST).toInt()
                res = kotlin.math.floor(res * PlantConstants.REDUCTION_FOR_LATE_SOWING_FIRST).toInt()
                growable.setCropsExpected(res)
            }
        }
    }

    /**
     * Calculate harvest estimation
     * calculates the fines for harvesting, is applied at the end of rounds before
     *returns true if fine applied
     * @param tile
     * @return
     */
    private fun calculateHarvestEstimation(tile: Tile): Boolean {
        val currTick = data.getCurrentTick()
        val yearTick = data.getYearTick()
        val growable = tile.getGrowable() ?: return true // TODO Logik prüfen
        val harvested = growable.getWasHarvestedAtTick()
        var ret = true

        val plant = growable.getCurrentPlant() ?: return true // TODO Logik prüfen
        val currentEstimation = growable.getCropsExpected()

        // test if harvested this tick or if estimation is already 0
        if (harvested == currTick || currentEstimation == 0) {
            growable.setCropsExpected(0)
            return ret
        }

        val harvestingList = plant.schedule.getTicks(Action.HARVESTING)
        val end = harvestingList[1]
        val longer = harvestingList[2]
        val fine = harvestingList[PlantConstants.THREE]
        val percentage = fine.toDouble() / HUNDRED_FOR_PERCENTAGE

        if (yearTick >= end && yearTick < end + longer + 1) {
            ret = false

            if (yearTick == end + longer) { // harvested too late, set to 0 and reseted
                growable.setCropsExpected(0)
                /*
                if (tile.getTileType() == TileType.FIELD) tile.getGrowable()?.setCurrentPlant(null)
                tile.getGrowable()?.setWasSowedAtTick(-1)
                tile.getGrowable()?.setWasWeededAtTick(mutableListOf())
                tile.getGrowable()?.setWasCutAtTick(-1)
                tile.getGrowable()?.setWasMowedAtTick(-1)*/
            } else { // fine applied to Estimation and set
                val res = (currentEstimation * percentage).toInt()
                growable.setCropsExpected(res)
            }
        }
        return ret
    }

    /**
     * Apply penalty helper for calculate
     *
     * @param tile
     * @param percent
     * @return
     */
    private fun applyPenalty(tile: Tile, percent: Double): Boolean {
        val g = tile.getGrowable() ?: return false
        val washarvested = tile.getGrowable()?.getWasHarvestedAtTick() == data.getCurrentTick()
        if (!washarvested) {
            val est = g.getCropsExpected()
            g.setCropsExpected((est * percent).toInt())
            return true // penalty applied
        }
        return false
    }

    /**
     * Calculate weeding estimation
     *differents over the plants, applies fines
     *outputs true if it should be logged
     * @param tile
     * @return
     */
    private fun calculateWeedingEstimation(tile: Tile): Boolean {
        val growable = tile.getGrowable() ?: return true
        val plant = growable.getCurrentPlant()

        val currTick = data.getCurrentTick()

        val sowed = growable.getWasSowedAtTick()
        val weeded: List<Int> = growable.getWasWeededAtTick()
        val lastWeeded: Int? = weeded.lastOrNull()

        val percent = FINE_LATE_WEEDING.toDouble() / HUNDRED_FOR_PERCENTAGE

        var penalized = true

        when (plant) {
            PlantType.POTATO, PlantType.PUMPKIN -> {
                val sinceSowing = currTick - sowed
                val shouldWeedThisTick = sinceSowing > 0 && sinceSowing % 2 == 0

                // Malus, wenn heute gejätet werden sollte, aber NICHT gejätet wurde
                if (shouldWeedThisTick && lastWeeded != currTick) {
                    penalized = !applyPenalty(tile, percent)
                }
            }

            PlantType.OAT -> {
                val ticksWindow = PlantConstants.OAT_WEEDING_TICKS_AFTER_SOWING
                val sinceSowing = currTick - sowed
                val shouldWeedNow = sinceSowing in 1..ticksWindow
                if (shouldWeedNow && lastWeeded != currTick) {
                    penalized = !applyPenalty(tile, percent)
                }
            }

            PlantType.WHEAT -> {
                // Pflicht-Jätezeitpunkte relativ zur Aussaat
                val at3 = currTick - sowed == PlantConstants.THREE
                val at9 = currTick - sowed == PlantConstants.NINE
                // Index-gesichert prüfen, ob an diesen Pflicht-Ticks gejätet wurde
                if (at3 || at9) {
                    val wasWeeded = lastWeeded == currTick
                    if (!wasWeeded) {
                        penalized = !applyPenalty(tile, percent)
                    }
                }
            }

            else -> {
                return true
            }
        }

        return penalized
    }

    /**
     * Calculate cutting
     * calculates cutting for Plants
     * return true if it should be logged
     *
     * @param tile
     * @return
     */
    private fun calculateCutting(tile: Tile): Boolean {
        val yearTick = data.getYearTick()
        val growable = tile.getGrowable() ?: return true // TODO Logik prüfen
        val cut = growable.getWasCutAtTick()
        val plant = growable.getCurrentPlant() ?: return true // TODO Logik prüfen
        val cuttingList = plant.schedule.getTicks(Action.CUTTING)
        val estimate = growable.getCropsExpected()
        val percent = PlantConstants.REDUCTION_FOR_LATE_CUTTING

        // after last cutting period check if it was cutted
        if (yearTick == cuttingList.last()) {
            if (cut == -1) {
                val res = (estimate * percent).toInt()
                growable.setCropsExpected(res)
                return false
            }
            growable.setWasCutAtTick(-1)
        }
        return true
    }

    /**
     * Calculate mowing estimation
     * returns true if should be logged
     *
     * @param tile
     * @return
     */
    private fun calculateMowingEstimation(tile: Tile): Boolean {
        val currTick = data.getCurrentTick()
        val yearTick = data.getYearTick()
        val growable = tile.getGrowable() ?: return true
        val plant = growable.getCurrentPlant() ?: return true
        if (growable.getWasHarvestedAtTick() == currTick || tile.getGrowable()?.getWasHarvestedAtTick() != -1) {
            return true
        }
        val mowingList = plant.schedule.getTicks(Action.MOWING)
        val mowed = growable.getWasMowedAtTick()
        val estimate = growable.getCropsExpected()
        var ret = true

        when (plant) {
            PlantType.ALMOND, PlantType.APPLE, PlantType.GRAPE ->
                if (yearTick == mowingList[0] || yearTick == mowingList[1]) {
                    if (currTick != mowed) {
                        val res = (estimate * PlantConstants.REDUCTION_FOR_MISSED_MOWING).toInt()
                        growable.setCropsExpected(res)
                        ret = false
                    }
                }
            PlantType.CHERRY ->
                if (yearTick == mowingList[0]) {
                    if (currTick != mowed) {
                        val res = (estimate * PlantConstants.REDUCTION_FOR_MISSED_MOWING).toInt()
                        growable.setCropsExpected(res)
                        ret = false
                    }
                }
            else -> ret = true
        }
        ret = estimateBeforAfterCheck(estimate, growable.getCropsExpected())
        return ret
    }

    /**
     * Estimate befor after check
     * helper for mowing
     *
     * @param befor
     * @param after
     * @return
     */
    private fun estimateBeforAfterCheck(befor: Int, after: Int): Boolean {
        if (befor == after) return true
        return false
    }
}
