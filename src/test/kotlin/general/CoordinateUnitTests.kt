package general

import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertTrue

class CoordinateUnitTests {
    private val coordinate = Coordinate(2, 2)

    @Test
    fun testForGetNeighborByDirection() {
        assertTrue(coordinate.getNeighborByDirection(Direction.NORTH)?.equals(Coordinate(2, 0)) ?: false)
    }

    @Test
    fun testForEveryNeighbor() {
        val neighborArray = coordinate.getNeighbours(1)
        assertTrue { !neighborArray.contains(coordinate) && neighborArray.contains(Coordinate(2, 0)) }
    }

    @Test
    fun testNeighborSimple() {
        val radius = 1
        val location = Coordinate(1, 1)
        val expected = listOf(Coordinate(0, 0), Coordinate(0, 2), Coordinate(2, 0), Coordinate(2, 2))
        assertTrue(location.getNeighbours(radius).containsAll(expected))
    }
}
