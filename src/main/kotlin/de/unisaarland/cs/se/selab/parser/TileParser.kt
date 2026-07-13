package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONObject

/**
 * Parses tiles from JSON and adds them to the simulation data.
 *
 * Responsible for parsing both growable and non-growable tiles
 * and storing them in the [SimulationData].
 */
class TileParser(private val simulationData: SimulationData) {

    /**
     * Parses a single tile (growable or non-growable) and adds it to the simulation data.
     *
     * @param tile the [JSONObject] containing information about a single tile
     * @return `true` if parsing was successful, `false` otherwise
     */
    fun parseOneTile(tile: JSONObject): Boolean {
        val tileType = TileType.valueOf(tile.getString(ParserConstants.KEY_CATEGORY))
        if (!validateJSONKeyset(tile, tileType)) return false
        val id = tile.getInt(ParserConstants.KEY_ID)
        val coordinate = parseCoordinate(tile)
        if (!validateTileBasics(coordinate, id, tileType)) return false
        val airflow = if (tile.has(ParserConstants.KEY_AIRFLOW)) tile.getBoolean(ParserConstants.KEY_AIRFLOW) else false
        val direction = parseDirection(tile)
        if (!validateDirection(coordinate, direction)) return false
        val owner = if (tile.has(ParserConstants.KEY_FARM)) tile.getInt(ParserConstants.KEY_FARM) else null
        val shedExists = if (!tile.has(ParserConstants.KEY_SHED)) false else tile.getBoolean(ParserConstants.KEY_SHED)

        val tileObject = Tile(id, tileType, coordinate, airflow, direction, shedExists, null)
        return when (tileType) {
            TileType.VILLAGE,
            TileType.MEADOW,
            TileType.ROAD,
            TileType.FARMSTEAD,
            TileType.FOREST -> parseNonGrowableTile(tileObject, owner)

            TileType.FIELD,
            TileType.PLANTATION -> parseGrowableTile(tile, tileObject, owner)
        }
    }

    /**
     * Validates the basic properties of a tile.
     *
     * @param coordinate the coordinate of the tile
     * @param id the unique tile ID
     * @param tileType the type of the tile
     * @return `true` if coordinate, ID, and shape are valid, `false` otherwise
     */
    private fun validateTileBasics(coordinate: Coordinate, id: Int, tileType: TileType): Boolean {
        return validateCoordinate(coordinate) &&
            validateCoordinateAndIDNotAlreadyTaken(coordinate, id) &&
            validateTileShape(coordinate, tileType)
    }

    /**
     * Validates whether the given JSON object contains the correct keys for the specified tile type.
     *
     * @param tile the JSON object to check
     * @param tileType the type of the tile
     * @return `true` if the key set matches the expected keys for the tile type, `false` otherwise
     */
    private fun validateJSONKeyset(tile: JSONObject, tileType: TileType): Boolean {
        val baseKeys = setOf(
            ParserConstants.KEY_ID,
            ParserConstants.KEY_CATEGORY,
            ParserConstants.KEY_COORDINATES,
            ParserConstants.KEY_AIRFLOW
        )
        return when (tileType) {
            TileType.VILLAGE -> {
                tile.keySet() == setOf(
                    ParserConstants.KEY_ID, ParserConstants.KEY_CATEGORY,
                    ParserConstants.KEY_COORDINATES
                )
            }
            TileType.ROAD, TileType.MEADOW, TileType.FOREST -> {
                val hasAirflow = tile.getBoolean(ParserConstants.KEY_AIRFLOW)
                if (hasAirflow) {
                    tile.keySet() == baseKeys + ParserConstants.KEY_DIRECTION
                } else {
                    tile.keySet() == baseKeys
                }
            }
            TileType.PLANTATION -> {
                val newSet = baseKeys + ParserConstants.KEY_FARM +
                    ParserConstants.KEY_CAPACITY + ParserConstants.KEY_PLANT
                val hasAirflow = tile.getBoolean(ParserConstants.KEY_AIRFLOW)
                if (hasAirflow) {
                    tile.keySet() == newSet + ParserConstants.KEY_DIRECTION
                } else {
                    tile.keySet() == newSet
                }
            }
            TileType.FIELD -> {
                val newSet = baseKeys + ParserConstants.KEY_FARM +
                    ParserConstants.KEY_CAPACITY + ParserConstants.KEY_POSSIBLE_PLANTS
                val hasAirflow = tile.getBoolean(ParserConstants.KEY_AIRFLOW)
                if (hasAirflow) {
                    tile.keySet() == newSet + ParserConstants.KEY_DIRECTION
                } else {
                    tile.keySet() == newSet
                }
            }

            TileType.FARMSTEAD -> {
                val hasAirflow = tile.getBoolean(ParserConstants.KEY_AIRFLOW)
                val newSet = baseKeys + ParserConstants.KEY_SHED + ParserConstants.KEY_FARM
                if (hasAirflow) {
                    tile.keySet() == newSet + ParserConstants.KEY_DIRECTION
                } else {
                    tile.keySet() == newSet
                }
            }
        }
    }

    /**
     * Extracts the coordinates of a tile from JSON.
     *
     * @param tile the [JSONObject] containing the tile information
     * @return a [Coordinate] representing the tile's position
     */
    private fun parseCoordinate(tile: JSONObject): Coordinate {
        val coordinateJSON = tile.getJSONObject(ParserConstants.KEY_COORDINATES)
        return Coordinate(
            x = coordinateJSON.getInt(ParserConstants.KEY_X),
            y = coordinateJSON.getInt(ParserConstants.KEY_Y)
        )
    }

    /**
     * Returns the direction of the airflow for a tile.
     *
     * @param tile the [JSONObject] containing the tile information
     * @return the airflow [Direction]; returns [Direction.NONE] if the "direction" key is missing
     */
    private fun parseDirection(tile: JSONObject): Direction {
        if (!tile.has(ParserConstants.KEY_DIRECTION)) return Direction.NONE

        return when (tile.getString(ParserConstants.KEY_DIRECTION)) {
            "0" -> Direction.NORTH
            "45" -> Direction.NORTHEAST
            "90" -> Direction.EAST
            "135" -> Direction.SOUTHEAST
            "180" -> Direction.SOUTH
            "225" -> Direction.SOUTHWEST
            "270" -> Direction.WEST
            "315" -> Direction.NORTHWEST
            else -> Direction.NONE
        }
    }

    /**
     * Adds a non-growable tile to the simulation data.
     *
     * @param tileObject the [Tile] to add
     * @param owner the ID of the owning farm, or `null` if no owner
     * @return `true` after successfully adding the tile
     */
    private fun parseNonGrowableTile(tileObject: Tile, owner: Int?): Boolean {
        simulationData.addTile(tileObject, owner)
        return true
    }

    /**
     * Parses a growable tile (FIELD or PLANTATION) and adds it to the simulation data.
     *
     * @param tile the [JSONObject] containing the tile information
     * @param tileObject the [Tile] object to set growable data for
     * @param owner the ID of the owning farm, or `null` if no owner
     * @return `true` if parsing and adding was successful, `false` otherwise
     */
    private fun parseGrowableTile(tile: JSONObject, tileObject: Tile, owner: Int?): Boolean {
        val capacity = tile.getInt(ParserConstants.KEY_CAPACITY)
        val type = tileObject.getTileType()
        val growable: Growable
        when (type) {
            TileType.FIELD -> {
                val possiblePlants = List(tile.getJSONArray(ParserConstants.KEY_POSSIBLE_PLANTS).length()) { i ->
                    PlantType.valueOf(tile.getJSONArray(ParserConstants.KEY_POSSIBLE_PLANTS).getString(i))
                }
                growable = Growable(possiblePlants, capacity)
            }

            TileType.PLANTATION -> {
                val plant = PlantType.valueOf(tile.getString(ParserConstants.KEY_PLANT))
                growable = Growable(plant, capacity)
            }

            else -> {
                return false
            }
        }

        tileObject.setGrowable(growable)
        simulationData.addTile(tileObject, owner)
        return true
    }

    /**
     * Validates that the given coordinate and ID are not already used in the map.
     *
     * @param coordinate the [Coordinate] of the tile
     * @param id the ID of the tile
     * @return `true` if both coordinate and ID are free, `false` if either is already taken
     */
    private fun validateCoordinateAndIDNotAlreadyTaken(coordinate: Coordinate, id: Int): Boolean {
        val map = simulationData.getMap()
        val isCoordinateFree = !map.doesTileExist(coordinate)
        val isIDFree = map.getCoordinateByTileID(id) == null
        return isCoordinateFree && isIDFree
    }

    /**
     * Validates that a coordinate is consistent in terms of evenness.
     *
     * Both x and y must be either even or odd for a valid coordinate.
     *
     * @param coordinate the [Coordinate] to validate
     * @return `true` if the coordinate is valid (both x and y are even or both are odd), `false` otherwise
     */
    private fun validateCoordinate(coordinate: Coordinate): Boolean {
        val x = coordinate.getX()
        val y = coordinate.getY()
        return (isEven(x) && isEven(y)) || (!isEven(x) && !isEven(y))
    }

    /**
     * Checks if a given integer is even.
     *
     * @param n the integer to check
     * @return `true` if `n` is even, `false` if `n` is odd
     */
    private fun isEven(n: Int) = n % 2 == 0

    /**
     * Validates the shape of a tile based on its coordinate and type.
     *
     *
     * @param coordinate the [Coordinate] of the tile
     * @param tileType the [TileType] of the tile
     * @return `true` if the coordinate matches the allowed shape for the tile type, `false` otherwise
     */
    private fun validateTileShape(coordinate: Coordinate, tileType: TileType): Boolean {
        return when (tileType) {
            TileType.MEADOW, TileType.FARMSTEAD -> coordinate.isCoordinateOfSquareTile()
            TileType.FIELD, TileType.PLANTATION -> coordinate.isCoordinateOfOctagonalTile()
            TileType.VILLAGE, TileType.ROAD, TileType.FOREST ->
                coordinate.isCoordinateOfSquareTile() || coordinate.isCoordinateOfOctagonalTile()
        }
    }

    /**
     * Validates the Direction of an airflow

     * @param coordinate the [Coordinate] of the tile
     * @param direction the [Direction] of the airflow
     * @return `true` if airflow is valid, `false` otherwise
     */
    private fun validateDirection(coordinate: Coordinate, direction: Direction): Boolean {
        if (coordinate.isCoordinateOfSquareTile()) {
            return when (direction) {
                Direction.NORTH,
                Direction.EAST,
                Direction.WEST,
                Direction.SOUTH -> false
                else -> true
            }
        }
        return true
    }
}
