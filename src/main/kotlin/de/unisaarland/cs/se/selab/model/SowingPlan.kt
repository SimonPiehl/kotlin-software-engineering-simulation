package de.unisaarland.cs.se.selab.model

/**
 * Sowing plan
 *
 * @property id
 * @property startsAtTick when the plan should be executed (ideal)
 * @property plantToSow which plant should be sowed
 * @property locations on which fields it has to be executed
 * @constructor Create Sowing plan
 */
class SowingPlan(
    private val id: Int,
    private val startsAtTick: Int,
    private val plantToSow: PlantType,
    private val locations: List<Int>
) {
    private var wasUsedInTick: Int = -1

    /**
     * returns the tick it was used in
     */
    fun getwasUsedInTick(): Int = this.wasUsedInTick

    /**
     * sets the tick that the plan was worked on
     */
    fun setWasUsedInTick(tick: Int) {
        this.wasUsedInTick = tick
    }

    /**
     * Get start tick
     *
     */
    fun getStartTick() = startsAtTick

    /**
     * Get plant to sow
     *
     */
    fun getPlantToSow() = plantToSow

    /**
     * Get locations
     *
     */
    fun getLocations() = locations

    /**
     * Get ID
     *
     */
    fun getID() = id
}
