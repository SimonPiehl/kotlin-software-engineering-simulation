package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BeeHappy
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Incident
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.view.Logger
import kotlin.collections.mutableListOf

/**
 * Incident controller
 *
 * @property data
 * @constructor Create Incident controller
 */
class IncidentController(private val data: SimulationData) {
    private val activeIncidents = mutableMapOf<Int, MutableList<Incident>>()

    /**
     * Check for active incidents if they end this tick
     */
    fun checkActiveIncidents() {
        val currentTick = data.getCurrentTick()

        // Search for active incidents Ending this Tick
        for (incident: Incident in activeIncidents[currentTick] ?: mutableListOf()) {
            if (incident is BrokenMachine) {
                incident.getMachine().setIsBroken(false)
            }
        }
    }

    /**
     * Check incidents occurring this tick and execute them
     */
    fun checkIncidents() {
        // Clear helper List
        data.clearTilesHarvestChanged()
        data.clearTilesWhereDrought()
        // execute all incidents that start in current Tick
        for (incident: Incident in data.getIncidents()[data.getCurrentTick()] ?: mutableListOf()) {
            when (incident) {
                is CloudCreation -> this.cloudCreation(incident)
                is AnimalAttack -> this.animalAttack(incident)
                is CityExpansion -> this.cityExpansion(incident)
                is Drought -> this.drought(incident)
                is BeeHappy -> this.beeHappy(incident)
                is BrokenMachine -> this.brokenMachine(incident)
            }
        }
    }

    /** performs a cloudCreation incident*/
    private fun cloudCreation(c: CloudCreation) {
        val map = data.getMap()
        val locationTile: Tile = map.getTileByID(c.getLocation()) ?: return
        val location: Coordinate = locationTile.getCoordinate()

        // Get all Tiles in radius, sorted by ascending tileID to add them in this order
        val allTiles = (
            map.getTilesByCoordinates(location.getNeighbours(c.getRadius())) + locationTile
            ).sortedBy { it.getID() }

        // Filter out Village Tiles
        val affectedTiles = allTiles.filter { it.getTileType() != TileType.VILLAGE }

        // Log Message for affected Tiles
        Logger.logIncident(c.getID(), c.toString(), affectedTiles.map { t -> t.getID() }.toMutableList())

        // Spawn Clouds with tileCoordinate for each affected tile
        for (tile: Tile in affectedTiles) {
            val newCloud = Cloud(
                maxOf(data.getMaxCloudID() + 1, 0),
                c.getAmount(),
                c.getDuration(),
                tile.getCoordinate()
            )

            // Test if merge needed
            val cloudOnSameSpot: Cloud? = data.getOtherCloudOnSameCoordinate(newCloud)
            if (cloudOnSameSpot != null) {
                // Merge and then add new Cloud with new ID+1
                // Calculate new Duration
                val dur1 = newCloud.getDuration()
                val dur2 = cloudOnSameSpot.getDuration()
                var dur = 0
                if (dur1 == -1) {
                    dur = dur2
                } else if (dur2 == -1) {
                    dur = dur1
                } else {
                    dur = kotlin.math.min(dur1, dur2)
                }
                // Create merged cLoud, use cloudCreation Cloud for that
                newCloud.setAmount(cloudOnSameSpot.getAmount() + newCloud.getAmount())
                newCloud.setID(newCloud.getID() + 1)
                newCloud.setDuration(dur)

                // Remove old Cloud from data
                data.deleteCloud(cloudOnSameSpot)

                // Log Merge
                Logger.logCloudMerge(
                    cloudOnSameSpot.getID(),
                    newCloud.getID() - 1,
                    newCloud.getID(),
                    newCloud.getAmount(),
                    newCloud.getDuration(),
                    tile.getID()
                )
            }

            // Add created cloud to map
            data.addCloud(newCloud)
        }
    }

    /** performs a AnimalAttack incident*/
    private fun animalAttack(a: AnimalAttack) {
        // Getting all the FOREST Tiles in the radius
        val map = data.getMap()
        val locationTile: Tile = map.getTileByID(a.getLocation()) ?: error("This should never happen 3")
        val tilesInRadius: MutableList<Tile> = mutableListOf(locationTile)
        tilesInRadius.addAll(map.getTilesByCoordinates(locationTile.getCoordinate().getNeighbours(a.getRadius())))
        val allForestTiles = tilesInRadius.filter { it.getTileType() == TileType.FOREST }

        // Getting all the affected Tiles adjoining to the FOREST Tiles
        val adjoiningCoordinates = allForestTiles.map { it.getCoordinate().getNeighbours(1) }.flatten().distinct()
        val adjoiningTiles = map.getTilesByCoordinates(adjoiningCoordinates)
        val affectedTiles = adjoiningTiles
            .filter { it.getTileType() == TileType.FIELD || it.getTileType() == TileType.PLANTATION }
            .sortedBy { it.getID() }
        val tileIdsAffected = affectedTiles.map { it.getID() }.toMutableList()

        for (tile in affectedTiles) {
            animalAttackOnTile(tile)
        }

        // Logger message
        Logger.logIncident(a.getID(), a.toString(), tileIdsAffected)
    }

    /** performs a AnimalAttack effects for one tile in radius*/
    private fun animalAttackOnTile(tile: Tile) {
        if (tile.getTileType() == TileType.FIELD) {
            val growable = tile.getGrowable() ?: return
            if (growable.getCropsExpected() != 0) {
                data
                    .addTilesHarvestChanged(tile, GeneralConstants.ANIMAL_ATTACK_MUL_50)
            }
        }
        if (tile.getTileType() == TileType.PLANTATION) {
            val growable = tile.getGrowable() ?: return
            if (growable.getCropsExpected() == 0) return

            // If Plant Type is Grape
            if (growable.getCurrentPlant() == PlantType.GRAPE) {
                data.addTilesHarvestChanged(tile, GeneralConstants.ANIMAL_ATTACK_MUL_50)
            } else {
                data.addTilesHarvestChanged(tile, GeneralConstants.ANIMAL_ATTACK_MUL_90)
                tryMowing(tile)
            }
        }
    }

    /*
    private fun performAnimalAttackOnAdjoining(affectedTiles: List<Tile>, a: AnimalAttack) {
        val actuallyAffectedTilesID: MutableList<Int> = mutableListOf()

        for (tile: Tile in affectedTiles) {
            // Check if Tile can still be influenced by AnimalAttack Or Harvest already 0
            if (tile.getGrowable()?.getIncidentsThisTick()?.contains(IncidentType.ANIMAL_ATTACK) ?: true ||
                tile.getGrowable()?.getCropsExpected() == 0
            ) {
                continue
            }

            // Reduce Harvest
            if (tile.getTileType() == TileType.FIELD) {
                val currExpected = tile.getGrowable()?.getCropsExpected() ?: 0
                tile.getGrowable()?.setCropsExpected(
                    currExpected.times(GeneralConstants.ANIMAL_ATTACK_MUL_50).toInt()
                )
            } else {
                // Reduce Harvest
                val currExpected = tile.getGrowable()?.getCropsExpected() ?: 0
                if (tile.getGrowable()?.getCurrentPlant() == PlantType.GRAPE) {
                    tile.getGrowable()?.setCropsExpected(
                        currExpected.times(GeneralConstants.ANIMAL_ATTACK_MUL_50).toInt()
                    )
                } else {
                    tile.getGrowable()?.setCropsExpected(
                        currExpected.times(GeneralConstants.ANIMAL_ATTACK_MUL_90).toInt()
                    )
                }

                // Update Mowing (reset need for Mowing for this + next Tick)
                tryMowing(tile)
            }

            // Add to affected Tiles (not if harvest was 0g before Incident)
            actuallyAffectedTilesID.add(tile.getID())
            data.addTilesHarvestChanged(tile)
            tile.getGrowable()?.addIncidentThisTick(IncidentType.ANIMAL_ATTACK)
        }

        // Log Message
        Logger.logIncident(a.getID(), a.toString(), actuallyAffectedTilesID)
    }
    */

    /** performs a mowing part of an AnimalAttack*/
    private fun tryMowing(tile: Tile) {
        if (tile.canIBeMowedByAnimals(data.getYearTick())) {
            tile.getGrowable()?.setWasMowedAtTick(data.getCurrentTick())
        }
        if (tile.canIBeMowedByAnimals(data.getYearTick() % GeneralConstants.AMOUNT_24 + 1)) {
            tile.getGrowable()?.setWasMowedAtTick(data.getCurrentTick() + 1)
        }
    }

    /** performs a CityExpansion incident*/
    private fun cityExpansion(c: CityExpansion) {
        val map = data.getMap()
        val affectedTile: Tile = map.getTileByID(c.getLocation()) ?: return

        // If FIELD: update in map -> remove from Growable Lists + remove Growable instance in Tile Object
        if (affectedTile.getTileType() == TileType.FIELD) {
            map.removeTile(affectedTile, map.getFarmIDbyField(affectedTile))
            affectedTile.changeToVillage()
            map.addTile(affectedTile, null)
        } else if (affectedTile.getTileType() == TileType.ROAD) {
            affectedTile.changeToVillage()
        }

        // Log Message
        Logger.logIncident(c.getID(), c.toString(), mutableListOf(affectedTile.getID()))

        // Check if Cloud Located on tile
        val cloudsHere: List<Cloud> = data.getClouds().filter { it.getLocation() == affectedTile.getCoordinate() }
        if (!cloudsHere.isEmpty()) {
            // Cloud Dissipates immediately
            data.deleteCloud(cloudsHere[0])
            // Log Stuck + Dissipate
            Logger.logCloudStuck(cloudsHere[0].getID(), affectedTile.getID())
        }
    }

    /** performs a BeeHappy incident*/
    private fun beeHappy(beeHappy: BeeHappy) {
        // Calculating the Radius Tiles
        val map = data.getMap()
        val locationTile: Tile = map.getTileByID(beeHappy.getLocation()) ?: error("This should never happen 4")
        val coordinatesInRadius = locationTile.getCoordinate().getNeighbours(beeHappy.getRadius())
        val tilesInRadius = map.getTilesByCoordinates(coordinatesInRadius)
        tilesInRadius.add(locationTile)

        // Calculating the affected Tiles
        val meadowTiles = tilesInRadius.filter { it.getTileType() == TileType.MEADOW }
        val coordinatesAroundMeadows = meadowTiles.map { it.getCoordinate().getNeighbours(2) }.flatten().distinct()
        val tilesAroundMeadows = map.getTilesByCoordinates(coordinatesAroundMeadows)

        val possiblyAffectedTiles = tilesAroundMeadows
            .filter { it.getTileType() == TileType.FIELD || it.getTileType() == TileType.PLANTATION }

        val affectedTiles = possiblyAffectedTiles
            .filter { it.canIBePollinated(data.getYearTick(), data.getCurrentTick()) }
            .sortedBy { it.getID() }

        // Doing Bee Happy for the affected Tiles
        for (tile in affectedTiles) {
            beeHappyPerTile(beeHappy, tile)
        }

        // Logger
        val affectedTilesIds = affectedTiles.map { it.getID() }.toMutableList()
        Logger.logIncident(
            incidentID = beeHappy.getID(),
            type = beeHappy.toString(),
            affectedTiles = affectedTilesIds
        )
    }

    /** performs the effects of BeeHappy for one Tile*/
    private fun beeHappyPerTile(beeHappy: BeeHappy, tile: Tile) {
        val growable = tile.getGrowable() ?: return
        val plant = growable.getCurrentPlant()

        if (plant == PlantType.POTATO) return

        // Calculate new crops expected
        val effect = beeHappy.getEffect()
        val effectPercentUnderOne = (effect / GeneralConstants.PERCENT_TO_DOUBLE) + 1
        if (growable.getCropsExpected() > 0) data.addTilesHarvestChanged(tile, effectPercentUnderOne)
    }

    /** performs a Drought incident*/
    private fun drought(d: Drought) {
        val map = data.getMap()
        val locationTile: Tile = map.getTileByID(d.getLocation()) ?: return
        val radius: Int = d.getRadius()

        // Get all tiles in radius
        val neighbours = locationTile.getCoordinate().getNeighbours(radius)
        val tilesInRadius = map.getTilesByCoordinates(neighbours) + locationTile

        val affectedTiles = tilesInRadius
            .filter { it.getTileType() == TileType.FIELD || it.getTileType() == TileType.PLANTATION }
            .sortedBy { it.getID() }

        // Search for Growable Tiles in Range
        for (tile: Tile in affectedTiles) {
            data.addTilesWhereDrought(tile)
            val growable = tile.getGrowable() ?: continue
            growable.setMoistureExposure(0)

            if (growable.getCropsExpected() == 0) continue

            data.addTilesHarvestChanged(tile, GeneralConstants.DROUGHT_EFFECT_ZERO)
        }

        val affectedTilesIds = affectedTiles.map { it.getID() }.toMutableList()
        // Log Message
        Logger.logIncident(d.getID(), d.toString(), affectedTilesIds)
    }

    /** performs a BrokenMachine incident*/
    private fun brokenMachine(brokenMachine: BrokenMachine) {
        val affectedMachine = brokenMachine.getMachine()
        if (affectedMachine.getIsBroken()) {
            // Machine is already broken
            overrideBrokenMachine(brokenMachine, affectedMachine)
        } else {
            // Machine ISN'T already broken
            affectedMachine.setIsBroken(true)
            val activeIncidentsAtEndTick =
                activeIncidents[brokenMachine.getDuration().getEndTick()] ?: mutableListOf()
            activeIncidentsAtEndTick.add(brokenMachine)
            activeIncidents[brokenMachine.getDuration().getEndTick()] = activeIncidentsAtEndTick
        }

        // Logger output
        // Log Message
        val tile = data.getMap().getTileByCoordinate(affectedMachine.getCurrentLocation())
            ?: error("This should never happen 5")
        val id: Int = tile.getID()
        Logger.logIncident(brokenMachine.getID(), brokenMachine.toString(), mutableListOf(id))
    }

    /**
     * if there is an active BrokenMachineIncident on same Machine, this function checks which Incident last longer and
     * thus should be applied while the other incident should not be in the active Incidents
     */
    private fun overrideBrokenMachine(brokenMachine: BrokenMachine, affectedMachine: Machine) {
        val allActiveBrokenMachines = activeIncidents.values.flatten().filterIsInstance<BrokenMachine>()
        val activeBrokenMachine = allActiveBrokenMachines
            .find { it.getMachine().getID() == affectedMachine.getID() } ?: error("This should never happen 6")
        if (brokenMachine.getDuration().getEndTick() > activeBrokenMachine.getDuration().getEndTick()) {
            // New Broken Machine is the longer one
            // Removing old Broken Machine
            val activeIncidentsAtOldBrokenMachineTick =
                activeIncidents[activeBrokenMachine.getDuration().getEndTick()] ?: error("This should never happen 7")
            activeIncidentsAtOldBrokenMachineTick.remove(activeBrokenMachine)
            // activeIncidentsAtOldBrokenMachineTick.add(brokenMachine)
            // activeIncidents[activeBrokenMachine.getDuration().getEndTick()] = activeIncidentsAtOldBrokenMachineTick
            activeIncidents.getOrPut(brokenMachine.getDuration().getEndTick()) { mutableListOf() }.add(brokenMachine)
        }
    }
}
