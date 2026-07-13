package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.GeneralConstants

/**
 * Machine
 *
 * @property id
 * @property name
 * @property actions all Actions the machine can perform
 * @property possiblePlants all plants the machine can work on
 * @property duration how many days the machine needs for one action
 * @property locationOfShed Coordinates of his shed
 * @constructor Create Machine
 */
class Machine(
    private val id: Int,
    private val name: String,
    private val actions: List<Action>,
    private val possiblePlants: List<PlantType>,
    private val duration: Int,
    private var locationOfShed: Coordinate
) {
    private var workedOnPlant: PlantType? = null
    private var performedAction: Action? = null
    private var amountLoadedThisTick: Int = 0
    private var isBroken: Boolean = false
    private var currentLocation: Coordinate = locationOfShed
    private var isDisabledPermanently: Boolean = false
    private var howManyDaysWorked: Int = 0

    /**
     * Can I still work
     *
     * @return
     */
    fun canIStillWork(): Boolean {
        return !isBroken && !isDisabledPermanently &&
            howManyDaysWorked + duration <= GeneralConstants.MACHINE_MAX_WORKING_DAYS
    }

    /**
     * Can I work
     *
     * @return
     */
    fun canIWork(): Boolean {
        return !isBroken && !isDisabledPermanently && howManyDaysWorked < 1
    }

    /**
     * If a machine did its duty
     */
    fun cannotworkanymore() {
        this.howManyDaysWorked = GeneralConstants.MACHINE_MAX_WORKING_DAYS
    }

    /**
     * Get is broken
     *
     * @return
     */
    fun getIsBroken(): Boolean {
        return isBroken
    }

    /**
     * Get ID
     *
     * @return
     */
    fun getID(): Int = this.id

    /**
     * Get name
     *
     * @return
     */
    fun getName(): String = this.name

    /**
     * Get possible actions
     *
     * @return
     */
    fun getPossibleActions(): List<Action> = this.actions

    /**
     * Get possible plants
     *
     * @return
     */
    fun getPossiblePlants(): List<PlantType> = this.possiblePlants

    /**
     * Get duration
     *
     * @return
     */
    fun getDuration(): Int = this.duration

    /**
     * Get worked on plant
     *
     * @return
     */
    fun getWorkedOnPlant(): PlantType? = workedOnPlant

    /**
     * Get performed action
     *
     * @return
     */
    fun getPerformedAction(): Action? = performedAction

    /**
     * Get worked this round
     *
     * @return
     */
    fun getWorkedThisRound(): Boolean = howManyDaysWorked > 0

    /**
     * Get amount loaded this tick
     *
     * @return
     */
    fun getAmountLoadedThisTick(): Int = amountLoadedThisTick

    /**
     * Reset
     *
     */
    fun reset() {
        howManyDaysWorked = 0
        performedAction = null
        workedOnPlant = null
        amountLoadedThisTick = 0
    }

    /**
     * Set amount loaded this tick
     *
     * @param amount
     */
    fun setAmountLoadedThisTick(amount: Int) {
        amountLoadedThisTick += amount
    }

    /**
     * Worked
     *
     */
    fun worked() {
        this.howManyDaysWorked += this.duration
    }

    /**
     * Set worked on plant
     *
     * @param plant
     */
    fun setWorkedOnPlant(plant: PlantType) {
        this.workedOnPlant = plant
    }

    /**
     * Set performed action
     *
     * @param action
     */
    fun setPerformedAction(action: Action) {
        this.performedAction = action
    }

    /**
     * Set is broken
     *
     * @param b
     */
    fun setIsBroken(b: Boolean) {
        this.isBroken = b
    }

    /**
     * Get location of shed
     *
     * @return
     */
    fun getLocationOfShed(): Coordinate {
        return locationOfShed
    }

    /**
     * Set location of shed
     *
     * @param location
     */
    fun setLocationOfShed(location: Coordinate) {
        this.locationOfShed = location
    }

    /**
     * Is disabled permanently
     *
     * @return
     */
    fun isDisabledPermanently(): Boolean = isDisabledPermanently

    /**
     * Set is disabled permanently
     *
     * @param isDisabled
     */
    fun setIsDisabledPermanently(isDisabled: Boolean) {
        this.isDisabledPermanently = isDisabled
    }

    /**
     * Set current location
     *
     * @param coordinate
     */
    fun setCurrentLocation(coordinate: Coordinate) {
        this.currentLocation = coordinate
    }

    /**
     * Get current location
     *
     * @return
     */
    fun getCurrentLocation(): Coordinate = currentLocation
}
