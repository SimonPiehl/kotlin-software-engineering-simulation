package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.util.IncidentType

/**
 * Growable
 *
 * @property currentPlant - Plant that is currently grows
 * @property possiblePlants - possible Plants it can hold, only for fields
 * @property maxMoisture - max amount of moisture the field/ plantation can hold
 * @constructor Creates Growable Component, either for fields or plantations
 */
class Growable(
    private var currentPlant: PlantType?,
    private val possiblePlants: List<PlantType>,
    private val maxMoisture: Int
) {
    private var cropsExpected: Int = 0
    private var moistureExposure: Int = maxMoisture
    private var sunlightExposureCurrentTick: Int = 0
    private var wasHarvestedAtTick: Int = -1
    private var wasSowedAtTick: Int = -1
    private val wasWeededAtTick: MutableList<Int> = mutableListOf()
    private var wasCutAtTick: Int = -1
    private var wasMowedAtTick: Int = -1
    private var liesFallowSinceTick: Int = GeneralConstants.DEFAULT_FALLOW_VAL
    private val changedInTickByIncident: MutableList<IncidentType> = mutableListOf()
    private var permanentDisabled: Boolean = false

    private var lastTickWorkedOn = -1

    /**
     * Constructor for Plantation Tiles
     */
    constructor(currentPlant: PlantType, maxMoisture: Int) : this(currentPlant, emptyList(), maxMoisture) {
        this.cropsExpected = currentPlant.idealEstimate
    }

    /**
     * Constructor for Field Tiles
     */
    constructor(possiblePlants: List<PlantType>, maxMoisture: Int) : this(null, possiblePlants, maxMoisture)

    /**
     * Set was done for all actions do make it general for action continuation
     */
    fun wasAtTick(action: Action, tick: Int) {
        when (action) {
            Action.HARVESTING -> this.setWasHarvestedAtTick(tick)
            Action.SOWING -> this.setWasSowedAtTick(tick)
            Action.WEEDING -> this.addWasWeededAtTick(tick)
            Action.CUTTING -> this.setWasCutAtTick(tick)
            Action.MOWING -> this.setWasMowedAtTick(tick)
            else -> return
        }
    }

    /**
     * Get incidents this tick
     *
     * @return
     */
    fun getIncidentsThisTick(): List<IncidentType> {
        return changedInTickByIncident
    }

    /**
     * Add incident this tick
     *
     * @param incidentType
     */
    fun addIncidentThisTick(incidentType: IncidentType) {
        changedInTickByIncident.add(incidentType)
    }

    /**
     * Get current plant that grows here
     *
     * @return
     */
    fun getCurrentPlant(): PlantType? = currentPlant

    /**
     * Get all plants that can possibly grow here
     *
     * @return
     */
    fun getPossiblePlants(): List<PlantType> = possiblePlants

    /**
     * Get max moisture the tile can hold
     *
     * @return capacity of tile
     */
    fun getMaxMoisture(): Int = maxMoisture

    /**
     * Get current moisture exposure
     *
     * @return
     */
    fun getMoistureExposure(): Int = moistureExposure

    /**
     * Get sunlight exposure in the current tick
     *
     * @return
     */
    fun getSunlightExposureCurrentTick(): Int = sunlightExposureCurrentTick

    /**
     * Get was harvested at tick
     *
     * @return last tick the growable was harvested
     */
    fun getWasHarvestedAtTick(): Int = wasHarvestedAtTick

    /**
     * Get was sowed at tick
     *
     * @return last tick the growable was sowed
     */
    fun getWasSowedAtTick(): Int = wasSowedAtTick

    /**
     * Get was weeded at tick
     *
     * @return last tick the growable was weeded
     */
    fun getWasWeededAtTick() = wasWeededAtTick

    /**
     * Set allTicks we weeded at
     *
     * @param list
     */
    fun setWasWeededAtTick(list: MutableList<Int>) {
        this.wasWeededAtTick.clear()
        this.wasWeededAtTick.addAll(list)
    }

    /**
     * Get was cut at tick
     *
     * @return
     */
    fun getWasCutAtTick(): Int = wasCutAtTick

    /**
     * Get was mowed at tick
     *
     * @return
     */
    fun getWasMowedAtTick(): Int = wasMowedAtTick

    /**
     * Get lies fallow since tick
     *
     * @return
     */
    fun getLiesFallowSinceTick(): Int = liesFallowSinceTick

    /**
     * Get permanent disabled
     *
     * @return
     */
    fun getPermanentDisabled(): Boolean = permanentDisabled

    /**
     * Get crops expected
     *
     * @return
     */
    fun getCropsExpected(): Int = cropsExpected

    // Setter

    /**
     * Set crops expected
     *
     * @param cropsExpected
     */
    fun setCropsExpected(cropsExpected: Int) {
        this.cropsExpected = cropsExpected
    }

    /**
     * Set moisture exposure
     *
     * @param moistureExposure
     */
    fun setMoistureExposure(moistureExposure: Int) {
        this.moistureExposure = moistureExposure
    }

    /**
     * Set sunlight exposure
     *
     * @param sunlightExposureCurrentTick
     */
    fun setSunlightExposure(sunlightExposureCurrentTick: Int) {
        this.sunlightExposureCurrentTick = sunlightExposureCurrentTick
    }

    /**
     * Set current plant and according estimate
     *
     * @param plant
     */
    fun setCurrentPlant(plant: PlantType?) {
        this.currentPlant = plant
        this.cropsExpected = plant?.idealEstimate ?: 0
    }

    /**
     * Set permanent disabled
     *
     * @param permanentDisabled
     */
    fun setPermanentDisabled(permanentDisabled: Boolean) {
        this.permanentDisabled = permanentDisabled
    }

    /**
     * Set lies fallow since tick and worked on extra for redundancy :)
     *
     * @param tick
     */
    fun setLiesFallowSinceTick(tick: Int) {
        this.liesFallowSinceTick = tick
    }

    /**
     * since on irrigation field we just change last worked on
     */
    fun setWasIrrigatedAtTick(tick: Int) {
        this.lastTickWorkedOn = tick
    }

    /**
     * Set was mowed at tick and worked on
     *
     * @param tick
     */
    fun setWasMowedAtTick(tick: Int) {
        this.wasMowedAtTick = tick
        this.lastTickWorkedOn = tick
    }

    /**
     * Set was cut at tick and worked on
     *
     * @param tick
     */
    fun setWasCutAtTick(tick: Int) {
        this.wasCutAtTick = tick
        this.lastTickWorkedOn = tick
    }

    /**
     * Set was weeded at tick and worked on
     *
     * @param tick
     */
    fun addWasWeededAtTick(tick: Int) {
        this.wasWeededAtTick.add(tick)
        this.lastTickWorkedOn = tick
    }

    /**
     * Set was sowed at tick and worked on
     *
     * @param tick
     */
    fun setWasSowedAtTick(tick: Int) {
        this.wasSowedAtTick = tick
        this.lastTickWorkedOn = tick
    }

    /**
     *  Set the last tick worked on to prevent double working per tick
     *
     */
    fun setLastTickWorkedOn(tick: Int) {
        this.lastTickWorkedOn = tick
    }

    /**
     * Set was harvested at tick and other fields that make sense
     *
     * @param tick
     */
    fun setWasHarvestedAtTick(tick: Int) {
        this.wasHarvestedAtTick = tick
        this.liesFallowSinceTick = tick
        this.lastTickWorkedOn = tick
    }

    /**
     * Reset incidents this tick
     *
     */
    fun resetIncidentsThisTick() {
        this.changedInTickByIncident.clear()
    }

    /**
     * Get last tick worked on to verify if the tile can still beworked on
     *
     * @return
     */
    fun getLastTickWorkedOn(): Int {
        return lastTickWorkedOn
    }
}
