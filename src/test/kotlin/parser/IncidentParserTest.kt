package parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BeeHappy
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.parser.IncidentParser
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IncidentParserTest {

    private lateinit var simulationData: SimulationData
    private lateinit var map: Map
    private lateinit var tile: Tile
    private lateinit var parser: IncidentParser

    @BeforeEach
    fun setUp() {
        simulationData = mock()
        map = mock()
        tile = mock()

        whenever(simulationData.getMap()).thenReturn(map)
        whenever(simulationData.getMaxTick()).thenReturn(100)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(0, 0))

        parser = IncidentParser(simulationData)
    }

    @Test
    fun `no unique ID for Incidents`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 1)
            put(ParserConstants.KEY_TYPE, "CLOUD_CREATION")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
            put(ParserConstants.KEY_AMOUNT, 5000)
        }

        val existingIncident = CloudCreation(
            id = 1,
            tick = 2,
            duration = 4,
            location = 0,
            radius = 1,
            amount = 50000
        )
        whenever(simulationData.getAllIncidents()).thenReturn(mutableListOf(existingIncident))

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }

    @Test
    fun `CloudCreation successfully parsed`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 3)
            put(ParserConstants.KEY_TYPE, "CLOUD_CREATION")
            put(ParserConstants.KEY_TICK, 5)
            put(ParserConstants.KEY_DURATION, 10)
            put(ParserConstants.KEY_LOCATION, 1)
            put(ParserConstants.KEY_RADIUS, 2)
            put(ParserConstants.KEY_AMOUNT, 100)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val result = parser.parseOneIncident(json)

        assertTrue(result)
        verify(simulationData).addIncident(any<CloudCreation>())
    }

    @Test
    fun `AnimalAttack failed`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 4)
            put(ParserConstants.KEY_TYPE, "ANIMAL_ATTACK")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
        }

        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val affectedTile = Tile(
            id = 0,
            tileType = TileType.FIELD,
            coordinate = Coordinate(0, 0),
            airflow = false,
            direction = Direction.NONE,
            shedExists = false,
            growable = null
        )

        val otherTile = Tile(
            id = 1,
            tileType = TileType.FIELD,
            coordinate = Coordinate(1, 0),
            airflow = false,
            direction = Direction.NONE,
            shedExists = false,
            growable = null
        )

        whenever(map.getTileByID(0)).thenReturn(affectedTile)
        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf(otherTile))

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }

    @Test
    fun `AnimalAttack successfully`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 5)
            put(ParserConstants.KEY_TYPE, "ANIMAL_ATTACK")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(map.getTileByID(0)).thenReturn(tile)
        val forestTile = mock<Tile>().apply { whenever(getTileType()).thenReturn(TileType.FOREST) }
        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf(forestTile))

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<AnimalAttack>())
    }

    @Test
    fun `BeeHappy successfully`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 6)
            put(ParserConstants.KEY_TYPE, "BEE_HAPPY")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
            put(ParserConstants.KEY_EFFECT, 10)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(map.getTileByID(0)).thenReturn(tile)
        val meadowTile = mock<Tile>().apply { whenever(getTileType()).thenReturn(TileType.MEADOW) }
        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf(meadowTile))

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<BeeHappy>())
    }

    @Test
    fun `Drought successfully`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 7)
            put(ParserConstants.KEY_TYPE, "DROUGHT")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<Drought>())
    }

    @Test
    fun `no machine for BrokenMachine`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 8)
            put(ParserConstants.KEY_TYPE, "BROKEN_MACHINE")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_MACHINE_ID, 999)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(simulationData.findMachineById(999)).thenReturn(null)

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }

    @Test
    fun `BrokenMachine successfully`() {
        val machine = mock<Machine>()
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 9)
            put(ParserConstants.KEY_TYPE, "BROKEN_MACHINE")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_DURATION, 5)
            put(ParserConstants.KEY_MACHINE_ID, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(simulationData.findMachineById(1)).thenReturn(machine)

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<BrokenMachine>())
    }

    @Test
    fun `CityExpansion successfully`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 10)
            put(ParserConstants.KEY_TYPE, "CITY_EXPANSION")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<CityExpansion>())
    }

    @Test
    fun `Duration -1 for CloudCreation`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 11)
            put(ParserConstants.KEY_TYPE, "CLOUD_CREATION")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_DURATION, -1)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
            put(ParserConstants.KEY_AMOUNT, 10)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<CloudCreation>())
    }

    @Test
    fun `No Forest for AnimalAttack results in failure`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 12)
            put(ParserConstants.KEY_TYPE, "ANIMAL_ATTACK")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 42)
            put(ParserConstants.KEY_RADIUS, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(map.getTileByID(42)).thenReturn(null)

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }

    @Test
    fun `BeeHappy if no meadow nearby returns false`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 13)
            put(ParserConstants.KEY_TYPE, "BEE_HAPPY")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_LOCATION, 0)
            put(ParserConstants.KEY_RADIUS, 1)
            put(ParserConstants.KEY_EFFECT, 5)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(map.getTileByID(0)).thenReturn(tile)
        val otherTile = Tile(
            id = 1,
            tileType = TileType.FIELD,
            coordinate = Coordinate(1, 0),
            airflow = false,
            direction = Direction.NONE,
            shedExists = false,
            growable = null
        )
        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf(otherTile))

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }

    @Test
    fun `Duration -1 for BrokenMachine`() {
        val machine = mock<Machine>()
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 14)
            put(ParserConstants.KEY_TYPE, "BROKEN_MACHINE")
            put(ParserConstants.KEY_TICK, 0)
            put(ParserConstants.KEY_DURATION, -1)
            put(ParserConstants.KEY_MACHINE_ID, 1)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())
        whenever(simulationData.findMachineById(1)).thenReturn(machine)

        val result = parser.parseOneIncident(json)
        assertTrue(result)
        verify(simulationData).addIncident(any<BrokenMachine>())
    }

    @Test
    fun `Missing keys result in fail`() {
        val json = JSONObject().apply {
            put(ParserConstants.KEY_ID, 15)
            put(ParserConstants.KEY_TYPE, "CITY_EXPANSION")
            put(ParserConstants.KEY_TICK, 0)
        }
        whenever(simulationData.getAllIncidents()).thenReturn(emptyList())

        val result = parser.parseOneIncident(json)
        assertFalse(result)
        verify(simulationData, never()).addIncident(any())
    }
}
