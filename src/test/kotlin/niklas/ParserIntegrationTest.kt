package niklas

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.parser.Parser
import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ParserIntegrationTest {
    private val data = SimulationData(1, 1)
    private val parser = Parser(data)

    fun helperJSONToObject(mapPath: String, farmsPath: String, scenarioPath: String):
        Triple<JSONObject, JSONObject, JSONObject> {
        val mapFile = File(mapPath)
        val farmsFile = File(farmsPath)
        val scenarioFile = File(scenarioPath)
        val mapObject = JSONObject(mapFile.readText(Charsets.UTF_8))
        val farmsObject = JSONObject(farmsFile.readText(Charsets.UTF_8))
        val scenarioObject = JSONObject(scenarioFile.readText(Charsets.UTF_8))
        return Triple(mapObject, farmsObject, scenarioObject)
    }

    @Test
    @DisplayName("Big Map, many incidents, 2 farms, several ticks, several sowing plans.")
    fun testBigTestParser() {
        val mapPath = "src/systemtest/resources/niklas/mapBigSystemTestFullNiklas.json"
        val farmsPath = "src/systemtest/resources/niklas/farmsBigSystemTestFullNiklas.json"
        val scenarioPath = "src/systemtest/resources/niklas/scenarioBigSystemTestFullNiklas.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("A Farm has elements in the key fields, that belong to another farm. It's invalid.")
    fun testFarmClaimsFieldOfOtherFarmParser() {
        val mapPath = "src/systemtest/resources/niklas/mapFarmClaimsFieldOfOtherFarm.json"
        val farmsPath = "src/systemtest/resources/niklas/farmFarmClaimsFieldOfOtherFarm.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("Scenario should be invalid. City expansion is besides FOREST.")
    fun testCityExpansionBesidesForestParser() {
        val mapPath = "src/systemtest/resources/niklas/mapCityExpansionBesidesForest.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/systemtest/resources/niklas/scenarioCityExpansionBesidesForest.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName(
        "Tests a BROKEN_MACHINE incident. " +
            "The Broken Machine is going to miss HARVESTING of the APPLE plantation. Full log output."
    )
    fun testBrokenMachineFullTestParser() {
        val mapPath = "src/systemtest/resources/niklas/mapBrokenMachine.json"
        val farmsPath = "src/systemtest/resources/niklas/farmsBrokenMachine.json"
        val scenarioPath = "src/systemtest/resources/niklas/scenarioBrokenMachines.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("We have no farms")
    fun testNoFarmsParser() {
        val mapPath = "src/systemtest/resources/niklas/mapBrokenMachine.json"
        val farmsPath = "src/test/kotlin/niklas/usefulJsons/emptyFarmsArray.json"
        val scenarioPath = "src/systemtest/resources/niklas/scenarioBrokenMachines.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("The FARMSTEAD of one farm has a FIELD of another farm as neighbour")
    fun testFarmsteadHasOtherFarmsFieldAsNeighbourParser() {
        val mapPath = "src/systemtest/resources/niklas/mapFarmsteadHasOtherFarmsFieldAsNeighbour.json"
        val farmsPath = "src/systemtest/resources/niklas/farmsFarmsteadHasOtherFarmsFieldAsNeighbour.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("No tiles in the map. Should be invalid.")
    fun testNoTilesParser() {
        val mapPath = "src/test/kotlin/niklas/usefulJsons/noTiles.json"
        val farmsPath = "src/systemtest/resources/niklas/farmsFarmsteadHasOtherFarmsFieldAsNeighbour.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("Village is besides Forest.")
    fun testVillageBesidesForestParser() {
        val mapPath = "src/test/kotlin/niklas/usefulJsons/mapVillageBesidesForest.json"
        val farmsPath = "src/systemtest/resources/niklas/farmsFarmsteadHasOtherFarmsFieldAsNeighbour.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("Valid. Has a small Map.")
    fun testSmallMapParser() {
        val mapPath = "src/test/kotlin/niklas/usefulJsons/smallMap.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("Valid. Has the example map, farms and scenario.")
    fun testExampleParser() {
        val mapPath = "src/systemtest/resources/example/map.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("Valid. A Cloud Creation happens on small map.")
    fun testCloudCreationParser() {
        val mapPath = "src/systemtest/resources/example/map.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/test/kotlin/niklas/usefulJsons/scenarioSimpleCloudCreation.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("Valid. Two Cloud Creations happen (don't intersect) on small map in same tick.")
    fun testTwoCloudCreationsValidParser() {
        val mapPath = "src/systemtest/resources/example/map.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/test/kotlin/niklas/usefulJsons/scenarioTwoCloudCreationsValid.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("Invalid. Two Cloud Creations happen (intersect) on small map in same tick.")
    fun testTwoCloudCreationsInvalidParser() {
        val mapPath = "src/systemtest/resources/example/map.json"
        val farmsPath = "src/systemtest/resources/example/farms.json"
        val scenarioPath = "src/test/kotlin/niklas/usefulJsons/scenarioTwoCloudCreationsInvalid.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }

    @Test
    @DisplayName("Valid. Two city expansions happen besides each other.")
    fun testTwoCityExpansionValidParser() {
        val mapPath = "src/test/kotlin/niklas/usefulJsons/mapTwoCityExpansionValid.json"
        val farmsPath = "src/test/kotlin/niklas/usefulJsons/farmsTwoCityExpansionsValid.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertTrue(result)
    }

    @Test
    @DisplayName("Invalid. the farms json has more keys than allowed.")
    fun testFarmHasMoreKeysThenAllowedParser() {
        val mapPath = "src/systemtest/resources/example/map.json"
        val farmsPath = "src/test/kotlin/niklas/usefulJsons/farmsFarmHasMoreKeysThenAllowed.json"
        val scenarioPath = "src/systemtest/resources/example/scenario.json"
        val (mapObject, farmsObject, scenarioObject) =
            helperJSONToObject(mapPath, farmsPath, scenarioPath)
        val result = parser.parse(
            mapObject,
            farmsObject,
            scenarioObject,
            "mapBigSystemTestFullNiklas.json",
            "farmsBigSystemTestFullNiklas.json",
            "scenarioBigSystemTestFullNiklas.json"
        )
        assertFalse(result)
    }
}
