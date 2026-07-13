package de.unisaarland.cs.se.selab
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.Simulator
import de.unisaarland.cs.se.selab.parser.Parser
import de.unisaarland.cs.se.selab.view.Logger
import org.json.JSONObject
import java.io.File
import java.io.PrintWriter

val writer = PrintWriter(System.out, true)

/**
 Main Function
 **/
fun main(args: Array<String>) {
    val maxTick = "--max_ticks"
    val loglevel = "--log_level"
    if (args.isEmpty() || args.contains("--help")) {
        printHelp()
        return
    }

    // Parse args as map: flag -> value
    val argMap = parseArguments(args)

    // Assign variables
    val mapPath = argMap["--map"].orEmpty()
    val farmsPath = argMap["--farms"].orEmpty()
    val scenarioPath = argMap["--scenario"].orEmpty()
    val maxTicks = (argMap[maxTick] ?: "0").toInt()
    val startYearTick = argMap["--start_year_tick"]?.toIntOrNull() ?: 1
    val logLevel = argMap[loglevel] ?: "IMPORTANT"
    val outPath = argMap["--out"] ?: "stdout"

    // Simulation parameters are parsed and validated. Start your simulator here:
    Logger.setLogLevel(logLevel)
    Logger.setFile(outPath)
    val data = SimulationData(maxTicks, startYearTick)
    val parser = Parser(data)
    // Get Paths
    val mapFile = File(mapPath)
    val farmsFile = File(farmsPath)
    val scenarioFile = File(scenarioPath)
    if (!parser.parse(
            JSONObject(mapFile.readText(Charsets.UTF_8)),
            JSONObject(farmsFile.readText(Charsets.UTF_8)),
            JSONObject(scenarioFile.readText(Charsets.UTF_8)),
            mapFile.name,
            farmsFile.name,
            scenarioFile.name
        )
    ) {
        return
    }
    val simulator = Simulator(data)
    simulator.simulate()
    return
}

private fun parseArguments(args: Array<String>): MutableMap<String, String> {
    val argMap = mutableMapOf<String, String>()
    var i = 0
    while (i < args.size) {
        val arg = args[i]
        if (arg.startsWith("--")) {
            val nextIndex = i + 1
            if (nextIndex < args.size && !args[nextIndex].startsWith("--")) {
                argMap[arg] = args[nextIndex]
                i += 2
            }
        }
    }
    return argMap
}

private fun printHelp() {
    writer.println(
        """
            Usage:
              --map <path>          Path to the map. (required)
              --farms <path>        Path to the file with farm info. (required)
              --scenario <path>     Path to the scenario file. (required)
              --start_year_tick <1-24>  Tick to start within a year (optional, default=1)
              --max_ticks <int>     Max simulation ticks, <= 1000 (required)
              --log_level <DEBUG|INFO|IMPORTANT>  Log level (required)
              --out <path>          Output file path, default 'stdout' (optional)
              --help                Show this help message
        """.trimIndent()
    )
    return
}
