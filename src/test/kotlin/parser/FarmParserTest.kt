
package parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FarmParserTest {

    private lateinit var simulationData: SimulationData
    private lateinit var map: Map
    private lateinit var parser: FarmParser

    @BeforeEach
    fun setUp() {
        simulationData = mock()
        map = mock()
        whenever(simulationData.getMap()).thenReturn(map)
        parser = FarmParser(simulationData)
    }

    @Test
    fun `parseOneFarm should return false for invalid farm ID`() {
        val json = JSONObject().apply {
            put("id", -1)
            put("name", "Farm1")
            put("farmsteads", JSONArray(listOf(1)))
            put(ParserConstants.FIELDS, JSONArray(listOf(2)))
            put("plantations", JSONArray())
            put("machines", JSONArray())
            put("sowingPlans", JSONArray())
        }

        val result = parser.parseOneFarm(json)
        assertFalse(result)
    }

    @Test
    fun `parseOneFarm should return true for valid minimal farm`() {
        val farmsteads = mock<Tile> { on { getID() } doReturn 1 }
        val fields = mock<Tile> { on { getID() } doReturn 2 }
        whenever(map.getFarmFarmsteadsByID(1)).thenReturn(listOf(farmsteads))
        whenever(map.getFieldsOfFarm(1)).thenReturn(mutableListOf(fields))
        whenever(map.getFarmPlantationsByID(1)).thenReturn(emptyList())

        val json = JSONObject().apply {
            put("id", 1)
            put("name", "Farm1")
            put("farmsteads", JSONArray(listOf(1)))
            put(ParserConstants.FIELDS, JSONArray(listOf(2)))
            put("plantations", JSONArray())
            put("machines", JSONArray())
            put("sowingPlans", JSONArray())
        }

        val result = parser.parseOneFarm(json)
        assertTrue(result)
    }

    @Test
    fun `parseOneMachine should succeed with valid fields`() {
        val machineTile = mock<Tile> { on { getCoordinate() } doReturn Coordinate(0, 0) }
        whenever(map.getTileByID(1)).thenReturn(machineTile)
        whenever(map.doesTileExist(1)).thenReturn(true)
        val farmsteadTile = mock<Tile> {
            on { getID() } doReturn 1
            on { shedExists() } doReturn true
        }
        whenever(map.getFarmFarmsteadsByID(1)).thenReturn(listOf(farmsteadTile))

        val machineJSON = JSONObject().apply {
            put("id", 1)
            put("name", "Tractor")
            put("duration", 5)
            put("location", 1)
            put("actions", JSONArray(listOf("SOWING")))
            put("plants", JSONArray(listOf("POTATO")))
        }

        val result = parser.parseOneMachine(machineJSON, 1)
        assert(result.isSuccess)
    }

    @Test
    fun `parseOnePlan should fail for invalid plant or missing fields`() {
        val planJSON = JSONObject().apply {
            put("id", 1)
            put("tick", 0)
            put("plant", "INVALID_PLANT")
            put(ParserConstants.FIELDS, JSONArray(listOf(1)))
        }

        val result = parser.parseOnePlan(planJSON, 1)
        assert(result.isFailure)
    }
}
