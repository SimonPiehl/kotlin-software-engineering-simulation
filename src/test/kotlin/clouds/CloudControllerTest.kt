package clouds

import de.unisaarland.cs.se.selab.controller.CloudController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Integration Tests for CloudController
 *
 */
class CloudControllerTest {
    val data = SimulationData(3, 1)
    val controller = CloudController(data)
    val standardCoordinate = Coordinate(1, 1)
    val standardDuration = 9

    @Test
    fun testCloudRainAllDownInstant() {
        val loc = Tile(1, TileType.ROAD, standardCoordinate, false, Direction.NONE, false, null)
        val cloud = Cloud(0, 5000, standardDuration, standardCoordinate)
        data.addCloud(cloud)
        data.addTile(loc, null)
        controller.cloudMovement()

        assertFalse(data.getClouds().contains(cloud))
        assertEquals(loc.getCoordinate(), cloud.getLocation())
        assertEquals(0, cloud.getAmount())
    }

    @Test
    fun testCloudDoNothing() {
        val loc = Tile(1, TileType.ROAD, standardCoordinate, false, Direction.NONE, false, null)
        val cloud = Cloud(0, 2000, standardDuration, standardCoordinate)
        data.addCloud(cloud)
        data.addTile(loc, null)
        controller.cloudMovement()

        assertTrue(data.getClouds().contains(cloud))
        assertEquals(loc.getCoordinate(), cloud.getLocation())
        assertEquals(2000, cloud.getAmount())
    }

    @Test
    fun testCloudDoNothingOnGrowable() {
        val g = Growable(PlantType.APPLE, 3000)
        g.setMoistureExposure(3000)
        val loc = Tile(0, TileType.PLANTATION, Coordinate(0, 0), true, Direction.SOUTHEAST, false, g)
        val cloud = Cloud(0, 10000, standardDuration, Coordinate(0, 0))
        data.addCloud(cloud)
        data.addTile(loc, null)
        controller.cloudMovement()

        assertTrue(data.getClouds().contains(cloud))
        assertEquals(loc.getCoordinate(), cloud.getLocation())
        assertEquals(10000, cloud.getAmount())
    }

    @Test
    fun testCloudMoveNoMergeThenRain() {
        val locTo = Tile(1, TileType.ROAD, standardCoordinate, false, Direction.NONE, false, null)
        val g = Growable(PlantType.APPLE, 3000)
        g.setMoistureExposure(3000)
        val loc = Tile(0, TileType.PLANTATION, Coordinate(0, 0), true, Direction.SOUTHEAST, false, g)
        val cloud = Cloud(0, 10000, standardDuration, standardCoordinate)
        data.addCloud(cloud)
        data.addTile(loc, null)
        data.addTile(locTo, null)
        controller.cloudMovement()

        assertFalse(data.getClouds().contains(cloud))
        assertEquals(locTo.getCoordinate(), cloud.getLocation())
        assertEquals(0, cloud.getAmount())
        assertEquals(3000, g.getMoistureExposure())
    }

    @Test
    fun testCloudMerge() {
        fun testCloudMoveNoMergeThenRain() {
            val locTo = Tile(1, TileType.ROAD, standardCoordinate, false, Direction.NONE, false, null)
            val g = Growable(PlantType.APPLE, 3000)
            g.setMoistureExposure(3000)
            val loc = Tile(0, TileType.PLANTATION, Coordinate(0, 0), true, Direction.SOUTHEAST, false, g)
            val cloud = Cloud(0, 1000, standardDuration, standardCoordinate)
            val cloudTo = Cloud(1, 1000, standardDuration, Coordinate(0, 0))
            data.addCloud(cloud)
            data.addCloud(cloudTo)
            data.addTile(loc, null)
            data.addTile(locTo, null)
            controller.cloudMovement()

            assertFalse(data.getClouds().contains(cloud))
            assertFalse(data.getClouds().contains(cloudTo))
            assertFalse(data.getClouds().none { it.getID() == 2 })
            val newCloud = data.getClouds().filter { it.getID() == 2 }[0]
            assertEquals(locTo.getCoordinate(), newCloud.getLocation())
            assertEquals(2000, newCloud.getAmount())
            assertEquals(3000, g.getMoistureExposure())
        }
        testCloudMoveNoMergeThenRain()
    }

    @Test
    fun testCloudRainingGrowable() {
        val grow = Growable(PlantType.APPLE, 3000)
        grow.setMoistureExposure(20)
        grow.setSunlightExposure(40)
        val grow2 = Growable(null, listOf(PlantType.OAT), 1000)
        grow2.setMoistureExposure(50)
        grow2.setSunlightExposure(40)
        val cord = Coordinate(2, 2)
        val loc = Tile(1, TileType.FIELD, cord, false, Direction.SOUTH, true, grow2)
        val loc2 = Tile(2, TileType.PLANTATION, Coordinate(2, 0), false, Direction.NONE, false, grow)
        val cloud = Cloud(0, 10000, standardDuration, cord)
        data.addCloud(cloud)
        data.addTile(loc, null)
        data.addTile(loc2, null)
        controller.cloudMovement()

        assertTrue(data.getClouds().contains(cloud))
        assertTrue(loc.getCoordinate().equals(cloud.getLocation()))
        assertEquals(9050, cloud.getAmount())
    }

    @Test
    fun testCloudTryMoveOnUnspecified() {
        val coo = Coordinate(2, 0)
        val loc = Tile(1, TileType.ROAD, coo, true, Direction.NORTH, false, null)
        data.addTile(loc, null)
        val cloud = Cloud(0, 10, standardDuration, coo)
        data.addCloud(cloud)
        // Do
        controller.cloudMovement()
        // Check
        assertEquals(coo, cloud.getLocation())
    }

    @Test
    fun testCloudOnVillage() {
        val coo = Coordinate(2, 0)
        val cooTo = Coordinate(2, -2)
        val loc = Tile(1, TileType.ROAD, coo, true, Direction.NORTH, false, null)
        val locTo = Tile(1, TileType.VILLAGE, cooTo, false, Direction.NONE, false, null)
        data.addTile(loc, null)
        data.addTile(locTo, null)
        val cloud = Cloud(0, 10, standardDuration, coo)
        data.addCloud(cloud)
        // Do
        controller.cloudMovement()
        // Check
        assertFalse(data.getClouds().contains(cloud))
    }
}
