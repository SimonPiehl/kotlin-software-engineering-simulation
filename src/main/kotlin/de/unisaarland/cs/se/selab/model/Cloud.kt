package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Coordinate

/**
 * Cloud
 *
 * @property id
 * @property amount how much water the cloud contains
 * @property duration how many ticks the cloud is able to act
 * @property location current location Coordinate the cloud is on
 */
class Cloud(
    private var id: Int,
    private var amount: Int,
    private var duration: Int,
    private var location: Coordinate
) {

    /**
     * Get ID
     *
     * @return
     */
    fun getID(): Int = this.id

    /**
     * Get amount
     *
     * @return
     */
    fun getAmount(): Int = this.amount

    /**
     * Get duration
     *
     * @return
     */
    fun getDuration(): Int = this.duration

    /**
     * Get location
     *
     * @return
     */
    fun getLocation(): Coordinate = this.location

    /**
     * Set location
     *
     * @param coordinate
     */
    fun setLocation(coordinate: Coordinate) {
        this.location = coordinate
    }

    /**
     * Set i d
     *
     * @param id
     */
    fun setID(id: Int) {
        this.id = id
    }

    /**
     * Set amount
     *
     * @param amount
     */
    fun setAmount(amount: Int) {
        this.amount = amount
    }

    /**
     * Set duration
     *
     * @param duration
     */
    fun setDuration(duration: Int) {
        this.duration = duration
    }
}
