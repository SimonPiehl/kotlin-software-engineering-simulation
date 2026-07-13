package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BeeHappy
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Duration
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONObject

/**
 * Parser for incidents
 *
 *  @property simulationData the simulation data where the incidents will be stored
 */
class IncidentParser(private val simulationData: SimulationData) {

    /**
     * Parses a single incident from a JSONObject.
     *
     * Chooses the correct specialized parse method based on the "type" string.
     *
     * @param incident JSONObject containing the information of one incident
     * @return `true` if parsing was successful, `false` otherwise
     */
    fun parseOneIncident(incident: JSONObject): Boolean {
        val id = incident.getInt(ParserConstants.KEY_ID)
        if (!validateIncidentIDUnique(id)) { return false }
        val type = incident.getString(ParserConstants.KEY_TYPE)
        val tick = incident.getInt(ParserConstants.KEY_TICK)

        return when (type) {
            "CLOUD_CREATION" -> parseCloudCreation(incident, id, tick)
            "ANIMAL_ATTACK" -> parseAnimalAttack(incident, id, tick)
            "BEE_HAPPY" -> parseBeeHappy(incident, id, tick)
            "DROUGHT" -> parseDrought(incident, id, tick)
            "BROKEN_MACHINE" -> parseBrokenMachine(incident, id, tick)
            "CITY_EXPANSION" -> parseCityExpansion(incident, id, tick)
            else -> false
        }
    }

    /**
     * Parses a CloudCreation incident.
     *
     * @param incident JSONObject containing CloudCreation data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseCloudCreation(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE, ParserConstants.KEY_TICK,
                ParserConstants.KEY_DURATION, ParserConstants.KEY_LOCATION,
                ParserConstants.KEY_RADIUS, ParserConstants.KEY_AMOUNT
            )
        ) {
            return false
        }
        val duration = incident.getInt(ParserConstants.KEY_DURATION)
        if (duration == 0) return false
        val location = incident.getInt(ParserConstants.KEY_LOCATION)
        val radius = incident.getInt(ParserConstants.KEY_RADIUS)
        val amount = incident.getInt(ParserConstants.KEY_AMOUNT)
        val cloudCreation = CloudCreation(id, tick, duration, location, radius, amount)
        simulationData.addIncident(cloudCreation)
        return true
    }

    /**
     * Parses and Validates an AnimalAttack incident.
     *
     * @param incident JSONObject containing AnimalAttack data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseAnimalAttack(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE, ParserConstants.KEY_TICK,
                ParserConstants.KEY_LOCATION,
                ParserConstants.KEY_RADIUS
            )
        ) {
            return false
        }
        val location = incident.getInt(ParserConstants.KEY_LOCATION)
        val radius = incident.getInt(ParserConstants.KEY_RADIUS)
        // Validation Step
        val map = simulationData.getMap()
        val tile = map.getTileByID(location) ?: return false
        val locationCoordinate = tile.getCoordinate()
        val affectedTileCoordinates = locationCoordinate.getNeighbours(radius).toMutableList()
        affectedTileCoordinates.add(locationCoordinate)
        val affectedTiles = map.getTilesByCoordinates(affectedTileCoordinates)
        if (affectedTiles.none { it.getTileType() == TileType.FOREST }) return false
        val animalAttack = AnimalAttack(id, tick, location, radius)
        simulationData.addIncident(animalAttack)
        return true
    }

    /**
     * Parses a BeeHappy incident.
     *
     * @param incident JSONObject containing BeeHappy data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseBeeHappy(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE,
                ParserConstants.KEY_TICK, ParserConstants.KEY_LOCATION,
                ParserConstants.KEY_RADIUS, ParserConstants.KEY_EFFECT
            )
        ) {
            return false
        }
        val location = incident.getInt(ParserConstants.KEY_LOCATION)
        val radius = incident.getInt(ParserConstants.KEY_RADIUS)
        val effect = incident.getInt(ParserConstants.KEY_EFFECT)
        // Validation Step
        val map = simulationData.getMap()
        val tile = map.getTileByID(location) ?: return false
        val locationCoordinate = tile.getCoordinate()
        val affectedTileCoordinates = locationCoordinate.getNeighbours(radius).toMutableList()
        affectedTileCoordinates.add(locationCoordinate)
        val affectedTiles = map.getTilesByCoordinates(affectedTileCoordinates)
        if (affectedTiles.none { it.getTileType() == TileType.MEADOW }) return false
        val beeHappy = BeeHappy(id, tick, location, radius, effect)
        simulationData.addIncident(beeHappy)
        return true
    }

    /**
     * Parses a Drought incident.
     *
     * @param incident JSONObject containing Drought data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseDrought(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE, ParserConstants.KEY_TICK,
                ParserConstants.KEY_LOCATION,
                ParserConstants.KEY_RADIUS
            )
        ) {
            return false
        }
        val location = incident.getInt(ParserConstants.KEY_LOCATION)
        val radius = incident.getInt(ParserConstants.KEY_RADIUS)
        val drought = Drought(id, tick, location, radius)
        simulationData.addIncident(drought)
        return true
    }

    /**
     * Parses a BrokenMachine incident.
     *
     * @param incident JSONObject containing BrokenMachine data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseBrokenMachine(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE, ParserConstants.KEY_TICK,
                ParserConstants.KEY_DURATION,
                ParserConstants.KEY_MACHINE_ID
            )
        ) {
            return false
        }
        val duration = parseDuration(incident, tick)
        val machineId = incident.getInt(ParserConstants.KEY_MACHINE_ID)
        val machine = simulationData.findMachineById(machineId) ?: return false
        val broken = BrokenMachine(id, tick, duration, machine)
        simulationData.addIncident(broken)
        return true
    }

    /**
     * Parses a CityExpansion incident.
     *
     * @param incident JSONObject containing CityExpansion data
     * @param id unique ID of the incident
     * @param tick simulation tick when incident occurs
     * @return `true` if parsing was successful, otherwise 'false'
     */
    private fun parseCityExpansion(incident: JSONObject, id: Int, tick: Int): Boolean {
        // keyset validation
        if (incident.keySet() != setOf(
                ParserConstants.KEY_ID, ParserConstants.KEY_TYPE, ParserConstants.KEY_TICK,
                ParserConstants.KEY_LOCATION,
            )
        ) {
            return false
        }
        val location = incident.getInt(ParserConstants.KEY_LOCATION)
        val city = CityExpansion(id, tick, location)
        simulationData.addIncident(city)
        return true
    }

    /**
     * Parses a duration from a JSONObject.
     *
     * @param incident JSONObject containing the duration
     * @param tick simulation tick when incident starts
     * @return Duration object with startTick and endTick
     */
    private fun parseDuration(incident: JSONObject, tick: Int): Duration {
        val duration = incident.getInt(ParserConstants.KEY_DURATION)
        return if (duration == -1) {
            Duration(
                startTick = tick,
                endTick = simulationData.getMaxTick() + 1
            )
        } else {
            Duration(
                startTick = tick,
                endTick = tick + duration
            )
        }
    }

    /**
     * Checks if an incident ID is unique across all farms.
     *
     * @param id the incident ID to check
     * @return `true` if the ID is not used yet, `false` if it already exists
     */
    private fun validateIncidentIDUnique(id: Int): Boolean {
        val incidents = simulationData.getAllIncidents()
        return incidents.none { it.getID() == id }
    }
}
