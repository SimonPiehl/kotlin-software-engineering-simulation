package de.unisaarland.cs.se.selab.model

/**
 * Represents a drought incident in the simulation.
 *
 * @property location the ID of the tile where the drought occurs
 * @property radius the radius around the tile affected by the drought
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class Drought(
    id: Int,
    tick: Int,
    private val location: Int,
    private val radius: Int
) : Incident(id, tick) {

    /**
     * Returns the location (tile ID) where the drought occurs.
     *
     * @return the tile ID of the location
     */
    fun getLocation(): Int = this.location

    /**
     * Returns the radius affected by the drought.
     *
     * @return the radius in tiles
     */
    fun getRadius(): Int = this.radius

    override fun toString(): String {
        return "DROUGHT"
    }
}
