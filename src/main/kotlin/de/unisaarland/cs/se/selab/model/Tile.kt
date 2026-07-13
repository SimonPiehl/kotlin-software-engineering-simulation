package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * Tile
 *
 * @property id
 * @property tileType
 * @property coordinate
 * @property airflow
 * @property direction NONE if no airflow on tile
 * @property shedExists always false for all tiles excluding Farmsteads
 * @property growable null if not PLANTATION or FIELD
 * @constructor Creates a Tile
 */
class Tile(
    private val id: Int,
    private var tileType: TileType,
    private val coordinate: Coordinate,
    private var airflow: Boolean,
    private var direction: Direction,
    private var shedExists: Boolean,
    private var growable: Growable?
) {

    // Setter

    /**
     * Set tile type
     *
     * @param tileType
     */
    fun setTileType(tileType: TileType) {
        this.tileType = tileType
    }

    /**
     * Set airflow
     *
     * @param airflow
     */
    fun setAirflow(airflow: Boolean) {
        this.airflow = airflow
    }

    /**
     * Set direction
     *
     * @param direction
     */
    fun setDirection(direction: Direction) {
        this.direction = direction
    }

    /**
     * Set growable
     *
     * @param growable
     */
    fun setGrowable(growable: Growable?) {
        this.growable = growable
    }

    /**
     * Can I be sowed
     * @param currentTick
     * @param yearTick
     * @return if tile can be currently sowed
     */
    fun canIBeSowed(currentTick: Int, yearTick: Int, plant: PlantType): Boolean {
        val g = growable ?: return false
        if (currentTick - g.getLiesFallowSinceTick() <= YearTicks.LATE_FEBRUARY) return false
        //
        if (g.getCropsExpected() != 0) return false
        if (!g.getPossiblePlants().contains(plant)) return false
        //
        return plant.schedule.canPerform(Action.SOWING, yearTick, 0, 0, plant) &&
            !checkWorkedOnThisTick(currentTick)
    }

    /**
     * Can I be harvested
     * @param yearTick
     * @return if tile can be currently harvested
     */
    fun canIBeHarvested(currentTick: Int, yearTick: Int): Boolean {
        val g = growable ?: return false
        if (g.getPermanentDisabled() || g.getCropsExpected() == 0) return false
        val pType = g.getCurrentPlant() ?: return false
        return pType.schedule.canPerform(
            Action.HARVESTING, yearTick, 0, 0,
            pType
        ) &&
            !checkWorkedOnThisTick(currentTick)
    }

    /**
     * Can I be cut
     *
     * @param yearTick
     * @return
     */
    fun canIBeCut(currentTick: Int, yearTick: Int): Boolean {
        val g = growable ?: return false
        if (g.getPermanentDisabled() || g.getCropsExpected() == 0) return false
        val pType = g.getCurrentPlant() ?: return false
        if (g.getWasCutAtTick() != -1) {
            return false
        }
        return pType.schedule.canPerform(
            Action.CUTTING, yearTick, 0, 0,
            pType
        ) &&
            !checkWorkedOnThisTick(currentTick)
    }

    /**
     * Can I be weeded
     *
     * @param currentTick
     * @return
     */
    fun canIBeWeeded(currentTick: Int): Boolean {
        val g = growable ?: return false
        val pType = g.getCurrentPlant() ?: return false
        if (g.getCropsExpected() == 0) return false
        return pType.schedule.canPerform(
            Action.WEEDING, 0, currentTick, g.getWasSowedAtTick(), pType
        ) &&
            !checkWorkedOnThisTick(currentTick)
    }

    /**
     * Can I be pollinated
     * checks if plant requires pollination and if it is possible according to currYearTick
     * @param yearTick
     * @return
     */
    fun canIBePollinated(yearTick: Int, currentTick: Int): Boolean {
        val g = growable ?: return false
        if (g.getPermanentDisabled()) return false
        val pType = g.getCurrentPlant() ?: return false
        // Check if Plant is effected (if plant exists)
        if (!(pType.effectedByPollination)) return false
        val condition = detektHelperCanIBePollinated(yearTick, currentTick, pType, g)

        return condition
    }

    /**
     * Detekt helper for canIBePollinated
     *
     * @param yearTick
     * @param pType
     * @param g
     * @return
     */
    fun detektHelperCanIBePollinated(yearTick: Int, currTick: Int, pType: PlantType, g: Growable): Boolean {
        val conditionPotato = g.getWasSowedAtTick() + 3 + 1 == currTick
        val conditionPumpkin = g.getWasSowedAtTick() + 2 + 1 == currTick ||
            g.getWasSowedAtTick() + 2 + 1 + 1 == currTick

        val conditionAppleAndCherry = yearTick == YearTicks.LATE_APRIL || yearTick == YearTicks.EARLY_MAY
        val conditionAlmond = yearTick == YearTicks.LATE_FEBRUARY || yearTick == YearTicks.EARLY_MARCH

        val canBePollinated = when (pType) {
            PlantType.POTATO -> conditionPotato
            PlantType.PUMPKIN -> conditionPumpkin
            PlantType.APPLE -> conditionAppleAndCherry
            PlantType.ALMOND -> conditionAlmond
            PlantType.CHERRY -> conditionAppleAndCherry
            else -> false
        }
        return canBePollinated
    }

    /**
     * Can I be mowed
     *
     * @param yearTick
     * @return
     */
    fun canIBeMowed(currentTick: Int, yearTick: Int): Boolean {
        val g = growable ?: return false
        if (g.getPermanentDisabled() || g.getCropsExpected() == 0) return false
        val pType = g.getCurrentPlant() ?: return false
        return pType.schedule.canPerform(
            Action.MOWING, yearTick, 0, 0, pType
        ) && !checkWorkedOnThisTick(currentTick)
    }

    /**
     * Can I be mowed by animals
     *
     * @param yearTick
     * @return
     */
    fun canIBeMowedByAnimals(yearTick: Int): Boolean {
        val g = growable ?: return false
        val pType = g.getCurrentPlant() ?: return false
        return pType.schedule.canPerform(
            Action.MOWING,
            yearTick,
            0,
            0,
            pType
        )
    }

    /**
     * Reduce moisture
     *
     */
    fun reduceMoisture() {
        growable?.let { g ->
            val reduction = if (g.getCurrentPlant() == null) {
                GeneralConstants.MOISTURE_REDUCTION_70
            } else {
                GeneralConstants.MOISTURE_REDUCTION_100
            }
            g.setMoistureExposure(kotlin.math.max(0, g.getMoistureExposure() - reduction))
        }
    }

    /**
     * Can I be irrigated
     *
     * @return
     */
    fun canIBeIrrigated(currentTick: Int): Boolean {
        return amIBelowMoistureThreshold() && !checkWorkedOnThisTick(currentTick) && !amIPermanentlyDisabled()
    }

    /**
     * Am I below moisture threshold
     *
     * @return
     */
    fun amIBelowMoistureThreshold(): Boolean {
        growable?.let { g ->
            val currMoisture = g.getMoistureExposure()
            return currMoisture < (g.getCurrentPlant()?.idealMoisture ?: (currMoisture - 1))
        }
        return false
    }

    /**
     * Get ID
     *
     * @return
     */
    fun getID(): Int = this.id

    /**
     * Get tile type
     *
     * @return
     */
    fun getTileType(): TileType = this.tileType

    /**
     * Get coordinate
     *
     * @return
     */
    fun getCoordinate(): Coordinate = this.coordinate

    /**
     * Get direction
     *
     * @return
     */
    fun getDirection(): Direction = this.direction

    /**
     * Get airflow
     *
     * @return
     */
    fun getAirflow(): Boolean = this.airflow

    /**
     * Shed exists
     *
     * @return
     */
    fun shedExists(): Boolean = this.shedExists

    /**
     * Get growable
     *
     * @return
     */
    fun getGrowable(): Growable? = this.growable

    /**
     * Change tile to village
     *
     */
    fun changeToVillage() {
        tileType = TileType.VILLAGE
        airflow = false
        direction = Direction.NONE
        shedExists = false
        growable = null
    }

    /**
     * Calls Sub Methods Can I Be ... depending on Action
     *
     * @param currentTick
     * @param yearTick
     * @param action
     * @return
     */
    fun canIBe(currentTick: Int, yearTick: Int, action: Action): Boolean {
        return when (action) {
            Action.HARVESTING -> canIBeHarvested(currentTick, yearTick)
            Action.WEEDING -> canIBeWeeded(currentTick)
            Action.CUTTING -> canIBeCut(currentTick, yearTick)
            Action.MOWING -> canIBeMowed(currentTick, yearTick)
            Action.IRRIGATING -> canIBeIrrigated(currentTick)
            else -> return true
        }
    }

    /**
     * Returns if this tile was worked on this tick
     * @param currentTick as int
     * @return if it matches the lastworked on tick
     *
     */

    fun checkWorkedOnThisTick(currentTick: Int): Boolean {
        return (this.growable?.getLastTickWorkedOn() ?: -1) == currentTick
    }

    /**
     * Is this tile permanently disabled by DROUGHT
     *
     * @return
     */
    fun amIPermanentlyDisabled(): Boolean {
        val g = growable ?: return false
        return g.getPermanentDisabled()
    }
}
