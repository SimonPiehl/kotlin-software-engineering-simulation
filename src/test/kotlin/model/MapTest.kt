package model

import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MapTest {

    private lateinit var map: Map
    private lateinit var fieldTile: Tile
    private lateinit var plantationTile: Tile
    private lateinit var farmsteadTile: Tile
    private lateinit var roadTile: Tile
    private lateinit var villageTile: Tile

    @BeforeEach
    fun setUp() {
        map = Map()

        fieldTile = Tile(
            1, TileType.FIELD, Coordinate(0, 0), false, Direction.NONE, false,
            Growable(PlantType.POTATO, 1000)
        )
        plantationTile = Tile(
            2, TileType.PLANTATION, Coordinate(1, 0), false, Direction.NONE, false,
            Growable(PlantType.APPLE, 500)
        )
        farmsteadTile = Tile(3, TileType.FARMSTEAD, Coordinate(0, 1), false, Direction.NONE, true, null)
        roadTile = Tile(4, TileType.ROAD, Coordinate(1, 1), false, Direction.NONE, false, null)
        villageTile = Tile(5, TileType.VILLAGE, Coordinate(2, 0), false, Direction.NONE, false, null)
    }

    @Test
    fun `test addTile with farmID adds to correct collections`() {
        val farmID = 1

        map.addTile(fieldTile, farmID)
        map.addTile(plantationTile, farmID)
        map.addTile(farmsteadTile, farmID)
        map.addTile(roadTile, farmID) // Should not be added to farm-specific maps
        map.addTile(villageTile, farmID) // Should not be added to farm-specific maps

        // Check tilesByCoordinate and tilesByID
        assertEquals(fieldTile, map.getTileByCoordinate(Coordinate(0, 0)))
        assertEquals(plantationTile, map.getTileByCoordinate(Coordinate(1, 0)))
        assertEquals(farmsteadTile, map.getTileByCoordinate(Coordinate(0, 1)))
        assertEquals(roadTile, map.getTileByCoordinate(Coordinate(1, 1)))
        assertEquals(villageTile, map.getTileByCoordinate(Coordinate(2, 0)))

        assertEquals(fieldTile, map.getTileByID(1))
        assertEquals(plantationTile, map.getTileByID(2))
        assertEquals(farmsteadTile, map.getTileByID(3))
        assertEquals(roadTile, map.getTileByID(4))
        assertEquals(villageTile, map.getTileByID(5))

        // Check farm-specific collections
        assertEquals(listOf(fieldTile), map.getFieldsOfFarm(farmID))
        assertEquals(listOf(plantationTile), map.getFarmPlantationsByID(farmID))
        assertEquals(listOf(farmsteadTile), map.getFarmFarmsteadsByID(farmID))
    }

    @Test
    fun `test addTile without farmID only adds to general collections`() {
        map.addTile(fieldTile, null)
        map.addTile(roadTile, null)

        // Should be in general collections
        assertTrue(map.doesTileExist(Coordinate(0, 0)))
        assertTrue(map.doesTileExist(Coordinate(1, 1)))
        assertEquals(fieldTile, map.getTileByID(1))
        assertEquals(roadTile, map.getTileByID(4))

        // Should not be in farm-specific collections
        assertEquals(emptyList<Tile>(), map.getFieldsOfFarm(1))
        assertEquals(emptyList<Tile>(), map.getFarmPlantationsByID(1))
        assertEquals(emptyList<Tile>(), map.getFarmFarmsteadsByID(1))
    }

    @Test
    fun `test removeTile removes from all collections`() {
        val farmID = 1
        map.addTile(fieldTile, farmID)
        map.addTile(plantationTile, farmID)
        map.addTile(farmsteadTile, farmID)

        // Remove field tile
        map.removeTile(fieldTile, farmID)

        assertFalse(map.doesTileExist(Coordinate(0, 0)))
        assertNull(map.getTileByID(1))
        assertEquals(emptyList<Tile>(), map.getFieldsOfFarm(farmID))
        assertEquals(listOf(plantationTile), map.getFarmPlantationsByID(farmID))
        assertEquals(listOf(farmsteadTile), map.getFarmFarmsteadsByID(farmID))
    }

    @Test
    fun `test doesTileExist with coordinate`() {
        map.addTile(fieldTile, 1)

        assertTrue(map.doesTileExist(Coordinate(0, 0)))
        assertFalse(map.doesTileExist(Coordinate(5, 5)))
    }

    @Test
    fun `test doesTileExist with ID`() {
        map.addTile(fieldTile, 1)

        assertTrue(map.doesTileExist(1))
        assertFalse(map.doesTileExist(999))
    }

    @Test
    fun `test getCoordinateByTileID`() {
        map.addTile(fieldTile, 1)

        assertEquals(Coordinate(0, 0), map.getCoordinateByTileID(1))
        assertNull(map.getCoordinateByTileID(999))
    }

    @Test
    fun `test getAllGrowable returns fields and plantations sorted by ID`() {
        val field1 = Tile(
            3,
            TileType.FIELD,
            Coordinate(0, 0),
            false,
            Direction.NONE,
            false,
            Growable(PlantType.POTATO, 1000)
        )
        val field2 = Tile(
            1,
            TileType.FIELD,
            Coordinate(1, 0),
            false,
            Direction.NONE,
            false,
            Growable(PlantType.WHEAT, 800)
        )
        val plantation1 = Tile(
            2,
            TileType.PLANTATION,
            Coordinate(0, 1),
            false,
            Direction.NONE,
            false,
            Growable(PlantType.APPLE, 500)
        )

        map.addTile(field1, 1)
        map.addTile(field2, 1)
        map.addTile(plantation1, 1)

        val growableTiles = map.getAllGrowable()

        assertEquals(3, growableTiles.size)
        // Should be sorted by ID: field2(1), plantation1(2), field1(3)
        assertEquals(1, growableTiles[0].getID())
        assertEquals(2, growableTiles[1].getID())
        assertEquals(3, growableTiles[2].getID())
    }

    @Test
    fun `test getTilesByCoordinates`() {
        map.addTile(fieldTile, 1)
        map.addTile(plantationTile, 1)
        map.addTile(farmsteadTile, 1)

        val coordinates = listOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(5, 5))
        val tiles = map.getTilesByCoordinates(coordinates)

        assertEquals(2, tiles.size)
        assertEquals(1, tiles[0].getID()) // fieldTile
        assertEquals(2, tiles[1].getID()) // plantationTile
    }

    @Test
    fun `test getFarmIDbyField`() {
        val farmID = 1
        map.addTile(fieldTile, farmID)

        assertEquals(farmID, map.getFarmIDbyField(fieldTile))
        assertNull(map.getFarmIDbyField(plantationTile)) // Not added yet
    }

    @Test
    fun `test getFarmIDbyTile`() {
        val farmID = 1
        map.addTile(fieldTile, farmID)
        map.addTile(plantationTile, farmID)
        map.addTile(farmsteadTile, farmID)

        assertEquals(farmID, map.getFarmIDbyTile(fieldTile))
        assertEquals(farmID, map.getFarmIDbyTile(plantationTile))
        assertEquals(farmID, map.getFarmIDbyTile(farmsteadTile))
        assertNull(map.getFarmIDbyTile(roadTile)) // Not added
    }

    @Test
    fun `test getAllTiles returns all tiles sorted by ID`() {
        val tile1 = Tile(5, TileType.FIELD, Coordinate(0, 0), false, Direction.NONE, false, null)
        val tile2 = Tile(1, TileType.FIELD, Coordinate(1, 0), false, Direction.NONE, false, null)
        val tile3 = Tile(3, TileType.FIELD, Coordinate(0, 1), false, Direction.NONE, false, null)

        map.addTile(tile1, 1)
        map.addTile(tile2, 1)
        map.addTile(tile3, 1)

        val allTiles = map.getAllTiles()

        assertEquals(3, allTiles.size)
        assertEquals(1, allTiles[0].getID())
        assertEquals(3, allTiles[1].getID())
        assertEquals(5, allTiles[2].getID())
    }

    @Test
    fun `test getAllTilesCopy returns copies of tiles`() {
        map.addTile(fieldTile, 1)

        val copies = map.getAllTilesCopy()

        assertEquals(1, copies.size)
        val copy = copies[0]

        // Should have same properties but be different object
        assertEquals(fieldTile.getID(), copy.getID())
        assertEquals(fieldTile.getTileType(), copy.getTileType())
        assertEquals(fieldTile.getCoordinate(), copy.getCoordinate())
        assertNotSame(fieldTile, copy)
    }

    @Test
    fun `test getFieldsOfFarm returns empty list for unknown farm`() {
        assertEquals(emptyList<Tile>(), map.getFieldsOfFarm(999))
    }

    @Test
    fun `test multiple farms with same tile types`() {
        val farm1Field = Tile(1, TileType.FIELD, Coordinate(0, 0), false, Direction.NONE, false, null)
        val farm2Field = Tile(2, TileType.FIELD, Coordinate(1, 0), false, Direction.NONE, false, null)

        map.addTile(farm1Field, 1)
        map.addTile(farm2Field, 2)

        assertEquals(listOf(farm1Field), map.getFieldsOfFarm(1))
        assertEquals(listOf(farm2Field), map.getFieldsOfFarm(2))
        assertEquals(emptyList<Tile>(), map.getFieldsOfFarm(3))
    }

    @Test
    fun `test removeTile with null farmID`() {
        map.addTile(roadTile, null)

        assertTrue(map.doesTileExist(Coordinate(1, 1)))
        map.removeTile(roadTile, null)
        assertFalse(map.doesTileExist(Coordinate(1, 1)))
    }
}
