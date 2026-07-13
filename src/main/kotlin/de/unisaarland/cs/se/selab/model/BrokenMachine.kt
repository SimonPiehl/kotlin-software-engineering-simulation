package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Duration

/**
 * Represents a broken machine incident in the simulation, which temporarily disables a machine.
 *
 * @property duration the period during which the machine is broken
 * @property machine the [Machine] object affected by the incident
 * @param id the unique identifier of the incident
 * @param tick the tick at which the incident occurs
 */
class BrokenMachine(
    id: Int,
    tick: Int,
    private val duration: Duration,
    private val machine: Machine
) : Incident(id, tick) {

    /**
     * Returns the [Machine] affected by this incident.
     *
     * @return the machine
     */
    fun getMachine(): Machine = this.machine

    /**
     * Returns the [Duration] during which the machine is broken.
     *
     * @return the duration object
     */
    fun getDuration(): Duration = this.duration

    override fun toString(): String {
        return "BROKEN_MACHINE"
    }
}
