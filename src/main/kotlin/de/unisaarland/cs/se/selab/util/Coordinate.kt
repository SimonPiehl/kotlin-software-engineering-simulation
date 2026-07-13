package de.unisaarland.cs.se.selab.util

/**
 * Coordinate
 *
 * @property x
 * @property y
 * @constructor Create empty Coordinate
 */
class Coordinate(
    private val x: Int,
    private val y: Int,
) {
    /**
     * Is coordinate of octagonal tile
     *
     * @return
     */
    fun isCoordinateOfOctagonalTile(): Boolean {
        return x % 2 == 0
    }

    /**
     * Is coordinate of square tile
     *
     * @return
     */
    fun isCoordinateOfSquareTile(): Boolean {
        return kotlin.math.abs(x % 2) == 1
    }

    /**
     * Get x
     *
     * @return
     */
    fun getX(): Int {
        return x
    }

    /**
     * Get y
     *
     * @return
     */
    fun getY(): Int {
        return y
    }

    /**
     * Get neighbours
     *
     * @param radius
     * @return
     */
    fun getNeighbours(radius: Int): List<Coordinate> {
        if (radius <= 0) return emptyList()
        val visited = mutableSetOf(Coordinate(this.x, this.y))
        val queue = ArrayDeque<Coordinate>().apply { add(Coordinate(x, y)) }
        val result = mutableSetOf<Coordinate>()
        var steps = 0

        while (steps < radius && queue.isNotEmpty()) {
            val levelSize = queue.size
            repeat(levelSize) {
                val current = queue.removeFirst()

                for (direction in Direction.entries) {
                    val neighbor = current.getNeighborByDirection(direction) ?: continue

                    if (visited.add(neighbor)) {
                        result.add(neighbor)
                        queue.add(neighbor)
                    }
                }
            }
            steps++
        }

        return result.toList()
    }

    /**
     * Get neighbor by direction
     *
     * @param direction
     * @return
     */
    fun getNeighborByDirection(direction: Direction): Coordinate? {
        var result: Coordinate?
        // If coordinate of okta there are neighbor coordinates in direction NORTH, EAST, SOUTH, WEST
        if (!isCoordinateOfSquareTile()) {
            result = when (direction) {
                Direction.NORTH -> Coordinate(x, y - 2)
                Direction.EAST -> Coordinate(x + 2, y)
                Direction.SOUTH -> Coordinate(x, y + 2)
                Direction.WEST -> Coordinate(x - 2, y)
                else -> null
            }
        } // If coordinate of square there are no neighbor coordinates in direction NORTH, EAST, SOUTH, WEST
        else {
            result = when (direction) {
                Direction.NORTH -> return null
                Direction.EAST -> return null
                Direction.SOUTH -> return null
                Direction.WEST -> return null
                else -> null
            }
        }
        // For all these directions there are neighbor coordinates for both types
        result = when (direction) {
            Direction.NORTHEAST -> Coordinate(x + 1, y - 1)
            Direction.SOUTHEAST -> Coordinate(x + 1, y + 1)
            Direction.NORTHWEST -> Coordinate(x - 1, y - 1)
            Direction.SOUTHWEST -> Coordinate(x - 1, y + 1)
            else -> result
        }
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (other !is Coordinate) {
            return false
        }
        return this.x == other.x && this.y == other.y
    }
    override fun hashCode(): Int {
        return this.x.hashCode() + this.y.hashCode()
    }
}
