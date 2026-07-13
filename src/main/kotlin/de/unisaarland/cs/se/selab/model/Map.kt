package de.unisaarland.cs.se.selab.model

import de.unisaarland.cs.se.selab.util.Coordinate

/**
 * Map
 *
 * @constructor Create Map (empty when first creating it)
 */
class Map {
    private val tilesByCoordinate = mutableMapOf<Coordinate, Tile>()
    private val tilesByID = mutableMapOf<Int, Tile>()
    private val farmFarmsteads = mutableMapOf<Int, MutableList<Tile>>()
    private val farmFields = mutableMapOf<Int, MutableList<Tile>>()
    private val farmPlantations = mutableMapOf<Int, MutableList<Tile>>()

    /**
     * Add tile to the map
     *
     * @param tile tile to add
     * @param farmID null if tile has no owner, otherwise the id of the farm that owns it
     */
    fun addTile(tile: Tile, farmID: Int?) {
        val coord = tile.getCoordinate()
        this.tilesByCoordinate[coord] = tile

        val id = tile.getID()
        this.tilesByID[id] = tile

        val type = tile.getTileType()

        if (farmID != null) {
            when (type) {
                TileType.FIELD -> farmFields.getOrPut(farmID) { mutableListOf() }.add(tile)
                TileType.PLANTATION -> farmPlantations.getOrPut(farmID) { mutableListOf() }.add(tile)
                TileType.FARMSTEAD -> farmFarmsteads.getOrPut(farmID) { mutableListOf() }.add(tile)
                else -> return
            }
        }
    }

    /**
     * Remove tile
     *
     * @param tile tile to remove
     * @param farmID id of farm that owns it, null if not owned by farm
     */
    fun removeTile(tile: Tile, farmID: Int?) {
        // remove from maps
        tilesByCoordinate.remove(tile.getCoordinate())
        tilesByID.remove(tile.getID())

        // remove from farm-specific maps
        when (tile.getTileType()) {
            TileType.FIELD -> {
                farmFields[farmID]?.let { list ->
                    list.remove(tile)
                    if (list.isEmpty()) farmFields.remove(farmID)
                }
            }
            TileType.PLANTATION -> {
                farmPlantations[farmID]?.let { list ->
                    list.remove(tile)
                    if (list.isEmpty()) farmPlantations.remove(farmID)
                }
            }
            TileType.FARMSTEAD -> {
                farmFarmsteads[farmID]?.let { list ->
                    list.remove(tile)
                    if (list.isEmpty()) farmFarmsteads.remove(farmID)
                }
            }
            else -> { /* nothing to do */ }
        }
    }

    /**
     * Does tile exist
     *
     * @param coordinate Coordinate where we want to check the presence of a tile
     * @return if tile corresponding to a Coordinate is existing
     */
    fun doesTileExist(coordinate: Coordinate): Boolean =
        coordinate in tilesByCoordinate

    /**
     * Get coordinate by tile ID
     *
     * @param id
     * @return
     */
    fun getCoordinateByTileID(id: Int): Coordinate? =
        tilesByID[id]?.getCoordinate()

    /**
     * Get all growable Tiles the map contains
     *
     * @return
     */
    fun getAllGrowable(): List<Tile> {
        val help1 = farmFields.values.flatten()
        val help2 = farmPlantations.values.flatten()
        return (help1 + help2).sortedBy { it.getID() }
    }

    /**
     * Get all tiles that exist corresponding to a list of coordinates
     *
     * @param coordinates
     * @return
     */
    fun getTilesByCoordinates(coordinates: List<Coordinate>): MutableList<Tile> {
        val list = mutableListOf<Tile>()

        for (coordinate in coordinates) {
            val tile: Tile? = getTileByCoordinate(coordinate)
            if (tile != null) list.add(tile)
        }
        return list.sortedBy { it.getID() }.toMutableList()
    }

    /**
     * Get tile by coordinate
     *
     * @param coordinate
     * @return
     */
    fun getTileByCoordinate(coordinate: Coordinate): Tile? =
        tilesByCoordinate[coordinate]

    /**
     * Get tile by ID
     *
     * @param id
     * @return
     */
    fun getTileByID(id: Int): Tile? {
        return tilesByID[id]
    }

    /**
     * Get FarmID by Tile
     *
     * @param Tile
     * @return
     */
    fun getFarmIDbyField(tile: Tile): Int? {
        for ((key, value) in farmFields) {
            if (value.contains(tile)) {
                return key
            }
        }
        return null
    }

    /**
     * Get farm ID by tile
     *
     * @param tile
     * @return
     */
    fun getFarmIDbyTile(tile: Tile): Int? {
        farmFarmsteads.forEach { (farmId, tiles) ->
            if (tile in tiles) return farmId
        }
        farmFields.forEach { (farmId, tiles) ->
            if (tile in tiles) return farmId
        }
        farmPlantations.forEach { (farmId, tiles) ->
            if (tile in tiles) return farmId
        }
        return null
    }

    /**
     * Get all tiles
     *
     * @return
     */
    fun getAllTiles(): List<Tile> = tilesByID.values.toList().sortedBy { it.getID() }

    /**
     * Does tile exist
     *
     * @param id
     * @return
     */
    fun doesTileExist(id: Int): Boolean {
        return tilesByID.containsKey(id)
    }

    /**
     * Get all tiles copy
     *
     * @return
     */
    fun getAllTilesCopy(): List<Tile> =
        tilesByID.values.map {
            Tile(
                it.getID(),
                it.getTileType(),
                it.getCoordinate(),
                it.getAirflow(),
                it.getDirection(),
                it.shedExists(),
                null
            )
        }.sortedBy { it.getID() }

    /**
     * Get fields of farm
     *
     * @param id
     * @return
     */
    fun getFieldsOfFarm(id: Int): MutableList<Tile> {
        return farmFields[id]?.sortedBy { it.getID() }?.toMutableList() ?: mutableListOf()
    }

    /**
     * Get farm fields by i d
     *
     * @param id
     * @return
     */
    fun getFarmFieldsByID(id: Int): List<Tile> {
        return farmFields[id]?.sortedBy { it.getID() } ?: mutableListOf()
    }

    /**
     * Get farm plantations by i d
     *
     * @param id
     * @return
     */
    fun getFarmPlantationsByID(id: Int): List<Tile> {
        return farmPlantations[id]?.sortedBy { it.getID() } ?: mutableListOf()
    }

    /**
     * Get farm farmsteads by i d
     *
     * @param id
     * @return
     */
    fun getFarmFarmsteadsByID(id: Int): List<Tile> {
        return farmFarmsteads[id]?.sortedBy { it.getID() } ?: mutableListOf()
    }
}
