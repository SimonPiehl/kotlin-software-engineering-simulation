package niklas

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.parser.TileParser
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TileParserTest {
    private val coordinateSquare = Coordinate(1, 1)
    private val coordinateOctagonal = Coordinate(0, 0)
    private val data: SimulationData = mock()
    private val map: Map = mock()

    // private val TileParser = TileParser(data)
    // private val coordJSON: JSONObject = mock()
    // private val tileJSONObject: JSONObject = mock()

    @BeforeTest
    fun setup() {
        // parseOneTile
        /*
        whenever(tileJSONObject.getInt(ParserConstants.KEY_ID)).thenReturn(0)
        whenever(tileJSONObject.getString(ParserConstants.KEY_CATEGORY)).thenReturn("ROAD")
        whenever(tileJSONObject.has(ParserConstants.KEY_AIRFLOW)).thenReturn(true)
        whenever(tileJSONObject.getBoolean(ParserConstants.KEY_AIRFLOW)).thenReturn(true)
        whenever(tileJSONObject.has(ParserConstants.KEY_FARM)).thenReturn(false)
        whenever(tileJSONObject.has(ParserConstants.KEY_SHED)).thenReturn(false)


        // parseCoordinate
        whenever<JSONObject>(tileJSONObject.getJSONObject(ParserConstants.KEY_COORDINATES)).thenReturn(coordJSON)
        whenever(coordJSON.getInt(ParserConstants.KEY_X)).thenReturn(1)
        whenever(coordJSON.getInt(ParserConstants.KEY_Y)).thenReturn(1)

        // parseDirection
        whenever(tileJSONObject.has(ParserConstants.KEY_DIRECTION)).thenReturn(true)
        whenever(tileJSONObject.getString(ParserConstants.KEY_DIRECTION)).thenReturn("315")
         */

        // parseNonGrowableTile
        doNothing().whenever(data).addTile(any(), anyOrNull())

        // parseGrowableTile (won't be called until now)

        // validateCoordinateAndIDNotAlreadyTaken
        /*
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)
        */

        // validateCoordinate

        // validateTileShape (deleted because issues)

        // validateDirection (already done above)
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON and adds it to SimulationData")
    fun validateRoadTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "315")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON. Invalid. Is square but direction is WEST")
    fun validateRoadFalseDirection() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "270")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON. Invalid. One coordinate is even the other odd")
    fun validateRoadFalseCoordinate() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "315")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 2)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses MEADOW tile JSON and adds it to SimulationData")
    fun validateMeadowTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "MEADOW")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "225")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses FOREST tile JSON and adds it to SimulationData")
    fun validateForestTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "FOREST")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "225")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses FARMSTEAD tile JSON and adds it to SimulationData")
    fun validateFarmsteadTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_SHED, true)
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "FARMSTEAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "135")
            put(ParserConstants.KEY_FARM, 0)
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with owner 0 to SimulationData
        verify(data).addTile(any(), eq<Int?>(0))
    }

    @Test
    @DisplayName("TileParser parses FIELD tile JSON and adds it to SimulationData")
    fun validateFieldTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "FIELD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "180")
            put(ParserConstants.KEY_FARM, 0)
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
            put(
                ParserConstants.KEY_POSSIBLE_PLANTS,
                JSONArray().apply {
                    put("WHEAT")
                    put("POTATO")
                    put("OAT")
                    put("PUMPKIN")
                }
            )
            put(ParserConstants.KEY_CAPACITY, 10000)
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with owner 0 to SimulationData
        verify(data).addTile(any(), eq<Int?>(0))
    }

    @Test
    @DisplayName("TileParser parses PLANTATION tile JSON and adds it to SimulationData")
    fun validatePlantationTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "PLANTATION")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "90")
            put(ParserConstants.KEY_FARM, 0)
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
            put(ParserConstants.KEY_PLANT, "APPLE")
            put(ParserConstants.KEY_CAPACITY, 10000)
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with owner 0 to SimulationData
        verify(data).addTile(any(), eq<Int?>(0))
    }

    @Test
    @DisplayName("TileParser parses PLANTATION tile JSON and adds it to SimulationData. It has no airflow.")
    fun validatePlantationNoAirflow() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "PLANTATION")
            put(ParserConstants.KEY_AIRFLOW, false)
            put(ParserConstants.KEY_FARM, 0)
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
            put(ParserConstants.KEY_PLANT, "CHERRY")
            put(ParserConstants.KEY_CAPACITY, 10000)
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with owner 0 to SimulationData
        verify(data).addTile(any(), eq<Int?>(0))
    }

    @Test
    @DisplayName("TileParser parses PLANTATION tile JSON and adds it to SimulationData. It Almond.")
    fun validatePlantationTrueTwo() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "PLANTATION")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "0")
            put(ParserConstants.KEY_FARM, 0)
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
            put(ParserConstants.KEY_PLANT, "ALMOND")
            put(ParserConstants.KEY_CAPACITY, 10000)
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with owner 0 to SimulationData
        verify(data).addTile(any(), eq<Int?>(0))
    }

    @Test
    @DisplayName("TileParser parses square VILLAGE tile JSON and adds it to SimulationData")
    fun validateVillageSquareTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "VILLAGE")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses octagonal VILLAGE tile JSON and adds it to SimulationData")
    fun validateVillageOctagonalTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateOctagonal)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "VILLAGE")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 0)
                    put(ParserConstants.KEY_Y, 0)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON and adds it to SimulationData. Direction ist NORTHEAST")
    fun validateRoadNorthEastTrue() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "45")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertTrue(result)

        // Verify that TileParser added a Tile (any instance) with no owner (null) to SimulationData
        verify(data).addTile(any(), isNull())
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON and a Tile with the same coordinate is already in Map.")
    fun validateCoordinateAlreadyTakenInvalid() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(true)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "45")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses ROAD tile JSON and a Tile with the same id is already in Map.")
    fun validateIdAlreadyTakenInvalid() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(coordinateOctagonal)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "45")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses square ROAD tile JSON. Invalid: Direction ist NORTH")
    fun validateSquareNorthInvalid() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "0")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses square ROAD tile JSON. Invalid: Direction ist EAST")
    fun validateSquareEastInvalid() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "90")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }

    @Test
    @DisplayName("TileParser parses square ROAD tile JSON. Invalid: Direction ist NORTH")
    fun validateSquareSouthInvalid() {
        // for validateCoordinateAndIDNotAlreadyTaken
        whenever(data.getMap()).thenReturn(map)
        whenever(map.doesTileExist(coordinateSquare)).thenReturn(false)
        whenever(map.getCoordinateByTileID(0)).thenReturn(null)

        val tileJSONObject = JSONObject().apply {
            put(ParserConstants.KEY_ID, 0)
            put(ParserConstants.KEY_CATEGORY, "ROAD")
            put(ParserConstants.KEY_AIRFLOW, true)
            put(ParserConstants.KEY_DIRECTION, "180")
            put(
                ParserConstants.KEY_COORDINATES,
                JSONObject().apply {
                    put(ParserConstants.KEY_X, 1)
                    put(ParserConstants.KEY_Y, 1)
                }
            )
        }
        val tileParser = TileParser(data)
        val result = tileParser.parseOneTile(tileJSONObject)
        assertFalse(result)

        // Verify that TileParser DIDN'T ADD a Tile (any instance) to SimulationData
        verify(data, never()).addTile(any(), anyOrNull())
    }
}
