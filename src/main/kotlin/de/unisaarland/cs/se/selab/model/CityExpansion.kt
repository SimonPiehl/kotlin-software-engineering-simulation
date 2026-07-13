package de.unisaarland.cs.se.selab.model

/**
 * Represents a city expansion incident in the simulation.
 *
 * @property location the ID of the tile where the city expansion occurs
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class CityExpansion(
    id: Int,
    tick: Int,
    private val location: Int
) : Incident(id, tick) {

    /**
     * Returns the location (tile ID) where the city expansion takes place.
     *
     * @return the tile ID of the location
     */
    fun getLocation(): Int = this.location

    override fun toString(): String {
        return "CITY_EXPANSION"
    }
}
