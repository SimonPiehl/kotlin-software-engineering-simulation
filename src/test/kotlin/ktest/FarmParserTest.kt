package ktest

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.parser.Parser
import org.json.JSONObject
import java.io.File
import kotlin.test.Test

class FarmParserTest {
    private val mapPath = "src/test/kotlin/ktest/FarmParserjson/Map.json"
    private val scenario = "src/test/kotlin/ktest/FarmParserjson/Scenario.json"

    val data = SimulationData(100, 0)
    val mapFile = File(mapPath)
    val m = JSONObject(mapFile.readText(Charsets.UTF_8))
    val scenarioFile = File(scenario)
    val s = JSONObject(scenarioFile.readText(Charsets.UTF_8))
    val parser = Parser(data)

    @Test
    fun testNoUniqueFarmName() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/uniquefarmname.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
        val ff2 = File("src/test/kotlin/ktest/FarmParserjson/uniquefarmid.json")
        val f2 = JSONObject(ff2.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f2, s, "map", "farm", "scenario"))
    }

    @Test
    fun testNoUniqueMachineName() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/uniquemachinename.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
        val ff2 = File("src/test/kotlin/ktest/FarmParserjson/uniquemachineid.json")
        val f2 = JSONObject(ff2.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f2, s, "map", "farm", "scenario"))
        val ff3 = File("src/test/kotlin/ktest/FarmParserjson/uniquemachineother.json")
        val f3 = JSONObject(ff3.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f3, s, "map", "farm", "scenario"))
    }

    @Test
    fun testMachineOnFarmstead() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/machineonfarmstead.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testMachineOnOwnFarmstead() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/machineonownfarmstead.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasActions() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/hasaction.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasPlant() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/hasplant.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testSowOnOwn() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/sowown.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
        val ff2 = File("src/test/kotlin/ktest/FarmParserjson/sowown2.json")
        val f2 = JSONObject(ff2.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f2, s, "map", "farm", "scenario"))
        val ff3 = File("src/test/kotlin/ktest/FarmParserjson/sowown3.json")
        val f3 = JSONObject(ff3.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f3, s, "map", "farm", "scenario"))
    }

    @Test
    fun testSowPossible() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/sowpossible.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasAllFields() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/hasallfields.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f, s, "map", "farm", "scenario"))
        val ff2 = File("src/test/kotlin/ktest/FarmParserjson/hasallfields2.json")
        val f2 = JSONObject(ff2.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f2, s, "map", "farm", "scenario"))
        val ff3 = File("src/test/kotlin/ktest/FarmParserjson/hasallfields3.json")
        val f3 = JSONObject(ff3.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f3, s, "map", "farm", "scenario"))
        val ff4 = File("src/test/kotlin/ktest/FarmParserjson/hasallfields4.json")
        val f4 = JSONObject(ff4.readText(Charsets.UTF_8))
        assert(!parser.parse(m, f4, s, "map", "farm", "scenario"))
    }

    @Test
    fun testUniqueFarmName() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/uniquefarmname.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testUniqueMachineName() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/uniquemachinename.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testMachineOnFarmsteadt() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/machineonfarmstead.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testMachineOnOwnFarmsteadt() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/machineonownfarmstead.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasActionst() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/hasaction.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasPlantt() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/hasplant.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testSowOnOwnt() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/sowown.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testSowPossiblet() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/sowpossible.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }

    @Test
    fun testHasAllFieldst() {
        val ff = File("src/test/kotlin/ktest/FarmParserjson/t/hasallfields.json")
        val f = JSONObject(ff.readText(Charsets.UTF_8))
        assert(parser.parse(m, f, s, "map", "farm", "scenario"))
    }
}
