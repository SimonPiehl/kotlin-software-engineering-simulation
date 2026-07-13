package de.unisaarland.cs.se.selab.util

/**
 * Duration
 *
 * @property startTick
 * @property endTick
 * @constructor Create empty Duration
 */
class Duration(private val startTick: Int, private val endTick: Int) {
    /**
     * Get end tick
     *
     * @return
     */
    fun getEndTick(): Int = endTick

    /**
     * Get int representation
     *
     * @return
     */
    fun getIntRepresentation(): Int {
        return endTick - startTick + 1
    }

    /**
     * Ends: Determines if we reached the maxTick of duration in the currentTick
     *
     * @param currentTick
     * @return
     */
    fun ends(currentTick: Int): Boolean {
        // >= as max Tick is not included in Duration as specified on p.12
        return currentTick >= endTick
    }
}
