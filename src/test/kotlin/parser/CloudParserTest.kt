package parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.parser.CloudParser
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CloudParserTest {

    private lateinit var simulationData: SimulationData
    private lateinit var map: de.unisaarland.cs.se.selab.model.Map
    private lateinit var tile: Tile
    private lateinit var parser: CloudParser

    @BeforeEach
    fun setUp() {
        simulationData = mock()
        map = mock()
        tile = mock()

        whenever(simulationData.getMap()).thenReturn(map)
        whenever(simulationData.getMaxTick()).thenReturn(100)

        parser = CloudParser(simulationData)
    }

    @Test
    fun `check successful parsing of a cloud`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_LOCATION, 0)
        }

        whenever(map.getCoordinateByTileID(0)).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByID(0)).thenReturn(tile)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(simulationData.getClouds()).thenReturn(mutableListOf())

        val result = parser.parseCloud(cloudJson)

        assertTrue(result)
        verify(simulationData).addCloud(any<Cloud>())
    }

    @Test
    fun `no cloud creation on village`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 1)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_LOCATION, 0)
        }

        whenever(map.getCoordinateByTileID(0)).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByID(0)).thenReturn(tile)
        whenever(tile.getTileType()).thenReturn(TileType.VILLAGE)
        whenever(simulationData.getClouds()).thenReturn(mutableListOf())

        val result = parser.parseCloud(cloudJson)

        assertFalse(result)
        verify(simulationData, never()).addCloud(any<Cloud>())
    }

    @Test
    fun `duration parsed when -1`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_DURATION, -1)
            put(ParserConstants.KEY_LOCATION, 0)
        }

        whenever(map.getCoordinateByTileID(0)).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByID(0)).thenReturn(tile)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(simulationData.getClouds()).thenReturn(mutableListOf())

        val result = parser.parseCloud(cloudJson)

        assertTrue(result)
        verify(simulationData).addCloud(any<Cloud>())
    }

    @Test
    fun `invalid keyset`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_LOCATION, 0)
            put("farm", 1)
        }

        val result = parser.parseCloud(cloudJson)

        assertFalse(result)
        verify(simulationData, never()).addCloud(any<Cloud>())
    }

    @Test
    fun `no creation on non-existing tile`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_LOCATION, 0)
        }

        whenever(map.getCoordinateByTileID(0)).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByID(0)).thenReturn(null)
        whenever(simulationData.getClouds()).thenReturn(mutableListOf())

        val result = parser.parseCloud(cloudJson)
        assertFalse(result)
    }

    @Test
    fun `cloud parsing Test more clouds`() {
        val cloudJson = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_AMOUNT, 5000)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_LOCATION, 0)
        }

        val existingCloud1 = mock<Cloud>()
        val existingCloud2 = mock<Cloud>()
        whenever(existingCloud1.getID()).thenReturn(1)
        whenever(existingCloud2.getID()).thenReturn(2)
        whenever(simulationData.getClouds()).thenReturn(mutableListOf(existingCloud1, existingCloud2))
        whenever(map.getCoordinateByTileID(0)).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByID(0)).thenReturn(tile)
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)

        val result = parser.parseCloud(cloudJson)
        assertTrue(result)
        verify(simulationData).addCloud(any<Cloud>())
    }
}
