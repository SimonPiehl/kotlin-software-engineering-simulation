package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONObject

/**
 * Parser for clouds.
 *
 * Parses cloud information from JSON objects and adds the resulting
 * [Cloud] to the [SimulationData].
 *
 * @property simulationData the simulation data where clouds will be stored
 */
class CloudParser(private val simulationData: SimulationData) {

    /**
     * Parses a single cloud from a JSONObject and adds it to the simulation data.
     *
     * @param cloud JSON object representing one cloud
     * @return `true` if parsing was successful, `false` otherwise
     */
    fun parseCloud(cloud: JSONObject): Boolean {
        if (!validateKeyset(cloud)) return false
        val id = cloud.getInt(ParserConstants.KEY_ID)
        val amount = cloud.getInt(ParserConstants.KEY_AMOUNT)
        val duration = cloud.getInt(ParserConstants.KEY_DURATION)
        val locationID = cloud.getInt(ParserConstants.KEY_LOCATION)
        val map = simulationData.getMap()

        val location = map.getCoordinateByTileID(locationID)
        val tile = map.getTileByID(locationID)

        val isValid = duration != 0 &&
            validateCloudIDUnique(id) &&
            location != null &&
            tile != null &&
            tile.getTileType() != TileType.VILLAGE

        if (!isValid) {
            return false
        }

        val cloudObject = Cloud(id, amount, duration, location)
        simulationData.addCloud(cloudObject)
        return true
    }

    /**
     * Checks whether the given cloud ID is unique within the simulation.
     *
     * @param id the cloud ID to check
     * @return `true` if the ID is not used yet, `false` if a cloud with this ID already exists
     */
    private fun validateCloudIDUnique(id: Int): Boolean {
        val clouds = simulationData.getClouds()
        return clouds.none { it.getID() == id }
    }

    /**
     * Checks whether the given cloud JSON object contains exactly the required keys.
     *
     * @param cloud the JSON object to check
     * @return `true` if the object has all and only the expected keys, `false` otherwise
     */
    private fun validateKeyset(cloud: JSONObject): Boolean {
        return cloud.keySet() == setOf(
            ParserConstants.KEY_ID, ParserConstants.KEY_LOCATION,
            ParserConstants.KEY_AMOUNT, ParserConstants.KEY_DURATION
        )
    }
}
