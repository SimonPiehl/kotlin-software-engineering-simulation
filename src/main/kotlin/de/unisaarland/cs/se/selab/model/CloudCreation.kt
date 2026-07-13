package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Duration

/**
 * Represents a cloud creation incident in the simulation.
 *
 * @property duration the duration of the cloud effect
 * @property location the ID of the tile where the cloud is created
 * @property radius the radius around the tile affected by the cloud
 * @property amount the total amount of water the cloud contains
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class CloudCreation(
    id: Int,
    tick: Int,
    private val duration: Int,
    private val location: Int,
    private val radius: Int,
    private val amount: Int
) : Incident(id, tick) {

    /**
     * Returns the duration of the cloud.
     *
     * @return the [Duration] object
     */
    fun getDuration(): Int = this.duration

    /**
     * Returns the total amount of water in the cloud.
     *
     * @return the amount of water
     */
    fun getAmount(): Int = this.amount

    /**
     * Returns the radius affected by the cloud.
     *
     * @return the radius in tiles
     */
    fun getRadius(): Int = this.radius

    /**
     * Returns the location (tile ID) where the cloud is created.
     *
     * @return the tile ID of the location
     */
    fun getLocation(): Int = this.location

    override fun toString(): String {
        return "CLOUD_CREATION"
    }
}
