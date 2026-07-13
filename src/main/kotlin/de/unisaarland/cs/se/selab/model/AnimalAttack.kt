package de.unisaarland.cs.se.selab.model

/**
 * Represents an animal attack incident in the simulation.
 *
 * @property location the ID of the tile where the animal attack occurs
 * @property radius the radius around the affected tile that is impacted by the attack
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class AnimalAttack(
    id: Int,
    tick: Int,
    private val location: Int,
    private val radius: Int,
) : Incident(id, tick) {

    /**
     * Returns the location (tile ID) of the animal attack.
     *
     * @return the tile ID where the attack occurs
     */
    fun getLocation(): Int = this.location

    /**
     * Returns the radius around the location affected by the attack.
     *
     * @return the radius of effect in tiles
     */
    fun getRadius(): Int = this.radius

    override fun toString(): String = "ANIMAL_ATTACK"
}
