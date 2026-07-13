package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.SowingPlan
import de.unisaarland.cs.se.selab.util.ParserConstants
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.sortedWith

/**
 * Parses and validates farm-related JSON and stores its created objects
 * (Farm, Machine, SowingPlan) into the provided [SimulationData].
 *
 * This parser is strict: it rejects unknown/duplicate IDs,
 * missing required keys, and inconsistent references to the global [Map].
 *
 * @property data Shared simulation state used for lookups and storing of objects.
 */
class FarmParser(private val data: SimulationData) {

    /**
     * Parse a single farm JSON object and store the resulting [Farm] in [data].
     *
     * Expected keys (exactly [ParserConstants.FARM_KEY_SET_SIZE] keys):
     * `id`, `name`, `farmsteads`, `fields`, `plantations`, `machines`, `sowingPlans`.
     *
     * Validation performed:
     * - ID >= 0
     * - The farm owns exactly the farmsteads/fields/plantations found in the global [Map]
     * (set equality; no missing or extra IDs; all IDs unique per list)
     * - The farm has at least one field *or* plantation
     * - Key sets of farm, machines, and sowing plans match the specification
     * - Machines and sowing plans parse and validate successfully
     *
     * On success, machines are sorted by (duration, id) and sowing plans by (startTick, id).
     *
     * @param farm The JSON object describing a farm.
     * @return `true` if parsing and validation succeed; `false` otherwise. No partial state is added
     * when returning `false`.
     */
    fun parseOneFarm(farm: JSONObject): Boolean {
        val farmID: Int? = farm.optInt(ParserConstants.ID, -1).takeIf { it >= 0 }
        val farmName: String? = farm.getString("name")
        val farmFarmsteadsJSON: JSONArray? = farm.optJSONArray("farmsteads")
        val farmFieldsJSON: JSONArray? = farm.optJSONArray(ParserConstants.FIELDS)
        val farmPlantationsJSON: JSONArray? = farm.optJSONArray("plantations")
        val farmMachinesJSON: JSONArray? = farm.optJSONArray("machines")
        val farmSowingPlansJSON: JSONArray? = farm.optJSONArray("sowingPlans")

        // Check for null
        val detektHelperCondition = farmID == null || farmName == null ||
            farmFarmsteadsJSON == null || farmFieldsJSON == null
        val detektHelperSecondCondition = farmPlantationsJSON == null ||
            farmMachinesJSON == null || farmSowingPlansJSON == null
        if (detektHelperCondition || detektHelperSecondCondition) {
            return false
        }

        // Getting it from the JSONs
        val farmFarmsteads = (0 until farmFarmsteadsJSON.length()).map {
                i ->
            farmFarmsteadsJSON.getInt(i)
        }
        val farmFields = (0 until farmFieldsJSON.length()).map {
                i ->
            farmFieldsJSON.getInt(i)
        }
        val farmPlantations = (0 until farmPlantationsJSON.length()).map {
                i ->
            farmPlantationsJSON.getInt(i)
        }

        // Check if validation worked
        val map = data.getMap()
        val farmHasAtLeastOneFieldOrPlantation = farmFields.isNotEmpty() || farmPlantations.isNotEmpty()
        val validationsOkHelper = validateID(farmID) &&
            validateFarmsteads(farmFarmsteads, map, farmID) &&
            validateFields(farmFields, map, farmID) &&
            validatePlantations(farmPlantations, map, farmID)
        val validationsOk = validationsOkHelper && farmHasAtLeastOneFieldOrPlantation && validateKeySetFarm(farm)
        if (validationsOk == false) return false

        // Parsing Machines and Sowing Plans
        val machineResult = parseMachines(farmMachinesJSON, farmID)
        val sowingPlansResult = parseSowingPlan(farmSowingPlansJSON, farmID)

        // Checking Failure
        if (machineResult.isFailure || sowingPlansResult.isFailure) return false

        // Getting machines and sowingPlans
        val machines = machineResult.getOrThrow()
        val sowingPlans = sowingPlansResult.getOrThrow()

        // Creating the new Farm
        val newFarm = Farm(
            farmID,
            farmName,
            machines.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() }),
            sowingPlans.sortedWith(compareBy<SowingPlan> { it.getStartTick() }.thenBy { it.getID() }).toMutableList()
        )
        data.addFarm(newFarm)
        return true
    }

    /**
     * Check that a Farm ID is non-negative.
     *
     * @param id farm identifier to validate.
     * @return `true` if `id >= 0`.
     */
    private fun validateID(id: Int): Boolean {
        return id >= 0
    }

    /**
     * Validate the declared farmsteads for a farm.
     *
     * Requirements:
     * - The map must contain at least one farmstead for the farm.
     * - The farmstead ids in the JSON and the farmsteads in the map must match (same ids).
     * - IDs in the JSON list must be unique.
     *
     * @param idList Farmstead tile IDs from JSON.
     * @param map Global map used for checks.
     * @param farmID Owning farm ID.
     * @return `true` if all constraints are met. False otherwise.
     */
    private fun validateFarmsteads(idList: List<Int>, map: Map, farmID: Int): Boolean {
        // Validate that farmsteads aren't empty
        val mapFarmsteads = map.getFarmFarmsteadsByID(farmID)
        val farmHasFarmsteads = mapFarmsteads.isNotEmpty()
        // Validate that all farmsteads exist in the map and the farm.json has all farmsteads from the map
        val mapFarmsteadsIDs = mapFarmsteads.map { it.getID() }
        val equalFarmsteads = idList.containsAll(mapFarmsteadsIDs) && mapFarmsteadsIDs.containsAll(idList)
        // Validate that Farmsteads ids are unique
        val uniqueFarmsteads = idList.size == idList.distinct().size
        return equalFarmsteads && uniqueFarmsteads && farmHasFarmsteads
    }

    /**
     * Validate the declared field IDs for a farm.
     *
     * Requirements:
     * - The list of field IDs equals the list of fields from the [Map] (same IDs) for this farm.
     * - All IDs are unique.
     *
     * @param idList Field tile IDs from JSON.
     * @param map Global map used for checks.
     * @param farmID Owning farm ID.
     * @return `true` if the lists match and IDs are unique.
     */
    private fun validateFields(idList: List<Int>, map: Map, farmID: Int): Boolean {
        // Validates that all fields exist in the map and the farm.json has all fields from the map.
        val mapFields = map.getFieldsOfFarm(farmID)
        val mapFieldsIDs = mapFields.map { it.getID() }
        val equalFields = idList.containsAll(mapFieldsIDs) && mapFieldsIDs.containsAll(idList)
        // Validates that fields ids are unique.
        val uniqueFields = idList.size == idList.distinct().size
        return equalFields && uniqueFields
    }

    /**
     * Validate the declared plantation IDs for a farm.
     *
     * Requirements:
     * - The list of plantation IDs equals the list of plantations from the [Map] (same IDs) for this farm.
     * - All IDs are unique.
     *
     * @param idList Plantation tile IDs from JSON.
     * @param map Global map used for checks.
     * @param farmID Owning farm ID.
     * @return `true` if the lists match and IDs are unique.
     */
    private fun validatePlantations(idList: List<Int>, map: Map, farmID: Int): Boolean {
        // Validates that all plantations exist in the map and the farm.json has all plantations from the map.
        val mapPlantations = map.getFarmPlantationsByID(farmID)
        val mapPlantationsIDs = mapPlantations.map { it.getID() }
        val equalPlantations = idList.containsAll(mapPlantationsIDs) &&
            mapPlantationsIDs.containsAll(idList)
        // Validates that plantations ids are unique.
        val uniquePlantations = idList.size == idList.distinct().size
        return equalPlantations && uniquePlantations
    }

    /**
     * Parse all machines of a farm.
     *
     * Behavior:
     * - Iterates the array, enforcing that every element is a JSON object.
     * - Delegates to [parseOneMachine] for each entry.
     *
     * @param machinesJson JSON array of machines.
     * @param farmID ID of the farm that owns these machines (used for location validation).
     * @return [Result.success] with the list of [Machine] sorted by (duration, id) on success;
     * otherwise a failure with details.
     */
    fun parseMachines(machinesJson: JSONArray, farmID: Int): Result<List<Machine>> {
        val machines = mutableListOf<Machine>()
        for (i in 0 until machinesJson.length()) {
            val machineJSON: JSONObject = machinesJson.optJSONObject(i)
                ?: return Result.failure(
                    IllegalArgumentException(
                        "Machine parsing failed: element at index $i is not an object"
                    )
                )
            val machineResult = parseOneMachine(machineJSON, farmID)
            if (machineResult.isFailure) {
                return Result.failure(
                    IllegalArgumentException(
                        "Machine parsing failed: element at index $i is not an object"
                    )
                )
            }
            machines.add(machineResult.getOrThrow())
        }
        return Result.success(machines.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() }))
    }

    /**
     * Parse and validate a single machine.
     *
     * Expected keys: `id`, `name`, `duration`, `location`, `actions`, `plants`.
     *
     * Validation performed:
     * - `id >= 0`, `duration in 1..ParserConstants.MAX_DURATION`, `location >= 0`, `name` present
     * - `actions` and `plants` arrays exist and contain only known values
     * - `location` points to a farmstead tile with a shed owned by the given farm
     *
     * @param machine JSON object describing the machine.
     * @param farmID Owning farm ID used to validate the starting location.
     * @return A success [Result] with the created [Machine], or a failure [Result] describing the issue.
     */
    fun parseOneMachine(machine: JSONObject, farmID: Int): Result<Machine> {
        // Getting fields
        val id = machine.optInt("id", -1).takeIf { it >= 0 }
        val name = machine.getString("name")
        val duration = machine.optInt("duration", -1).takeIf { it in 1..ParserConstants.MAX_DURATION }
        val location = machine.optInt("location", -1).takeIf { it >= 0 }

        // Checking for null
        val detektHelper = id == null || name == null
        if (detektHelper || duration == null || location == null) {
            return Result.failure(IllegalArgumentException("Machine has null fields"))
        }

        val helper = parseOneMachineBecauseDetekt(machine)
        if (helper.isFailure) {
            return Result.failure(IllegalArgumentException("Machine: Actions or PlantTypes parsing failed"))
        }
        val (actions, plantTypes) = helper.getOrThrow()

        // More validation
        if (validateMachineLocation(location, farmID) == false) {
            return Result.failure(IllegalArgumentException("Machine location couldn't be validated"))
        }

        val tile = data.getMap().getTileByID(location)
            ?: return Result.failure(IllegalArgumentException("Machine location couldn't be found"))
        val coordinate = tile.getCoordinate()

        // Done with validation
        return Result.success(
            Machine(
                id = id,
                name = name,
                actions = actions,
                possiblePlants = plantTypes,
                duration = duration,
                locationOfShed = coordinate
            )
        )
    }

    /**
     * Extract the machine's `actions` and `plants` lists.
     *
     * This is helper for [parseOneMachine] because of detekt.
     * It enforces presence of both arrays and validates every entry.
     *
     * @param machine Source JSON object.
     * @return A pair of mutable lists `(actions, plantTypes)` on success; a failure otherwise.
     */
    private fun parseOneMachineBecauseDetekt(machine: JSONObject):
        Result<Pair<MutableList<Action>, MutableList<PlantType>>> {
        // Getting Actions and PlantTypes
        val actionsArray = machine.optJSONArray("actions")
            ?: return Result.failure(IllegalArgumentException("Missing 'actions'"))

        val plantsArray = machine.optJSONArray("plants")
            ?: return Result.failure(IllegalArgumentException("Missing 'plants'"))

        // Getting the Actions
        val actions = mutableListOf<Action>()
        for (i in 0 until actionsArray.length()) {
            val actionString: String? = actionsArray.optString(i, null)
            val action = actionFromString(actionString)
                ?: return Result.failure(IllegalArgumentException("Machine Action couldn't be taken"))
            actions.add(action)
        }

        // Getting the PlantTypes
        val plantTypes = mutableListOf<PlantType>()
        for (i in 0 until plantsArray.length()) {
            val plantTypeString: String? = plantsArray.optString(i, null)
            val plantType = plantTypeFromString(plantTypeString)
                ?: return Result.failure(IllegalArgumentException("Machine Action couldn't be taken"))
            plantTypes.add(plantType)
        }

        return Result.success(Pair(actions, plantTypes))
    }

    /**
     * Map string names to [PlantType] enum constants.
     *
     * @param name Uppercased plant name or `null`.
     * @return Matching [PlantType] or `null` for unknown values.
     */
    private fun plantTypeFromString(name: String?): PlantType? =
        when (name) {
            "POTATO" -> PlantType.POTATO
            "WHEAT" -> PlantType.WHEAT
            "OAT" -> PlantType.OAT
            "PUMPKIN" -> PlantType.PUMPKIN
            "APPLE" -> PlantType.APPLE
            "ALMOND" -> PlantType.ALMOND
            "CHERRY" -> PlantType.CHERRY
            "GRAPE" -> PlantType.GRAPE
            else -> null
        }

    /**
     * Map string names to [Action] enum constants.
     *
     * @param name Uppercased action name or `null`.
     * @return Matching [Action] or `null` for unknown values.
     */
    private fun actionFromString(name: String?): Action? =
        when (name) {
            "SOWING" -> Action.SOWING
            "CUTTING" -> Action.CUTTING
            "MOWING" -> Action.MOWING
            "WEEDING" -> Action.WEEDING
            "IRRIGATING" -> Action.IRRIGATING
            "HARVESTING" -> Action.HARVESTING
            else -> null // invalid value
        }

    /**
     * Ensure the machine's starting tile exists and is a shed on a farmstead of the given farm.
     *
     * Checks performed:
     * - Tile exists in the [Map]
     * - Tile is one of the farm's farmsteads (by ID)
     * - A shed is present on that tile
     *
     * @param tileID Tile ID claimed as machine location.
     * @param farmID Owning farm ID.
     * @return `true` if the location is valid for initializing the machine.
     */
    private fun validateMachineLocation(tileID: Int, farmID: Int): Boolean {
        val map = data.getMap()
        if (map.doesTileExist(tileID) == false) return false
        val farmsteads = map.getFarmFarmsteadsByID(farmID)
        val tile = farmsteads.find { it.getID() == tileID } ?: return false
        if (tile.shedExists() == false) return false
        return true
    }

    /**
     * Parse all sowing plans of a farm.
     *
     * Behavior:
     * - Iterates over the array; fails on invalid entries.
     * - Delegates to [parseOnePlan].
     *
     * @param plansJSON JSON array of sowing plans.
     * @param farmID Owning farm ID (used for reachability and ownership checks when expanding locations).
     * @return [Result.success] with the list of [SowingPlan] on success; a failure otherwise.
     */
    fun parseSowingPlan(plansJSON: JSONArray, farmID: Int): Result<List<SowingPlan>> {
        val plans = mutableListOf<SowingPlan>()
        for (i in 0 until plansJSON.length()) {
            val planJSON = plansJSON.optJSONObject(i)
                ?: return Result.failure(IllegalArgumentException("Sowing Plan couldn't be got"))
            val planResult = parseOnePlan(planJSON, farmID)
            if (planResult.isFailure) {
                return Result.failure(IllegalArgumentException("Sowing Plan was failure"))
            }
            val plan = planResult.getOrThrow()
            plans.add(plan)
        }
        return Result.success(plans)
    }

    /**
     * Parse a single sowing plan.
     *
     * Supported shapes:
     * 1) Explicit field list: `{ id, tick, plant, fields }`
     * 2) Area specification: `{ id, tick, plant, location, radius }`
     *
     * Validation performed:
     * - `id >= 0`, `tick >= 0`, `plant` is a FIELD plant (POTATO/WHEAT/OAT/PUMPKIN)
     * - For (2): `location` exists and `radius >= 0`
     * - Locations resolve to non-empty list of field IDs of the given farm
     * - All resulting field IDs are unique and owned by the farm
     * - At least one target field can grow the specified plant (based on tile growables)
     *
     * @param plan JSON object describing the plan.
     * @param farmID Owning farm ID.
     * @return Success with a [SowingPlan] instance or failure describing why parsing/validation failed.
     */
    fun parseOnePlan(plan: JSONObject, farmID: Int): Result<SowingPlan> {
        // Get the things every plan needs to have
        val id = plan.optInt("id", -1).takeIf { it >= 0 }
        val tick = plan.optInt("tick", -1).takeIf { it >= 0 }
        val plantString: String? = plan.optString("plant", null)
        val plantType = plantTypeFromString(plantString)
        val fieldPlants = listOf<PlantType>(PlantType.POTATO, PlantType.WHEAT, PlantType.OAT, PlantType.PUMPKIN)
        val detektHelperCondition = id == null || tick == null || plantType == null
        if (!fieldPlants.contains(plantType) || detektHelperCondition) {
            return Result.failure(IllegalArgumentException("Plan invalid values"))
        }

        // Check which plan type it is (Does it have 1. fields or 2. location and radius)
        val locationsList = mutableListOf<Int>()
        val fieldsArray = plan.optJSONArray(ParserConstants.FIELDS)
        if (fieldsArray == null) {
            // This is SowingPlan of type 2
            val location = plan.optInt("location", -1).takeIf { it >= 0 }
            val radius = plan.optInt("radius", -1).takeIf { it >= 0 }
            if (location == null || radius == null || !data.getMap().doesTileExist(location)) {
                return Result.failure(IllegalArgumentException("Plan location or radius invalid"))
            }
            locationsList.addAll(convertToTileList(location, radius, farmID).toMutableList())
        } else {
            // This is SowingPlan of type 1
            for (i in 0 until fieldsArray.length()) {
                val field = fieldsArray.optInt(i, -1).takeIf { it >= 0 }
                    ?: return Result.failure(IllegalArgumentException("Plan: field invalid"))
                locationsList.add(field)
            }
        }
        if (validatePlan(locationsList, farmID) == false) {
            return Result.failure(IllegalArgumentException("Plan invalid"))
        }

        return Result.success(
            SowingPlan(
                id = id,
                startsAtTick = tick,
                plantToSow = plantType,
                locations = locationsList
            )
        )
    }

    /**
     * Expand an area-based plan (center + radius) into concrete field IDs owned by the farm.
     *
     * @param location Center tile ID.
     * @param radius Non-negative radius in tiles.
     * @param farmID Owning farm ID.
     * @return List of field tile IDs within the area that belong to the farm (may be empty).
     */
    fun convertToTileList(location: Int, radius: Int, farmID: Int): List<Int> {
        // Get the ids of the area made up by the location and radius
        val map = data.getMap()
        val tile = map.getTileByID(location) ?: error("should never happen")
        val tileCoordinate = tile.getCoordinate()
        val locationCoords = tileCoordinate.getNeighbours(radius).toMutableList()
        locationCoords.add(tileCoordinate)
        val allTiles = map.getAllTiles()
        val locationTiles = allTiles.filter { it.getCoordinate() in locationCoords }
        val locations = locationTiles.map { it.getID() }

        // Get the field ids of the Farm that are contained in the Sowing Plan
        val fieldsOfFarm = data.getMap().getFieldsOfFarm(farmID)
        val fieldIdsOfFarm = fieldsOfFarm.map { it.getID() }
        val planFieldIDs = fieldIdsOfFarm.intersect(locations.toSet())

        return planFieldIDs.toList()
    }

    /**
     * Validate the fully expanded locations of a sowing plan.
     *
     * Checks performed:
     * - Location list is non-empty
     * - All locations are fields owned by the farm
     * - No duplicate field IDs
     *
     * @param locations List of target tile IDs (expected to be field IDs).
     * @param plantType Requested field plant.
     * @param farmID Owning farm ID.
     * @return `true` if the plan targets are valid for the given farm and plant.
     */
    fun validatePlan(locations: List<Int>, farmID: Int): Boolean {
        val map = data.getMap()

        // Check that plan has locations and all locations are
        val validationHelper = locations.isEmpty() ||
            map.getFarmFieldsByID(farmID).map { it.getID() }.containsAll(locations) == false

        // Check that locations tile ids are from the farm
        if (validationHelper) return false

        // Checking that fields ids are unique
        if (locations.size != locations.distinct().size) return false

        return true
    }

    /**
     * Verify that the farm JSON uses the exact key sets mandated by the specification
     * and that all nested objects (machines, sowing plans) also satisfy their key-set
     * constraints.
     *
     * @param farm Raw farm JSON.
     * @return `true` if the farm and all nested objects have exactly the expected keys.
     */
    private fun validateKeySetFarm(farm: JSONObject): Boolean {
        // Checking farm KeySet
        val keySetSize = farm.keySet().size
        if (keySetSize != ParserConstants.FARM_KEY_SET_SIZE) {
            // JSONObject has more or less keys than required
            return false
        }

        // Checking the Key Set for machines
        val machinesJSONArray = farm.getJSONArray("machines")
        val machineObjectsList = mutableListOf<JSONObject>()
        for (i in 0 until machinesJSONArray.length()) {
            val machineObject = machinesJSONArray.getJSONObject(i)
            machineObjectsList.add(machineObject)
        }
        var result = true
        for (machineJSONObject in machineObjectsList) {
            result = validateKeySetMachine(machineJSONObject)
            if (result == false) return false
        }

        // Checking the KeySet for Farms
        val sowingPlanJSONArray = farm.getJSONArray("sowingPlans")
        if (sowingPlanJSONArray.length() == 0) return true // No Sowing Plans is ok
        val sowingPlanObjectsList = mutableListOf<JSONObject>()
        for (i in 0 until sowingPlanJSONArray.length()) {
            val sowingPlanObject = sowingPlanJSONArray.getJSONObject(i)
            sowingPlanObjectsList.add(sowingPlanObject)
        }
        for (sowingPlanJSONObject in sowingPlanObjectsList) {
            result = validateKeySetSowingPlan(sowingPlanJSONObject)
            if (result == false) return false
        }

        // Everything Checked and ok
        return true
    }

    /**
     * Check that a machine JSON object has exactly [ParserConstants.MACHINE_KEY_SET_SIZE] keys.
     *
     * @param machineJSON Raw machine JSON.
     * @return `true` if the key count matches the expected count.
     */
    private fun validateKeySetMachine(machineJSON: JSONObject): Boolean {
        val keySetSize = machineJSON.keySet().size
        return keySetSize == ParserConstants.MACHINE_KEY_SET_SIZE
    }

    /**
     * Check that a sowing plan JSON object uses the correct key set depending on its shape:
     * - Field-list variant → [ParserConstants.SOWING_PLAN_FIELDS_TYPE_KEY_SET_SIZE]
     * - Area (location+radius) variant → [ParserConstants.SOWING_PLAN_RADIUS_TYPE_KEY_SET_SIZE]
     *
     * @param sowingPlanJSON Raw plan JSON.
     * @return `true` if the key count matches the expected variant.
     */
    private fun validateKeySetSowingPlan(sowingPlanJSON: JSONObject): Boolean {
        val keySetSize = sowingPlanJSON.keySet().size
        return if (sowingPlanJSON.has(ParserConstants.FIELDS)) {
            keySetSize == ParserConstants.SOWING_PLAN_FIELDS_TYPE_KEY_SET_SIZE
        } else {
            keySetSize == ParserConstants.SOWING_PLAN_RADIUS_TYPE_KEY_SET_SIZE
        }
    }
}
