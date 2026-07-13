package de.unisaarland.cs.se.selab.view

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.util.LogLevel
import java.io.File
import java.io.PrintWriter

const val COMMA = ","

/**
 * Logger
 */
object Logger {
    private const val STANDARD_OUTPUT = "stdout"
    private var logLevel: LogLevel = LogLevel.DEBUG
    private var outputFile: String = STANDARD_OUTPUT

    private val PrintWriterStdout: PrintWriter = PrintWriter(System.out)
    private var PrintWriterFile: PrintWriter? = null

    // Set Functions

    /**
     * Sets file to print in
     *
     * @param filename new output file
     */
    fun setFile(filename: String) {
        if (filename.endsWith("stdout")) return
        outputFile = filename
        PrintWriterFile = PrintWriter(File(filename))
    }

    /**
     * Logs all messages either in stdout or in a file depending on output-filename
     *
     * @param message: Log Message that has to be printed
     */
    fun log(message: String) {
        if (outputFile == STANDARD_OUTPUT) {
            PrintWriterStdout.println(message)
            PrintWriterStdout.flush()
        } else {
            PrintWriterFile?.println(message)
            PrintWriterFile?.flush()
        }
    }

    /**
     * Set log level
     * which is per default DEBUG
     * @param logLevel
     */
    fun setLogLevel(logLevel: String) {
        when (logLevel) {
            "INFO" -> this.logLevel = LogLevel.INFO
            "IMPORTANT" -> this.logLevel = LogLevel.IMPORTANT
        }
    }

    /**
     * Log parsing successful
     *
     * @param filename
     */
    fun logParsingSuccessful(filename: String) {
        if (logLevel != LogLevel.IMPORTANT) {
            log("[INFO] Initialization Info: $filename successfully parsed and validated.")
        }
    }

    /**
     * Log parsing failed
     *
     * @param filename
     */
    fun logParsingFailed(filename: String) {
        log("[IMPORTANT] Initialization Info: $filename is invalid.")
    }

    /**
     * Log sim start
     *
     * @param yearTick
     */
    fun logSimStart(yearTick: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log("[INFO] Simulation Info: Simulation started at tick $yearTick within the year.")
        }
    }

    /**
     * Log sim end
     *
     * @param tick
     */
    fun logSimEnd(tick: Int) {
        log("[IMPORTANT] Simulation Info: Simulation ended at tick $tick.")
    }

    /**
     * Log tick start
     *
     * @param tick
     * @param yearTick
     */
    fun logTickStart(tick: Int, yearTick: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log("[INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.")
        }
    }

    /**
     * Log growable low moisture
     *
     * @param amountFields
     * @param amountPlantation
     */
    fun logGrowableLowMoisture(amountFields: Int, amountPlantation: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log(
                "[INFO] Soil Moisture: The soil moisture is below threshold in " +
                    "$amountFields FIELD and $amountPlantation PLANTATION tiles."
            )
        }
    }

    /**
     * Log cloud rain
     *
     * @param cloudID
     * @param amount
     * @param tileID
     */
    fun logCloudRain(cloudID: Int, amount: Int, tileID: Int) {
        log("[IMPORTANT] Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water.")
    }

    /**
     * Log cloud movement
     *
     * @param cloudID
     * @param amount
     * @param startTileID
     * @param endTileID
     */
    fun logCloudMovement(cloudID: Int, amount: Int, startTileID: Int, endTileID: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log(
                "[INFO] Cloud Movement: Cloud $cloudID with $amount L water moved " +
                    "from tile $startTileID to tile $endTileID."
            )
        }
    }

    /**
     * Log cloud sun on tile
     *
     * @param amountSun
     * @param startTileID
     */
    fun logCloudSunOnTile(amountSun: Int, startTileID: Int) {
        if (logLevel == LogLevel.DEBUG) {
            log(
                "[DEBUG] Cloud Movement: On tile $startTileID, the amount of sunlight is " +
                    "$amountSun."
            )
        }
    }

    /**
     * Log cloud merge
     *
     * @param cloudID1 From Tile
     * @param cloudID2 Moving to Tile
     * @param cloudIDNew
     * @param amount
     * @param duration
     * @param tileID
     */
    fun logCloudMerge(
        cloudFromTile: Int,
        cloudMovingToTile: Int,
        cloudIDNew: Int,
        amount: Int,
        duration: Int,
        tileID: Int
    ) {
        log(
            "[IMPORTANT] Cloud Union: Clouds $cloudFromTile and $cloudMovingToTile " +
                "united to cloud $cloudIDNew with $amount L water and duration $duration on tile $tileID."
        )
    }

    /**
     * Log cloud dissipate stuck
     *
     * @param cloudID
     * @param tileID
     */
    fun logCloudStuck(cloudID: Int, tileID: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log("[INFO] Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID.")
        }
    }

    /**
     * Log cloud dissipate rain or duration
     *
     * @param cloudID
     * @param tileID
     */
    fun logCloudDissipate(cloudID: Int, tileID: Int) {
        if (logLevel != LogLevel.IMPORTANT) {
            log("[INFO] Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID.")
        }
    }

    /**
     * Log cloud position
     *
     * @param cloudID
     * @param tileID
     * @param amountSunOnTile
     */
    fun logCloudPosition(cloudID: Int, tileID: Int, amountSunOnTile: Int) {
        if (logLevel == LogLevel.DEBUG) {
            log(
                "[DEBUG] Cloud Position: Cloud $cloudID is on tile $tileID, where the " +
                    "amount of sunlight is $amountSunOnTile."
            )
        }
    }

    /**
     * Log farm start
     *
     * @param farmID
     */
    fun logFarmStart(farmID: Int) {
        log("[IMPORTANT] Farm: Farm $farmID starts its actions.")
    }

    /**
     * Log all sowing plans this tick
     *
     * @param farmID
     * @param planIDs
     */
    fun logAllSowingPlansThisTick(farmID: Int, planIDs: List<Int>) {
        if (logLevel == LogLevel.DEBUG) {
            if (planIDs.isEmpty()) {
                log(
                    "[DEBUG] Farm: Farm $farmID has the following active sowing plans it " +
                        "intends to pursue in this tick: ."
                )
                return
            }
            val l = planIDs.sorted()
            val pl = l.joinToString(COMMA)
            log(
                "[DEBUG] Farm: Farm $farmID has the following active sowing plans it " +
                    "intends to pursue in this tick: $pl."
            )
        }
    }

    /**
     * Log farm action
     *
     * @param machineID
     * @param actionType
     * @param tileID
     * @param duration
     */
    fun logFarmAction(machineID: Int, actionType: Action, tileID: Int, duration: Int) {
        log(
            "[IMPORTANT] Farm Action: Machine $machineID performs $actionType on " +
                "tile $tileID for $duration days."
        )
    }

    /**
     * Log performed plan
     *
     * @param machineID
     * @param plant
     * @param planID
     */
    fun logPerformedPlan(machineID: Int, plant: PlantType, planID: Int) {
        log(
            "[IMPORTANT] Farm Sowing: Machine $machineID has sowed $plant according " +
                "to sowing plan $planID."
        )
    }

    /**
     * Log collected harvest
     *
     * @param machineID
     * @param amount
     * @param plant
     */
    fun logCollectedHarvest(machineID: Int, amount: Int, plant: PlantType) {
        log(
            "[IMPORTANT] Farm Harvest: Machine $machineID has collected $amount g of " +
                "$plant harvest."
        )
    }

    /**
     * Log machine return
     *
     * @param machineID
     * @param successful
     * @param shedTileID
     */
    fun logMachineReturn(machineID: Int, successful: Boolean, shedTileID: Int?) {
        if (successful && shedTileID != null) {
            log(
                "[IMPORTANT] Farm Machine: Machine $machineID is finished and returns to " +
                    "the shed at $shedTileID."
            )
        } else {
            log(
                "[IMPORTANT] Farm Machine: Machine $machineID is finished but failed to " +
                    "return."
            )
        }
    }

    /**
     * Log unload machine
     *
     * @param machineID
     * @param plant
     * @param amount
     */
    fun logUnloadMachine(machineID: Int, plant: PlantType, amount: Int) {
        log(
            "[IMPORTANT] Farm Machine: Machine $machineID unloads $amount g of " +
                "$plant harvest in the shed."
        )
    }

    /**
     * Log farm finished
     *
     * @param farmID
     */
    fun logFarmFinished(farmID: Int) {
        log("[IMPORTANT] Farm: Farm $farmID finished its actions.")
    }

    /**
     * Log incident
     *
     * @param incidentID
     * @param type
     * @param affectedTiles
     */
    fun logIncident(incidentID: Int, type: String, affectedTiles: MutableList<Int>) {
        if (affectedTiles.isEmpty()) {
            log(
                "[IMPORTANT] Incident: Incident $incidentID of type $type " +
                    "happened and affected tiles ."
            )
            return
        }
        affectedTiles.sort()
        val tilesFormatted = affectedTiles.joinToString(COMMA)
        log(
            "[IMPORTANT] Incident: Incident $incidentID of type $type " +
                "happened and affected tiles $tilesFormatted."
        )
    }

    /**
     * Log required action not performed
     *
     * @param tileID
     * @param actions
     */
    fun logRequiredActionNotPerformed(tileID: Int, actions: List<Action>) {
        if (logLevel == LogLevel.DEBUG) {
            if (actions.isEmpty()) {
                log(
                    "[DEBUG] Harvest Estimate: Required actions on tile $tileID were not " +
                        "performed: ."
                )
                return
            }
            log(
                "[DEBUG] Harvest Estimate: Required actions on tile $tileID were not " +
                    "performed: ${actions.joinToString(",")}."
            )
        }
    }

    /**
     * Log changed harvest estimate
     *
     * @param tileID
     * @param amount
     * @param plant
     */
    fun logChangedHarvestEstimate(tileID: Int, amount: Int, plant: PlantType) {
        if (logLevel != LogLevel.IMPORTANT) {
            log(
                "[INFO] Harvest Estimate: Harvest estimate on tile $tileID changed to " +
                    "$amount g of $plant."
            )
        }
    }

    /**
     * Log statistics are calculated
     *
     */
    fun logStatisticsAreCalculated() {
        log("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
    }

    /**
     * Log farm statistic
     *
     * @param farmID
     * @param amount
     */
    fun logFarmStatistic(farmID: Int, amount: Int) {
        log(
            "[IMPORTANT] Simulation Statistics: Farm $farmID collected $amount g of " +
                "harvest."
        )
    }

    /**
     * Log harvest of plant statistic
     *
     * @param plant
     * @param amount
     */
    fun logHarvestOfPlantStatistic(plant: PlantType, amount: Int) {
        log(
            "[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: " +
                "$amount g."
        )
    }

    /**
     * Log remaining estimate harvest
     *
     * @param amount
     */
    fun logRemainingEstimateHarvest(amount: Int) {
        log(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in " +
                "fields and plantations: $amount g."
        )
    }
}
