package de.unisaarland.cs.se.selab.model

/**
 * Represents a bee happy incident in the simulation, which increases pollination
 * in a certain area around a tile.
 *
 * @property location the ID of the tile where the effect occurs
 * @property radius the radius around the tile affected by the bees
 * @property effect the strength of the pollination effect on the harvest
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class BeeHappy(
    id: Int,
    tick: Int,
    private val location: Int,
    private val radius: Int,
    private val effect: Int,
) : Incident(id, tick) {

    /**
     * Returns the strength of the pollination effect.
     *
     * @return the effect value
     */
    fun getEffect(): Int = this.effect

    /**
     * Returns the location (tile ID) where the bee happy incident occurs.
     *
     * @return the tile ID
     */
    fun getLocation(): Int = this.location

    /**
     * Returns the radius around the location affected by the incident.
     *
     * @return the radius in tiles
     */
    fun getRadius(): Int = this.radius

    override fun toString(): String {
        return "BEE_HAPPY"
    }
}
