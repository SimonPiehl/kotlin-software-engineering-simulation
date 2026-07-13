package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.ValidationFailure
import com.github.erosb.jsonsKema.Validator
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Incident
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.SowingPlan
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.view.Logger
import org.json.JSONArray
import org.json.JSONObject

/**
 * High‑level JSON parser for the simulation input: **map**, **farms**, and **scenario**.
 *
 * Responsibilities:
 * - Validate each top‑level JSON document against its JSON Schema (via json‑sKema).
 * - Delegate element parsing to specialized parsers
 * (e.g., [TileParser], [FarmParser], [CloudParser], [IncidentParser]).
 * - Perform cross‑document semantic validation (e.g., unique IDs/names, scenario constraints).
 *
 * The parser is fail‑fast: upon the first validation or parsing error, it logs the corresponding failure
 * and returns `false` without applying partial state beyond what prior steps committed.
 *
 * @property data shared simulation state; used for lookups and registration during parsing.
 */
class Parser(private val data: SimulationData) {

    /**
     * Parse the three simulation files in order: **map → farms → scenario**.
     *
     * For each file, it calls a function that validates the JSON against its schema, parses it, runs
     * checks, and based on that this function logs success/failure using [Logger]. The function returns `true`
     * only if *all* three JSONObjects parse and validate successfully.
     *
     * @param map The map JSONObject.
     * @param farms The farms JSONObject.
     * @param scenario The scenario JSONObject.
     * @param mapString File name of map for logging (e.g., "map.json").
     * @param farmString File name of farms for logging (e.g., "farms.json").
     * @param scenarioString File name of scenario for logging (e.g., "scenario.json").
     * @return `true` if all JSONObjects were validated, parsed, and cross‑validated; `false` otherwise.
     */
    fun parse(
        map: JSONObject,
        farms: JSONObject,
        scenario: JSONObject,
        mapString: String,
        farmString: String,
        scenarioString: String
    ): Boolean {
        var result: Boolean = parseMap(map)
        if (result == false) {
            Logger.logParsingFailed(mapString)
            return false
        }
        Logger.logParsingSuccessful(mapString)
        result = parseFarms(farms)
        if (result == false) {
            Logger.logParsingFailed(farmString)
            return false
        }
        Logger.logParsingSuccessful(farmString)
        result = parseScenario(scenario)
        if (result == false) {
            Logger.logParsingFailed(scenarioString)
            return false
        }
        Logger.logParsingSuccessful(scenarioString)
        return true
    }

    /**
     * Validate and parse the **map** JSONObject, calls the TileParser to parse it's tiles,
     * and runs map checks.
     *
     * Steps:
     * 1. Validate JSON against `schema/map.schema`.
     * 2. Parse all tiles using [TileParser].
     * 3. Run validation using: [validateMap] and [validateOwnership].
     *
     * @param map map JSONObject (must contain a non‑empty `tiles` array).
     * @return `true` if the map is schema‑valid, tiles are parsed, and validations pass; `false` otherwise.
     */
    private fun parseMap(map: JSONObject): Boolean {
        // 1) Load the schema from classpath resources (src/main/resources/schema/map.schema)
        val schema = SchemaLoader.forURL("classpath:/schema/map.schema").load()

        // 2) Convert your org.json JSONObject into json-sKema’s JsonValue
        val instance = JsonParser(map.toString()).parse()

        // 3) Validate
        val failure: ValidationFailure? = Validator.forSchema(schema).validate(instance)

        // 4) Check if validation failed. If yes return false.
        // If failure is unequal to null it failed.
        if (failure != null) {
            return false
        }

        // Parsing the tiles
        val tileParser = TileParser(data)
        val tilesArray: JSONArray = map.getJSONArray("tiles")

        if (tilesArray.length() == 0) {
            return false
        }

        var result: Boolean
        for (i in 0 until tilesArray.length()) {
            val tileJSON = tilesArray.getJSONObject(i)
            result = tileParser.parseOneTile(tileJSON)
            if (result == false) {
                return false
            }
        }

        // Map is now created
        // Validate the Map
        return validateMap() && validateOwnership()
    }

    /**
     * Checks adjacency constraints for every tile via [validateAdjoiningTiles].
     *
     * @return `true` if all tiles satisfy adjacency rules; `false` otherwise.
     */
    private fun validateMap(): Boolean {
        val map = data.getMap()
        for (tile in map.getAllTiles()) {
            if (!validateAdjoiningTiles(map, tile)) {
                return false
            }
        }

        return true
    }

    /**
     * Validates that the adjoining tiles around a given tile are allowed
     * based on the type of the tile.
     *
     * @param map the [Map] containing all tiles
     * @param tile the [Tile] to validate
     * @return `true` if all adjoining tiles are valid, `false` otherwise
     */
    private fun validateAdjoiningTiles(map: Map, tile: Tile): Boolean {
        val coordinate = tile.getCoordinate()
        val adjoiningTilesCoordinates = coordinate.getNeighbours(1)
        val adjoiningTiles = map.getTilesByCoordinates(adjoiningTilesCoordinates)
        val allowedNeighbors = getAllowedNeighbors(tile.getTileType())
        return adjoiningTiles.all { it.getTileType() in allowedNeighbors }
    }

    /**
     * Return the set of allowed neighbor tile types for a given [TileType].
     *
     * @param tileType The center tile type.
     * @return a set of permissible neighboring types.
     */
    private fun getAllowedNeighbors(tileType: TileType): Set<TileType> = when (tileType) {
        TileType.FARMSTEAD -> TileType.entries.filter { it != TileType.MEADOW && it != TileType.FARMSTEAD }.toSet()
        TileType.FIELD -> TileType.entries.toSet()
        TileType.FOREST -> TileType.entries.filter { it != TileType.VILLAGE }.toSet()
        TileType.MEADOW -> TileType.entries.filter { it != TileType.FARMSTEAD && it != TileType.MEADOW }.toSet()
        TileType.PLANTATION -> TileType.entries.toSet()
        TileType.ROAD -> TileType.entries.toSet()
        TileType.VILLAGE -> TileType.entries.filter { it != TileType.FOREST }.toSet()
    }

    /**
     * Validate and parse the **farms** document, store farms, and enforce global uniqueness.
     *
     * Steps:
     * 1. Validate JSON against `schema/farms.schema`.
     * 2. Parse each farm with [FarmParser].
     * 3. Enforce global uniqueness constraints via [validateFarmsAndMachinesUniqueIDAndName].
     *
     * @param farms farms JSONObject (must contain a non‑empty `farms` array).
     * @return `true` if schema validation and all farm parses succeed and uniqueness holds; `false` otherwise.
     */
    private fun parseFarms(farms: JSONObject): Boolean {
        // 1) Load the schema from classpath resources (src/main/resources/schema/farms.schema)
        val schema = SchemaLoader.forURL("classpath:/schema/farms.schema").load()

        // 2) Convert your org.json JSONObject into json-sKema’s JsonValue
        val instance = JsonParser(farms.toString()).parse()

        // 3) Validate
        val failure: ValidationFailure? = Validator.forSchema(schema).validate(instance)

        // 4) Check if validation failed. If yes return false.
        // If failure is unequal to null it failed.
        if (failure != null) {
            return false
        }

        // Parsing farms
        val farmParser = FarmParser(data)
        val farmsArray = farms.getJSONArray("farms")

        if (farmsArray.length() == 0) {
            return false
        }

        var result: Boolean
        for (i in 0 until farmsArray.length()) {
            val farmJSON = farmsArray.getJSONObject(i)
            result = farmParser.parseOneFarm(farmJSON)
            if (result == false) {
                return false
            }
        }

        return validateFarmsAndMachinesUniqueIDAndName()
    }

    /**
     * Enforce global uniqueness across entities introduced by the farms file.
     *
     * Checks:
     * - Farm IDs and names are unique across all farms.
     * - Machine IDs and names are unique across all machines (from all farms).
     * - SowingPlan IDs are unique across all farms.
     *
     * @return `true` if all uniqueness constraints hold; `false` otherwise.
     */
    private fun validateFarmsAndMachinesUniqueIDAndName(): Boolean {
        val farms = data.getFarms()
        val allMachines = farms.flatMap { it.getMachines() }
        val sowingPlans = mutableListOf<SowingPlan>()
        val allFarms = data.getFarms()
        allFarms.forEach { farm ->
            sowingPlans += farm.getPlans()
        }

        // Check farm ids unique
        val farmIds = farms.map { it.getID() }
        val uniqueFarmIds = farmIds.size == farmIds.distinct().size
        if (uniqueFarmIds == false) return false

        // Check farm names unique
        val farmNames = farms.map { it.getName() }
        val farmNamesAreUnique = farmNames.size == farmNames.distinct().size
        if (farmNamesAreUnique == false) return false

        // Check machine ids unique
        val machineIds = allMachines.map { it.getID() }
        val uniqueMachineIds = machineIds.size == machineIds.distinct().size
        if (uniqueMachineIds == false) return false

        // Check sowing Plan unique ID
        val sowingPlanIds = sowingPlans.map { it.getID() }
        val uniquePlanIds = sowingPlanIds.size == sowingPlanIds.distinct().size
        if (!uniquePlanIds) return false

        // Check machine names unique
        val machineNames = allMachines.map { it.getName() }
        val machineNamesAreUnique = machineNames.size == machineNames.distinct().size
        return machineNamesAreUnique != false
    }

    /**
     * Validate that farmstead ownership boundaries are respected.
     *
     * Every tile adjoining a FARMSTEAD must either be unowned or share the same owner.
     *
     * @return `true` if ownership boundaries are consistent; `false` otherwise.
     */
    private fun validateOwnership(): Boolean {
        val map = data.getMap()
        val allTiles = map.getAllTiles()

        allTiles.forEach { tile ->
            if (tile.getTileType() == TileType.FARMSTEAD) {
                val owner = map.getFarmIDbyTile(tile)
                val coordinate = tile.getCoordinate()
                val adjoiningCoordinates = coordinate.getNeighbours(1)
                val adjoiningTiles = map.getTilesByCoordinates(adjoiningCoordinates)
                val result = adjoiningTiles.all { neighbour ->
                    val neighbourOwner = map.getFarmIDbyTile(neighbour)
                    neighbourOwner == null || neighbourOwner == owner
                }

                if (!result) return false
            }
        }

        return true
    }

    /**
     * Validate and parse the **scenario** document, then enforce scenario‑level rules.
     *
     * Steps:
     * 1. Validate JSON against `schema/scenario.schema`.
     * 2. Parse all clouds and incidents via [CloudParser] and [IncidentParser].
     * 3. Run cross‑incident validations via [validateScenario].
     *
     * @param scenario scenario JSONObject (must contain arrays `clouds` and `incidents`).
     * @return `true` if schema validation, parsing, and scenario validation succeed; `false` otherwise.
     */
    private fun parseScenario(scenario: JSONObject): Boolean {
        // 1) Load the schema from classpath resources (src/main/resources/schema/farms.schema)
        val schema = SchemaLoader.forURL("classpath:/schema/scenario.schema").load()

        // 2) Convert your org.json JSONObject into json-sKema’s JsonValue
        val instance = JsonParser(scenario.toString()).parse()

        // 3) Validate
        val failure: ValidationFailure? = Validator.forSchema(schema).validate(instance)

        // 4) Check if validation failed. If yes return false.
        // If failure is unequal to null it failed.
        if (failure != null) {
            return false
        }

        // Parsing farms
        val cloudParser = CloudParser(data)
        val incidentParser = IncidentParser(data)
        val cloudsArray = scenario.getJSONArray("clouds")
        val incidentsArray = scenario.getJSONArray("incidents")

        // Parsing Clouds
        var result: Boolean
        for (i in 0 until cloudsArray.length()) {
            val cloudJSON = cloudsArray.getJSONObject(i)
            result = cloudParser.parseCloud(cloudJSON)
            if (result == false) {
                return false
            }
        }

        // Parsing Incidents
        for (i in 0 until incidentsArray.length()) {
            val incidentJSON = incidentsArray.getJSONObject(i)
            result = incidentParser.parseOneIncident(incidentJSON)
            if (result == false) {
                return false
            }
        }
        return validateScenario()
    }

    /**
     * Run scenario‑level semantic validation across all parsed incidents and clouds.
     *
     * Validations include (in order):
     * - Cloud locations are unique: [validateCloudsLocationsUnique].
     * - City expansions are never adjacent to FOREST tiles: [validateCityExpansionNotBesidesForest].
     * - City expansions occur next to a village and on ROAD or FIELD tiles: [validateCityExpansionNextToVillage].
     * - Sowing plans still reference fields that exist after expansions: [validatePlansContainingFields].
     * - Cloud creations avoid all‑village areas at creation time: [validateCloudCreationNotOnVillage].
     * - Cloud creation areas at the same tick do not intersect: [cloudCreationsDontIntersect].
     * - Droughts cover at least one FIELD or PLANTATION: [validateDrought].
     *
     * @return `true` if all validations pass; `false` on the first failure.
     */
    private fun validateScenario(): Boolean {
        val map = data.getMap()
        val incidents = data.getIncidents().values.flatten()
        val orderedIncidents = incidents.sortedWith(compareBy<Incident> { it.getTick() }.thenBy { it.getID() })
        val result1 = validateCloudsLocationsUnique(data.getClouds()) &&
            validateCityExpansionNotBesidesForest(map, orderedIncidents)
        var result = validateCityExpansionNextToVillage(map, orderedIncidents)
        if (result == false || result1 == false) return false
        result = validatePlansContainingFields(map, orderedIncidents)
        if (result == false) return false
        result = validateCloudCreationNotOnVillage(map, orderedIncidents)
        if (result == false) return false
        result = cloudCreationsDontIntersect(map, orderedIncidents)
        if (result == false) return false
        result = validateDrought(map, orderedIncidents)
        return result != false
    }

    /**
     * Ensure each [CityExpansion] occurs on an existing tile that is either ROAD or FIELD and
     * is adjacent to at least one VILLAGE tile.
     *
     * @param map The global map (copied for safe simulation where needed).
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @return `true` if every city expansion satisfies placement rules; `false` otherwise.
     */
    private fun validateCityExpansionNextToVillage(map: Map, orderedIncidents: List<Incident>): Boolean {
        val allTiles = map.getAllTilesCopy()
        val allCityExpansions = orderedIncidents.filterIsInstance<CityExpansion>()
        var result: Boolean

        // Checking, that the tile of the City Expansion exists
        for (cityExpansion in allCityExpansions) {
            result = map.doesTileExist(cityExpansion.getLocation())
            if (result == false) {
                // Tile where CityExpansion is, does not exist
                return false
            }
        }

        // Checking tile where expansion happens is of type ROAD or FIELD and is next to a village.
        for (cityExpansion in allCityExpansions) {
            val tile = allTiles.first { it.getID() == cityExpansion.getLocation() }
            // Check if tile is of type ROAD or FIELD
            if (tile.getTileType() != TileType.ROAD && tile.getTileType() != TileType.FIELD) return false

            // Checking if it's next to a village
            val tileCoordinate = tile.getCoordinate()
            val neighbourCoords = tileCoordinate.getNeighbours(1).toSet()
            val neighbourTiles = allTiles.filter { it.getCoordinate() in neighbourCoords }
            val hasVillageNeighbour = neighbourTiles.any { it.getTileType() == TileType.VILLAGE }
            if (!hasVillageNeighbour) return false

            // Update Tile
            tile.changeToVillage()
        }
        return true
    }

    /**
     * Validate that each farm's sowing plans still target existing fields (after simulated city expansions)
     * and that at least one targeted field can grow the plan's plant type via [validatePlanContainsFields].
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id), used to simulate city expansion that occur before plans.
     * @return `true` if all plans remain valid; `false` otherwise.
     */
    private fun validatePlansContainingFields(map: Map, orderedIncidents: List<Incident>): Boolean {
        val farmsList = data.getFarms()

        var result: Boolean
        for (farm in farmsList) {
            val sowingPlans = farm.getPlans()
            for (plan in sowingPlans) {
                result = validatePlanContainsFields(map, orderedIncidents, farm, plan)
                if (result == false) return false
            }
        }
        return true
    }

    /**
     * Validate a single [SowingPlan] against the map state after simulating all [CityExpansion]s
     * (i.e., removing fields that have become villages prior to the plan's start).
     * The plan is valid if it still targets at least one of the farm's fields and that field supports
     * the plan's plant type.
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @param farm The farm that owns the plan.
     * @param plan The plan to check.
     * @return `true` if the plan references at least one viable field; `false` otherwise.
     */
    private fun validatePlanContainsFields(
        map: Map,
        orderedIncidents: List<Incident>,
        farm: Farm,
        plan: SowingPlan
    ): Boolean {
        val allCityExpansions = orderedIncidents.filterIsInstance<CityExpansion>()

        // Get the farm fields
        val fieldsOfFarm = map.getFieldsOfFarm(farm.getID())
        val farmFieldsIDs = fieldsOfFarm.map { it.getID() }.toMutableList()

        // Simulate the CityExpansions
        // (remove all FIELDS of the farm, that get turned into VILLAGE before Plan StartTick)
        for (cityExpansion in allCityExpansions) {
            farmFieldsIDs.remove(cityExpansion.getLocation())
        }

        // Check if SowingPlanLocations contain a field
        val planFieldIDs = plan.getLocations()
        val planContainsFields = planFieldIDs.intersect(farmFieldsIDs.toSet())
        val planContainsField = planContainsFields.isNotEmpty()

        // Checking if SowingPlanLocations contain a Field with the PlantType of the plan
        val planFieldsTiles = fieldsOfFarm.filter { it.getID() in planContainsFields }
        val planFieldsGrowables = planFieldsTiles.map { it.getGrowable() }
        val planFieldsPlantTypes = planFieldsGrowables.map {
            it?.getPossiblePlants()
                ?: error("Growable should never be null")
        }.flatten()
        val planFieldsHavePlantType = planFieldsPlantTypes.contains(plan.getPlantToSow())

        return planContainsField && planFieldsHavePlantType
    }

    /**
     * Ensure each [CloudCreation] affects at least one tile that is **not** VILLAGE at the time of creation.
     * This is evaluated after simulating any prior [CityExpansion] incidents in the ordered list.
     * It checks it via [helperValidateCloudCreationNotOnVillage].
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @return `true` if every cloud creation covers at least one non‑village tile; `false` otherwise.
     */
    private fun validateCloudCreationNotOnVillage(map: Map, orderedIncidents: List<Incident>): Boolean {
        val allCloudCreations = orderedIncidents.filterIsInstance<CloudCreation>()

        var result: Boolean
        for (cloudCreation in allCloudCreations) {
            result = helperValidateCloudCreationNotOnVillage(map, orderedIncidents, cloudCreation)
            if (result == false) return false
        }
        return true
    }

    /**
     * Helper for [validateCloudCreationNotOnVillage]. Simulates prior city expansions and verifies
     * the cloud creation area contains at least one non‑village tile.
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @param cloudCreation The cloud creation incident to check.
     * @return `true` if the affected area is not entirely village; `false` otherwise.
     */
    private fun helperValidateCloudCreationNotOnVillage(
        map: Map,
        orderedIncidents: List<Incident>,
        cloudCreation: CloudCreation
    ): Boolean {
        val result: Boolean = map.doesTileExist(cloudCreation.getLocation())
        if (result == false) return false

        // Simulate village expansion
        val idx = orderedIncidents.indexOf(cloudCreation)
        // Getting all CityExpansions prior to the Cloud Creation
        val priorCityExpansions = orderedIncidents
            .subList(0, idx) // everything before that index
            .filterIsInstance<CityExpansion>()

        val allTiles = map.getAllTilesCopy()
        val allNotVillageTiles = allTiles.filter { it.getTileType() != TileType.VILLAGE }
        val allNotVillageTilesIDs = allNotVillageTiles.map { it.getID() }.toMutableList()

        for (cityExpansion in priorCityExpansions) {
            allNotVillageTilesIDs.remove(cityExpansion.getLocation())
        }

        // City expansion simulation done

        // Check that CloudCreation locations have at least one tile that isn't village
        val cloudCreationTile = map.getTileByID(cloudCreation.getLocation()) ?: throw NoSuchElementException(
            "Tile ${cloudCreation.getLocation()} should exist"
        )

        val tileCoordinate = cloudCreationTile.getCoordinate()
        val neighbourCoords = tileCoordinate.getNeighbours(cloudCreation.getRadius()).toSet()
        val cloudCreationTiles = allTiles.filter { it.getCoordinate() in neighbourCoords }.toMutableList()
        cloudCreationTiles.add(cloudCreationTile)
        val cloudCreationTilesIDs = cloudCreationTiles.map { it.getID() }
        return allNotVillageTilesIDs.intersect(cloudCreationTilesIDs.toSet()).isNotEmpty()
    }

    /**
     * Validate that each [Drought] affects at least one FIELD or PLANTATION tile at the time it occurs.
     * Prior city expansions are simulated so fields turned into villages no longer count.
     * it does so via [helperValidateDrought].
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @return `true` if every drought covers at least one field/plantation; `false` otherwise.
     */
    private fun validateDrought(map: Map, orderedIncidents: List<Incident>): Boolean {
        val allDroughts = orderedIncidents.filterIsInstance<Drought>()

        var result: Boolean
        for (drought in allDroughts) {
            result = helperValidateDrought(map, orderedIncidents, drought)
            if (result == false) return false
        }
        return true
    }

    /**
     * Helper for [validateDrought]. Simulates prior city expansions and verifies the drought area
     * intersects at least one FIELD or PLANTATION tile.
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id).
     * @param drought The drought incident to check.
     * @return `true` if the affected area contains at least one field/plantation; `false` otherwise.
     */
    private fun helperValidateDrought(map: Map, orderedIncidents: List<Incident>, drought: Drought): Boolean {
        val idx = orderedIncidents.indexOf(drought)
        // Getting all CityExpansions prior to the Drought
        val priorCityExpansions = orderedIncidents
            .subList(0, idx) // everything before that index
            .filterIsInstance<CityExpansion>()

        // Simulating City Expansions prior to Drought
        val allTiles = map.getAllTilesCopy()
        val allFieldOrPlantationTiles = allTiles.filter {
            it.getTileType() == TileType.FIELD ||
                it.getTileType() == TileType.PLANTATION
        }
        val allFieldOrPlantationTilesIDs = allFieldOrPlantationTiles.map { it.getID() }.toMutableList()

        for (cityExpansion in priorCityExpansions) {
            allFieldOrPlantationTilesIDs.remove(cityExpansion.getLocation())
        }

        // City expansion simulation done

        // Check that Drought locations have at least one tile that is Field or Plantation
        val droughtTile = map.getTileByID(drought.getLocation()) ?: throw NoSuchElementException(
            "Tile ${drought.getLocation()} should exist"
        )

        val tileCoordinate = droughtTile.getCoordinate()
        val neighbourCoords = tileCoordinate.getNeighbours(drought.getRadius()).toSet()
        val droughtTiles = allTiles.filter { it.getCoordinate() in neighbourCoords }.toMutableList()
        droughtTiles.add(droughtTile)
        val droughtTilesIDs = droughtTiles.map { it.getID() }
        return allFieldOrPlantationTilesIDs.intersect(droughtTilesIDs.toSet()).isNotEmpty()
    }

    /**
     * Checks that Cloud Creations that happen in the same Tick don't intersect in the tiles they affect.
     * Village tiles are ignored (they are never included in a cloud creation's affected set).
     *
     * @param map The map
     * @param orderedIncidents The Incidents ordered by Tick and then by id
     * @return `true` if no overlaps occur at the same tick; `false` otherwise.
     */
    private fun cloudCreationsDontIntersect(map: Map, orderedIncidents: List<Incident>): Boolean {
        val allCloudCreations = orderedIncidents.filterIsInstance<CloudCreation>()
        val tickList = allCloudCreations.map { it.getTick() }.distinct()
        val cloudCreationTilesIds = allCloudCreations.map { helperCloudCreationToTileIdsTheyAffect(map, it) }

        for (tick in tickList) {
            val cloudCreationsThisTick = allCloudCreations
                .withIndex()
                .filter { it.value.getTick() == tick }

            if (cloudCreationsThisTick.size <= 1) continue

            // List of Lists of Tile ids, of the Cloud Creations this Tick
            val tilesOfCloudCreationsThisTick = cloudCreationsThisTick.map { cloudCreationTilesIds[it.index] }

            val allTileIdsOfCloudCreationsThisTick = tilesOfCloudCreationsThisTick.flatten()

            val dontIntersect = allTileIdsOfCloudCreationsThisTick.size ==
                allTileIdsOfCloudCreationsThisTick.distinct().size

            if (dontIntersect == false) return false
        }
        return true
    }

    /**
     * Compute the list of tile IDs affected by a [CloudCreation] (location + radius), excluding any VILLAGE tiles.
     * The result is de‑duplicated.
     *
     * @param map The global map.
     * @param cloudCreation The cloud creation incident.
     * @return List of affected tile IDs (non‑village), distinct.
     */
    private fun helperCloudCreationToTileIdsTheyAffect(map: Map, cloudCreation: CloudCreation): List<Int> {
        // Calculate the Tile Ids the cloudCreation affects
        val cloudTile = map.getTileByID(cloudCreation.getLocation()) ?: error("This should never happen 2")
        val cloudTileCoordinate = cloudTile.getCoordinate()
        val cloudCreationCoordinates = cloudTileCoordinate
            .getNeighbours(cloudCreation.getRadius()).toMutableList()
        cloudCreationCoordinates.add(cloudTileCoordinate)
        val cloudCreationTiles = map.getTilesByCoordinates(cloudCreationCoordinates)
            .filter { it.getTileType() != TileType.VILLAGE }
        val cloudCreationTilesIDs = cloudCreationTiles.map { it.getID() }
        return cloudCreationTilesIDs.distinct()
    }

    /**
     * Ensure that at most one [Cloud] is created per starting tile.
     *
     * @param clouds All parsed clouds.
     * @return `true` if all cloud starting locations are unique; `false` otherwise.
     */
    private fun validateCloudsLocationsUnique(clouds: List<Cloud>): Boolean {
        val cloudsCoordinates = clouds.map { it.getLocation() }
        return cloudsCoordinates.size == cloudsCoordinates.distinct().size
    }

    /**
     * After simulating all [CityExpansion]s, verify that no FOREST tile is directly adjacent to a VILLAGE tile.
     *
     * @param map The global map.
     * @param orderedIncidents Incidents ordered by (tick, id) used to simulate expansions.
     * @return `true` if forests are never adjacent to villages; `false` otherwise.
     */
    private fun validateCityExpansionNotBesidesForest(map: Map, orderedIncidents: List<Incident>): Boolean {
        val allTiles = map.getAllTilesCopy()
        val allCityExpansions = orderedIncidents.filterIsInstance<CityExpansion>()

        // Simulate Village Expansions
        for (cityExpansion in allCityExpansions) {
            val tile = allTiles.find { it.getID() == cityExpansion.getLocation() }
                ?: error("This should never happen 8")
            tile.changeToVillage()
        }
        val allForestTiles = allTiles.filter { it.getTileType() == TileType.FOREST }
        val neighbourCoords = allForestTiles.map { it.getCoordinate().getNeighbours(1) }
            .flatten().distinct()
        val allAdjoiningTilesToForest = allTiles.filter { it.getCoordinate() in neighbourCoords }

        // Check if any adjoining Tiles are VILLAGE Tiles
        return allAdjoiningTilesToForest.none { it.getTileType() == TileType.VILLAGE }
    }
}
