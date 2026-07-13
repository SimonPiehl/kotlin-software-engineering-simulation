package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Incident
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import kotlin.collections.mutableMapOf

/**
 * Simulation data
 *
 * @constructor
 *
 * @param maxTick
 * @param yearTick
 */
class SimulationData(
    private val maxTick: Int,
    private var yearTick: Int
) {

    private val map: Map = Map()
    private val clouds: MutableList<Cloud> = mutableListOf()
    private val farms: MutableList<Farm> = mutableListOf()
    private var maxCloudID: Int = -1
    private val incidents: MutableMap<Int, MutableList<Incident>> = mutableMapOf()
    private var currentTick: Int = 0
    private val tilesHarvestChanged = mutableMapOf<Tile, MutableList<Double>>()
    private val tilesWhereDrought = mutableListOf<Tile>()

    /**
     * Get map
     *
     * @return
     */
    // Map
    fun getMap(): Map = map

    /**
     * Add tile
     *
     * @param tile
     * @param farmId
     */
    fun addTile(tile: Tile, farmId: Int?) { map.addTile(tile, farmId) }

    /**
     * Get max tick
     *
     * @return
     */
    fun getMaxTick(): Int = maxTick

    /**
     * Get clouds
     *
     * @return
     */
    fun getClouds(): MutableList<Cloud> = clouds

    /**
     * Add cloud
     *
     * @param cloud
     */
    fun addCloud(cloud: Cloud) {
        this.clouds.add(cloud)
        clouds.sortBy { it.getID() }
        this.maxCloudID = kotlin.math.max(this.maxCloudID, cloud.getID())
    }

    /**
     * Delete cloud
     *
     * @param cloud
     */
    fun deleteCloud(cloud: Cloud) { this.clouds.remove(cloud) }

    /**
     * Get farms
     *
     * @return
     */
    fun getFarms(): MutableList<Farm> {
        return farms.sortedBy { it.getID() }.toMutableList()
    }

    /**
     * Add farm
     *
     * @param farm
     */
    fun addFarm(farm: Farm) {
        this.farms.add(farm)
        farms.sortBy { it.getID() }
    }

    /**
     * Get max cloud i d
     *
     * @return
     */
    fun getMaxCloudID(): Int = maxCloudID

    /**
     * Get incidents
     *
     * @return
     */
    fun getIncidents(): MutableMap<Int, MutableList<Incident>> = incidents

    /**
     * Add incident
     *
     * @param incident
     */
    fun addIncident(incident: Incident) {
        val key = incident.getTick()
        incidents.getOrPut(key) { mutableListOf() }.add(incident)
        incidents[key]?.sortBy { it.getID() }
    }

    /**
     * Get current tick
     *
     * @return
     */
    fun getCurrentTick(): Int = currentTick

    /**
     * Set current tick
     *
     * @param value
     */
    fun setCurrentTick(value: Int) { this.currentTick = value }

    /**
     * Get year tick
     *
     * @return
     */
    fun getYearTick(): Int = yearTick

    /**
     * Set year tick
     *
     * @param value
     */
    fun setYearTick(value: Int) { this.yearTick = value }

    /**
     * Get other cloud on same coordinate
     *
     * @param cloud
     * @return
     */
    fun getOtherCloudOnSameCoordinate(cloud: Cloud): Cloud? {
        val res = clouds.filter { it.getLocation() == cloud.getLocation() && it.getID() != cloud.getID() }
        if (res.isEmpty()) return null
        // Else return cloud, only 1 possible as 2 clouds on one tile not allowed
        return res[0]
    }

    /**
     * Find machine by id
     *
     * @param machineId
     * @return
     */
    fun findMachineById(machineId: Int): Machine? {
        for (farm in farms) {
            val machine = farm.getMachineByID(machineId)
            if (machine != null) {
                return machine
            }
        }
        return null
    }

    /**
     * Returns a List of all Incidents occurring in the whole simulation
     */
    fun getAllIncidents(): List<Incident> {
        val incidents = getIncidents().values.flatten()
        return incidents
    }

    /**
     * Returns the Farm of the given ID
     */
    fun getFarmByID(id: Int): Farm? {
        return farms.find { it.getID() == id }
    }

    /**
     * Get tiles harvest changed
     *
     * @return
     */
    fun getTilesHarvestChanged(): MutableMap<Tile, MutableList<Double>> {
        return tilesHarvestChanged
    }

    /**
     * Add tiles harvest changed
     *
     * @param tile
     */
    fun addTilesHarvestChanged(tile: Tile, effectPercentUnderOne: Double) {
        val list = tilesHarvestChanged.getOrPut(tile) { mutableListOf() }
        list.add(effectPercentUnderOne)
    }

    /**
     * Clear tiles harvest changed
     *
     */
    fun clearTilesHarvestChanged() {
        tilesHarvestChanged.clear()
    }

    /**
     * Add tilesWhereDrought
     *
     * @param tile
     */
    fun addTilesWhereDrought(tile: Tile) {
        tilesWhereDrought.add(tile)
        tilesWhereDrought.sortBy { it.getID() }
    }

    /**
     * Clear tilesWhereDrought field
     *
     */
    fun clearTilesWhereDrought() {
        tilesWhereDrought.clear()
    }

    /**
     * Get tiles where drought
     *
     * @return
     */
    fun getTilesWhereDrought(): List<Tile> {
        return tilesWhereDrought
    }
}
