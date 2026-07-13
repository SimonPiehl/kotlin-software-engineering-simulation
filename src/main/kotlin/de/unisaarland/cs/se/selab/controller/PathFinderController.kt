package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.view.Logger

/**
 * PathFinder controller
 *
 * @property data
 * @constructor Create empty Path finder controller
 */
class PathFinderController(private var data: SimulationData) {

    /**
     * Get all reachable tiles from machine m
     *
     * @param m
     * @return
     */
    private fun getAllReachableTilesPrivate(farm: Farm, m: Machine, onlyTwo: Boolean): List<Pair<Tile, Int>> {
        // Init of visited list
        val visited = mutableMapOf<Coordinate, Int>()
        // Init of queue list
        val queue: ArrayDeque<Pair<Coordinate, Int>> = ArrayDeque()
        // Start location = location of machine
        val start = m.getCurrentLocation()
        // Adds a pair of start and round to queue
        queue.add(Pair(start, 0))
        // Adds start node to visited map
        visited[start] = 0
        // While queue not empty perform bfs
        while (queue.isNotEmpty()) {
            // Remove current pair out of queue
            val (current, steps) = queue.removeFirst()
            // If Steps >=2 finished if only tow needed
            if (steps >= 2 && onlyTwo) break
            // Get neighbors of current coordinate
            val neighbours = current.getNeighbours(1)
            // For all coordinates
            for (coordinate in neighbours) {
                // Get tiles
                val tile = data.getMap().getTileByCoordinate(coordinate) ?: continue
                // Check restrictions, machines cannot traverse FOREST nor tiles
                // From other farms nor VILLAGE if they are loaded
                if (tile.getTileType() == TileType.FOREST || isRestrictedFarmTile(tile, farm) ||
                    isVillageWithLoad(m, tile)
                ) {
                    // If so continue and do not add to visited list
                    continue
                }
                // If not already in visited add with current radius
                if (!visited.containsKey(coordinate)) {
                    val newSteps = steps + 1
                    visited[coordinate] = newSteps
                    queue.add(Pair(coordinate, newSteps))
                }
            }
        }
        // Remove all possible null tiles and translate to tiles,step
        val output = visited.mapNotNull { (coordinate, steps) ->
            data.getMap().getTileByCoordinate(coordinate)?.let { tile ->
                Pair(tile, steps)
            }
        }
        // Filter out the start node
        return output.filter { it.first.getID() != data.getMap().getTileByCoordinate(m.getCurrentLocation())?.getID() }
            .sortedBy { it.first.getID() }
    }

    // Filters out tiles not from machines farm
    private fun isRestrictedFarmTile(tile: Tile, farm: Farm): Boolean {
        // Checks if tile is of type contains in a farm
        val isFarmTile = tile.getTileType() in setOf(TileType.FIELD, TileType.FARMSTEAD, TileType.PLANTATION)
        // If not return false -> not restricted
        if (!isFarmTile) return false
        // Gets all fields plantations farmsteads of current farm
        val farmFields = data.getMap().getFarmFieldsByID(farm.getID())
        val farmPlantations = data.getMap().getFarmPlantationsByID(farm.getID())
        val farmFarmsteads = data.getMap().getFarmFarmsteadsByID(farm.getID())
        // If not contained -> must be in other farm -> restricted
        return !(farmFields.contains(tile) || farmPlantations.contains(tile) || farmFarmsteads.contains(tile))
    }

    /**
     * Checks if machine loaded and tile is village -> restricted
     *
     * @param m
     * @param tile
     * @return
     */
    private fun isVillageWithLoad(m: Machine, tile: Tile): Boolean {
        return m.getAmountLoadedThisTick() > 0 && tile.getTileType() == TileType.VILLAGE
    }

    /**
     * Get all reachable tiles in radius two from machine
     *
     * @param farm
     * @param m
     * @return
     */
    fun getAllReachableTilesInRadiusTwo(farm: Farm, m: Machine): List<Tile> {
        // Calls getAllReachableTilesPrivate but only 2 rounds
        val reachableTiles = getAllReachableTilesPrivate(farm, m, true)
        // Filters steps again to be sure
        val filteredTiles = reachableTiles.filter { (_, steps) -> steps <= 2 }
        // Return by ID sorted
        return filteredTiles.map { (tile, _) -> tile }.sortedBy { it.getID() }
    }

    /**
     * Is tile reachable from machines position takes loaded in consideration
     *
     * @param farm
     * @param m
     * @param tile
     * @return
     */
    fun isTileReachable(farm: Farm, m: Machine, tile: Tile?): Boolean {
        var isReachable = false
        // Checks if tile is in all reachable tiles
        if (getAllReachableTilesPrivate(farm, m, false).map { (tile, _) -> tile }.contains(tile)) {
            isReachable = true
        }
        return isReachable
    }

    /**
     * Move loaded machine to shed if possible own, if not next reachable shed with the lowest id
     *
     * @param m
     * @param farm
     */

    fun moveToShedLoadedMachine(m: Machine, farm: Farm) {
        var wentToShed = false
        // Get location of shed
        var shedLocation = m.getLocationOfShed()
        // If tile is reachable return to shed
        if (isTileReachable(farm, m, data.getMap().getTileByCoordinate(shedLocation))) {
            m.setCurrentLocation(shedLocation)
            wentToShed = true
        } else {
            // Else search for other shed sorted by ID
            for (shed in data.getMap().getFarmFarmsteadsByID(farm.getID()).sortedBy { it.getID() }) {
                if (isTileReachable(farm, m, shed)) {
                    shedLocation = shed.getCoordinate()
                    m.setCurrentLocation(shedLocation)
                    m.setLocationOfShed(shedLocation)
                    wentToShed = true
                    break
                }
            }
        }
        val plant = m.getWorkedOnPlant()
        if (plant != null) {
            // If went to sed unload machine
            if (wentToShed) {
                m.setIsDisabledPermanently(false)
                Logger.logMachineReturn(m.getID(), true, data.getMap().getTileByCoordinate(shedLocation)?.getID())
                Logger.logUnloadMachine(
                    m.getID(),
                    plant,
                    m.getAmountLoadedThisTick()
                )
            } // Else machine did not return
            else {
                Logger.logMachineReturn(m.getID(), false, data.getMap().getTileByCoordinate(shedLocation)?.getID())
            }
        }
        // If not went to shed set permanently disabled
        if (!wentToShed) {
            m.setIsDisabledPermanently(true)
        }
    }

    /**
     * Move unloaded machine to shed always possible
     *
     * @param m
     */
    fun moveToShedUnloadedMachine(m: Machine) {
        // Get location of shed
        val shedLocation = m.getLocationOfShed()
        // Set location of shed
        m.setCurrentLocation(shedLocation)
        // Empty machine can always return to shed
        Logger.logMachineReturn(m.getID(), true, data.getMap().getTileByCoordinate(shedLocation)?.getID())
    }
}
