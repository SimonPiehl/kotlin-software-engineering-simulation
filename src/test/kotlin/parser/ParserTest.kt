package parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.SowingPlan
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.parser.Parser
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse

class ParserTest {

    private lateinit var simulationData: SimulationData
    private lateinit var parser: Parser
    private lateinit var map: Map
    private lateinit var tile: Tile
    private lateinit var farm: Farm
    private lateinit var sowingPlan: SowingPlan

    @BeforeEach
    fun setUp() {
        simulationData = mock()
        map = mock()
        tile = mock()
        farm = mock()
        sowingPlan = mock()
        parser = Parser(simulationData)

        // Map-Mocks
        val tile0 = mock<Tile>()
        val tile1 = mock<Tile>()
        val tile3 = mock<Tile>()
        val tile4 = mock<Tile>()
        val tile5 = mock<Tile>()

        whenever(tile0.getID()).thenReturn(0)
        whenever(tile0.getTileType()).thenReturn(TileType.FARMSTEAD)
        whenever(tile0.getCoordinate()).thenReturn(mock())

        whenever(tile1.getID()).thenReturn(1)
        whenever(tile1.getTileType()).thenReturn(TileType.VILLAGE)
        whenever(tile1.getCoordinate()).thenReturn(mock())

        whenever(tile3.getID()).thenReturn(3)
        whenever(tile3.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(tile3.getCoordinate()).thenReturn(mock())

        whenever(tile4.getID()).thenReturn(4)
        whenever(tile4.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile4.getCoordinate()).thenReturn(mock())

        whenever(tile5.getID()).thenReturn(5)
        whenever(tile5.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile5.getCoordinate()).thenReturn(mock())

        val allTiles = listOf(tile0, tile1, tile3, tile4, tile5)
        whenever(map.getAllTiles()).thenReturn(allTiles)
        whenever(simulationData.getMap()).thenReturn(map)

        // Farm-Mocks
        whenever(simulationData.getFarms()).thenReturn(mutableListOf(farm))
        whenever(farm.getID()).thenReturn(0)
        whenever(farm.getName()).thenReturn("Farm1")
        whenever(farm.getPlans()).thenReturn(listOf(sowingPlan))
        whenever(sowingPlan.getID()).thenReturn(0)
        whenever(sowingPlan.getLocations()).thenReturn(listOf(5))
        whenever(sowingPlan.getPlantToSow()).thenReturn(mock())
    }

    @Test
    fun `parse should return false if map parsing fails`() {
        val mapJson = JSONObject() // empty JSON will fail schema
        val farmsJson = JSONObject()
        val scenarioJson = JSONObject()

        val result = parser.parse(mapJson, farmsJson, scenarioJson, "map.json", "farms.json", "scenario.json")
        assertFalse(result)
    }

    @Test
    fun `parse should return false if farms parsing fails`() {
        val mapJson = JSONObject().apply {
            put("tiles", JSONArray().put(JSONObject()))
        }
        val farmsJson = JSONObject() // empty JSON fails schema
        val scenarioJson = JSONObject()

        val result = parser.parse(mapJson, farmsJson, scenarioJson, "map.json", "farms.json", "scenario.json")
        assertFalse(result)
    }

    @Test
    fun `parse should return false if scenario parsing fails`() {
        val mapJson = JSONObject().apply {
            put("tiles", JSONArray().put(JSONObject()))
        }
        val farmsJson = JSONObject().apply {
            put("farms", JSONArray().put(JSONObject()))
        }
        val scenarioJson = JSONObject() // empty JSON fails schema

        val result = parser.parse(mapJson, farmsJson, scenarioJson, "map.json", "farms.json", "scenario.json")
        assertFalse(result)
    }

    @Test
    fun `parse should return false if farms have non-unique IDs`() {
        val mapJson = JSONObject().apply {
            put("tiles", JSONArray().put(JSONObject()))
        }
        val farmsJson = JSONObject().apply {
            put("farms", JSONArray().put(JSONObject()))
        }
        val scenarioJson = JSONObject().apply {
            put("clouds", JSONArray())
            put("incidents", JSONArray())
        }

        whenever(simulationData.getFarms()).thenReturn(mutableListOf(farm, farm)) // duplicate
        whenever(farm.getID()).thenReturn(1)
        whenever(farm.getName()).thenReturn("Farm1")
        whenever(farm.getPlans()).thenReturn(emptyList())
        whenever(map.getAllTiles()).thenReturn(listOf(tile))
        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile.getID()).thenReturn(1)
        whenever(tile.getCoordinate()).thenReturn(mock())

        val result = parser.parse(mapJson, farmsJson, scenarioJson, "map.json", "farms.json", "scenario.json")
        assertFalse(result)
    }
}
