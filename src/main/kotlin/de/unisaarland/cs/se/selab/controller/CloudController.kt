package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.view.Logger

/**
 * Cloud controller - Responsible for Cloud Raining, Moving, Merging, Dissipating & Sunlight + Moisture on Growable
 *
 * @property data - specifies all properties in the current Simulation
 */
class CloudController(private val data: SimulationData) {
    /**
     * Cloud movement: Perform cloud Actions for all clouds in data
     *
     */
    fun cloudMovement() {
        // Map to consider the max Tiles a merged Cloud can move in this tick
        val tilesMovedForCloud: MutableMap<Cloud, Int> = data.getClouds().associateWith { 0 }.toMutableMap()

        val cloudsExistingBeforeMovement = data.getClouds().toList()
        val cloudsMerged: MutableList<Cloud> = mutableListOf()

        // Movement
        for (cloud in cloudsExistingBeforeMovement) {
            // Check if cloud was removed during Movement
            if (!data.getClouds().contains(cloud)) continue
            oneCloud(cloud, tilesMovedForCloud, cloudsMerged)
        }

        // Movement for MergedClouds
        // var max: Int = data.getClouds().size + cloudsMerged.size // just to make sure that no infinite loops are pos
        while (!cloudsMerged.isEmpty()) {
            val currCloud: Cloud = cloudsMerged.first()
            // Remove from merged Clouds as cloud action for this will now be performed
            cloudsMerged.remove(currCloud)
            if (!data.getClouds().contains(currCloud)) continue
            // Perform Movement for this cloud
            oneCloud(currCloud, tilesMovedForCloud, cloudsMerged)
        }

        // For Logging sunlight by ascending Tile ID
        val tilesWithCloud: MutableMap<Tile, Cloud> = mutableMapOf()

        // Reduce Sunlight -50
        for (cloud in data.getClouds()) {
            val tile: Tile? = data.getMap().getTileByCoordinate(cloud.getLocation())
            if (tile?.getGrowable() != null) {
                tile.getGrowable()?.setSunlightExposure(
                    kotlin.math.max(
                        0,
                        tile.getGrowable()?.getSunlightExposureCurrentTick()
                            ?.minus(GeneralConstants.MAX_SUN_REDUCTION) ?: error("This should never happen Tim 1")
                    )
                )
                tilesWithCloud[tile] = cloud
            }
        }

        // Log Sunlight by ascending Tile ID
        for ((tile, cloud) in tilesWithCloud.toSortedMap(compareBy { it.getID() })) {
            Logger.logCloudPosition(
                cloud.getID(),
                tile.getID(),
                tile.getGrowable()?.getSunlightExposureCurrentTick() ?: 0
            )
        }
    }

    /** performs the cloud Actions for one Cloud */
    private fun oneCloud(cloud: Cloud, tilesMovedForCloud: MutableMap<Cloud, Int>, merged: MutableList<Cloud>) {
        val map = data.getMap()

        // Get Moved tiles this tick, important for merging
        var tilesMoved: Int = tilesMovedForCloud[cloud] ?: error("This should never happen Tim 2")
        var c = 0

        while (c <= GeneralConstants.MAX_MOVES_PER_TICK) {
            c++
            // Try to rain
            rainIfPossible(cloud)

            // Check amount
            if (cloud.getAmount() == 0) {
                val location = map.getTileByCoordinate(cloud.getLocation()) ?: error("This should never happen Tim 3")
                dissipate(cloud, false, location)
                return
            }

            // Check Move
            if (!checkMoveAndDoIfPossible(cloud, tilesMoved)) break

            // Increase Moved Tiles this tick
            tilesMoved++
            tilesMovedForCloud[cloud] = tilesMoved

            val location = map.getTileByCoordinate(cloud.getLocation()) ?: error("This should never happen Tim 4")
            // Check Location (dissipate if Village)
            if (location.getTileType() == TileType.VILLAGE) {
                dissipate(cloud, true, location)
                return
            }

            // Check if merged happened, if yes: return as curr Cloud dissipated
            if (checkMergeAndDoIfPossible(cloud, tilesMovedForCloud, merged)) return
        }
        // Try to rain again
        rainIfPossible(cloud)
        // Check amount
        if (cloud.getAmount() == 0) {
            val location = map.getTileByCoordinate(cloud.getLocation()) ?: error("This should never happen Tim 3")
            dissipate(cloud, false, location)
            return
        }
        // Check duration after cloud Movement of curr Cloud
        val currDuration = cloud.getDuration()
        val newDuration = if (currDuration == -1) -1 else currDuration - 1
        if (newDuration == 0) {
            val location = map.getTileByCoordinate(cloud.getLocation()) ?: error("This should never happen Tim 5")
            dissipate(cloud, false, location)
        } else {
            cloud.setDuration(newDuration)
        }
    }

    /** checks if rain if possible, if yes it performs raining */
    private fun rainIfPossible(cloud: Cloud) {
        // Check if amount is enough to Rain
        if (cloud.getAmount() < GeneralConstants.MIN_RAIN_AMOUNT) {
            return
        }

        val tile: Tile = data.getMap().getTileByCoordinate(cloud.getLocation())
            ?: error("This should never happen Tim 6")

        if (tile.getTileType() != TileType.PLANTATION && tile.getTileType() != TileType.FIELD) {
            // rain all if on non-growable Tile
            Logger.logCloudRain(cloud.getID(), cloud.getAmount(), tile.getID())
            cloud.setAmount(0)
            return
        }

        // Else: Rain amount depends on capacity of Growable
        val currMoisture = tile.getGrowable()?.getMoistureExposure() ?: error("This should never happen Tim 6")
        val maxMoisture = tile.getGrowable()?.getMaxMoisture() ?: error("This should never happen Tim 7")

        if (currMoisture < maxMoisture) {
            val toRain = kotlin.math.min(cloud.getAmount(), maxMoisture - currMoisture)
            // Subtract in Cloud
            cloud.setAmount(cloud.getAmount() - toRain)
            // Add in Tile
            val moistureCurr: Int = tile.getGrowable()?.getMoistureExposure() ?: error("This should never happen Tim 8")
            tile.getGrowable()?.setMoistureExposure(moistureCurr + toRain)

            // Log
            Logger.logCloudRain(cloud.getID(), toRain, tile.getID())
        }
        // Else: Cannot rain as max Capacity => do not rain
    }

    /**
     * Check if Movement is possible, and if yes, it performs the move
     *
     * @param cloud Current Cloud
     * @param tilesMovedThisTick indicates how many tiles the cloud can still move
     * @return true if the cloud moved, false otherwise
     */
    private fun checkMoveAndDoIfPossible(cloud: Cloud, tilesMovedThisTick: Int): Boolean {
        val map = data.getMap()
        val locationTile: Tile = map.getTileByCoordinate(cloud.getLocation()) ?: error("This should never happen Tim 9")

        if (!locationTile.getAirflow() || tilesMovedThisTick >= GeneralConstants.MAX_MOVES_PER_TICK) {
            return false
        }

        // Get loc neighbour where to move
        val coordinateToMove =
            locationTile.getCoordinate().getNeighborByDirection(locationTile.getDirection())
                ?: error("This should never happen Tim 10")

        // Check if target is unspecified tile
        map.getTileByCoordinate(coordinateToMove) ?: return false

        // Reduce Sunlight after officially traversed the old tile
        locationTile.getGrowable()?.setSunlightExposure(
            kotlin.math.max(
                0,
                locationTile.getGrowable()?.getSunlightExposureCurrentTick()?.minus(3)
                    ?: error("This should never happen Tim 11")
            )
        )

        // Now Move
        cloud.setLocation(coordinateToMove)

        // Log Movement
        Logger.logCloudMovement(
            cloud.getID(),
            cloud.getAmount(),
            locationTile.getID(),
            map.getTileByCoordinate(coordinateToMove)?.getID() ?: error("This should never happen Tim 12")
        )

        // Log sun on start Tile if start Tile is Growable
        if (locationTile.getGrowable() != null) {
            Logger.logCloudSunOnTile(
                locationTile.getGrowable()?.getSunlightExposureCurrentTick()
                    ?: error("This should never happen Tim 13"),
                locationTile.getID()
            )
        }

        return true
    }

    /**
     * Check if merge is possible and if yes it performs it
     *
     * @param cloud current Cloud
     * @param tilesMovedForCloud how much every cloud moved, important to calculate moves num of newCloud in curr Tick
     * @param merged collects all merged clouds in this tick
     * @return true only if merge was performed this tick
     */
    private fun checkMergeAndDoIfPossible(
        cloud: Cloud,
        tilesMovedForCloud: MutableMap<Cloud, Int>,
        merged: MutableList<Cloud>
    ): Boolean {
        val otherCloudOnSameTile: Cloud = data.getOtherCloudOnSameCoordinate(cloud) ?: return false

        // Merge Clouds Now
        // Calculate new Duration
        val dur1 = cloud.getDuration()
        val dur2 = otherCloudOnSameTile.getDuration()
        var dur = 0
        if (dur1 == -1) {
            dur = dur2
        } else if (dur2 == -1) {
            dur = dur1
        } else {
            dur = kotlin.math.min(dur1, dur2)
        }
        // Create cloud with am = am1 + am2, duration = min(dur1,dur2), id = maxID +1
        val newCloud = Cloud(
            data.getMaxCloudID() + 1,
            cloud.getAmount() + otherCloudOnSameTile.getAmount(),
            dur,
            cloud.getLocation()
        )
        // Remove other clouds:
        data.deleteCloud(cloud)
        data.deleteCloud(otherCloudOnSameTile)

        // Calculate moves this newCloud can make this tick, gets to min steps of the merged to be able to move max
        // times of both
        tilesMovedForCloud[newCloud] =
            kotlin.math.min(
                tilesMovedForCloud[cloud]
                    ?: error("This should never happen Tim 14"),
                tilesMovedForCloud[otherCloudOnSameTile]
                    ?: error("This should never happen Tim 15")
            )

        // Add new Cloud to data
        data.addCloud(newCloud)
        // Add to merged Clouds
        merged.add(newCloud)

        // Log Merging
        Logger.logCloudMerge(
            otherCloudOnSameTile.getID(),
            cloud.getID(),
            newCloud.getID(),
            newCloud.getAmount(),
            newCloud.getDuration(),
            data.getMap().getTileByCoordinate(newCloud.getLocation())?.getID()
                ?: error("This should never happen Tim 16")
        )

        return true
    }

    /**
     * performs dissipation of a cloud and prints the correct Logger message, depending on reason of Dissipation
     *
     * @param cloud: current Cloud
     * @param stuck: if cloud got stuck on village or not
     * @param location: current LocationTile the cloud is located on
     */

    private fun dissipate(cloud: Cloud, stuck: Boolean, location: Tile) {
        // Remove cloud
        data.deleteCloud(cloud)

        // Log Dissipate
        if (stuck) {
            Logger.logCloudStuck(cloud.getID(), location.getID())
        } else {
            Logger.logCloudDissipate(cloud.getID(), location.getID())
        }
    }
}
