package de.unisaarland.cs.se.selab.model

/**
 * Base class for all incidents in the simulation.
 *
 * @property id the unique identifier of the incident
 * @property tick the tick at which the incident occurs
 * @constructor Creates an incident with the given ID and tick
 */
sealed class Incident(private val id: Int, private val tick: Int) {

    /**
     * Returns the unique ID of the incident.
     *
     * @return the incident ID
     */
    fun getID(): Int = this.id

    /**
     * Returns the tick at which the incident occurs.
     *
     * @return the tick
     */
    fun getTick(): Int = this.tick
}
