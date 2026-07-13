package de.unisaarland.cs.se.selab.controller

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.SowingPlan
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.view.Logger

/**
 * The Controller that does the fourth step of the Simulation/Farm
 */
class FarmController(
    private val data: SimulationData,
    private val pathFinder: PathFinderController
) {

    /**
     * Simulates the FarmsAct step of the Simulation
     */
    fun farmsAction() {
        for (farm in data.getFarms()) {
            farmActs(farm)
        }
    }

    /**
     * Do the actions that are done for every farm
     *
     */
    private fun farmActs(farm: Farm) {
        Logger.logFarmStart(farm.getID())
        Logger.logAllSowingPlansThisTick(
            farm.getID(),
            farm.getPlans().filter {
                it.getStartTick() <= data.getCurrentTick()
            }.map { it.getID() }
        )
        // Do all the farm based actions according to continuation
        checkAndIfPossibleSOWINGFields(farm)
        checkAndIfPossibleHARVESTINGPlantation(farm)
        checkAndIfPossibleHARVESTINGFields(farm)
        checkAndIfPossibleCUTTINGPlantation(farm)

        performOnLeftOverMachinesById(farm)

        for (m in farm.getMachines()) {
            m.reset()
        }
        Logger.logFarmFinished(farm.getID())
    }

    /**
     * Does the sowing step per farm over all possible fields
     *
     */
    private fun checkAndIfPossibleSOWINGFields(farm: Farm) {
        // gets all plans that could be worked on this tick
        val possiblePlans = farm.getPlans().filter { it.getStartTick() <= data.getCurrentTick() }

        // iterates over all those plans
        for (plan in possiblePlans) {
            // select only the fields that could actually receive those sowings
            val sowableFields = data.getMap().getFarmFieldsByID(farm.getID()).filter {
                it.canIBeSowed(
                    data.getCurrentTick(), data.getYearTick(), plan.getPlantToSow()
                ) &&
                    it.getGrowable()?.getPossiblePlants()?.contains(
                        plan.getPlantToSow()
                    ) ?: false &&
                    plan.getLocations().contains(it.getID())
            }

            // iterate over those fields to check if a machine can do the job
            for (tile in sowableFields) {
                // get all the machines that could do the sowing
                if (tile.getGrowable()?.getWasSowedAtTick() == data.getCurrentTick()) continue
                val m = farm.getMachinesByPlantAndAction(plan.getPlantToSow(), Action.SOWING).filter {
                    it.canIWork() &&
                        pathFinder.isTileReachable(farm, it, tile) &&
                        tile.getGrowable()?.getWasSowedAtTick() != data.getCurrentTick()
                }.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() })
                // if now machine can do the job we don't remove the plan yet and don't set any fields but continue
                if (m.isEmpty()) continue
                // set necessary fields
                tile.getGrowable()?.setCurrentPlant(plan.getPlantToSow())
                tile.getGrowable()?.setWasSowedAtTick(data.getCurrentTick())
                Logger.logFarmAction(m[0].getID(), Action.SOWING, tile.getID(), m[0].getDuration())
                Logger.logPerformedPlan(m[0].getID(), plan.getPlantToSow(), plan.getID())
                m[0].setCurrentLocation(tile.getCoordinate())
                m[0].worked()
                plan.setWasUsedInTick(data.getCurrentTick())
                // continue action for as long as the machine that started working can still continue that plan
                while (m[0].canIStillWork()) {
                    continueSow(farm, m[0], plan)
                }
                // remove the plan from the farm if it was worked on
                farm.removePlan(plan)
                pathFinder.moveToShedUnloadedMachine(m[0])
            }
        }
    }

    /**
     * Does the harvesting plantation step per given farm
     *
     */
    private fun checkAndIfPossibleHARVESTINGPlantation(farm: Farm) {
        // List of all plantation that can be harvested
        val harvestablePlantations = data.getMap().getFarmPlantationsByID(farm.getID()).filter {
            it.canIBeHarvested(data.getCurrentTick(), data.getYearTick())
        }

        // iterate over all those iterable plantations
        for (plantation in harvestablePlantations) {
            // get all machines that could do the job
            for (m in farm.getMachinesByPlantAndAction(
                plantation.getGrowable()?.getCurrentPlant() ?: PlantType.OAT,
                Action.HARVESTING
            ).filter {
                it.canIWork() && pathFinder.isTileReachable(farm, it, plantation)
            }.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() })) {
                if (plantation.checkWorkedOnThisTick(data.getCurrentTick())) {
                    continue
                }
                // call the harvest function that is defined below
                harvest(m, plantation)

                // continue this action until the machine can't work no more
                while (m.canIStillWork()) {
                    continueAction(farm, m)
                }
                unloadMachine(farm, m)
            }
        }
    }

    /**
     * Does the harvesting of fields step per farm
     *
     */
    private fun checkAndIfPossibleHARVESTINGFields(farm: Farm) {
        // creates list of all fields that could be harvested this tick
        val harvestableFields = data.getMap().getFarmFieldsByID(farm.getID()).filter {
            it.canIBeHarvested(data.getCurrentTick(), data.getYearTick())
        }

        // iterates over all those fields
        for (field in harvestableFields) {
            // iterates over all machines that could work on this field
            if (field.checkWorkedOnThisTick(data.getCurrentTick())) {
                continue
            }
            for (
            machine in farm.getMachinesByPlantAndAction(
                field.getGrowable()?.getCurrentPlant() ?: PlantType.OAT,
                Action.HARVESTING
            ).filter {
                it.canIWork() && pathFinder.isTileReachable(farm, it, field)
            }.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() })
            ) {
                if (field.checkWorkedOnThisTick(data.getCurrentTick())) {
                    break
                }
                // harvest this field with the machine
                harvest(machine, field)

                // continue this step until the machine can not work anymore
                while (machine.canIStillWork()) {
                    continueAction(farm, machine)
                }
                unloadMachine(farm, machine)
            }
        }
    }

    /**
     * harvest the tile that is given with the given machine and prints according log messages
     */
    private fun harvest(m: Machine, tile: Tile) {
        // set the fields for machine
        m.setAmountLoadedThisTick(tile.getGrowable()?.getCropsExpected() ?: 0)
        m.setPerformedAction(Action.HARVESTING)
        m.setWorkedOnPlant(tile.getGrowable()?.getCurrentPlant() ?: PlantType.OAT)
        m.setCurrentLocation(tile.getCoordinate())
        m.worked()

        // Log all the needed output
        Logger.logFarmAction(m.getID(), Action.HARVESTING, tile.getID(), m.getDuration())
        Logger.logCollectedHarvest(
            m.getID(),
            tile.getGrowable()?.getCropsExpected() ?: 0,
            m.getWorkedOnPlant() ?: PlantType.OAT
        )
        // Reset the tiles back to their default
        if (tile.getTileType() == TileType.FIELD) tile.getGrowable()?.setCurrentPlant(null)
        tile.getGrowable()?.setCropsExpected(0)
        tile.getGrowable()?.setLiesFallowSinceTick(data.getCurrentTick())
        tile.getGrowable()?.setWasSowedAtTick(-1)
        tile.getGrowable()?.setWasWeededAtTick(mutableListOf())
        tile.getGrowable()?.setWasCutAtTick(-1)
        tile.getGrowable()?.setWasMowedAtTick(-1)
        tile.getGrowable()?.setLastTickWorkedOn(data.getCurrentTick())
        tile.getGrowable()?.setWasHarvestedAtTick(data.getCurrentTick())
    }

    /**
     * add the loaded amount to the statistics and return back home
     *
     */
    private fun unloadMachine(farm: Farm, m: Machine) {
        // Adds the amound that the machine loaded to the statistic and moves back to the shed
        val type = m.getWorkedOnPlant() ?: return
        farm.addToStatistic(type, m.getAmountLoadedThisTick())
        pathFinder.moveToShedLoadedMachine(m, farm)
    }

    /**
     *
     * Does the cutting plantation step of the farm act on all possible plantations
     */
    private fun checkAndIfPossibleCUTTINGPlantation(farm: Farm) {
        // creates a list with all the plantations that could be cut
        val cuttablePlantations = data.getMap().getFarmPlantationsByID(farm.getID()).filter {
            it.canIBeCut(data.getCurrentTick(), data.getYearTick())
        }
        // iterates over those plantations
        for (plantation in cuttablePlantations) {
            val type = plantation.getGrowable()?.getCurrentPlant() ?: continue
            // iterates over all machines that could do the job
            for (
            m in farm.getMachinesByPlantAndAction(
                type,
                Action.CUTTING
            ).filter {
                it.canIWork() && pathFinder.isTileReachable(farm, it, plantation)
            }.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() })
            ) {
                if (plantation.checkWorkedOnThisTick(data.getCurrentTick())) {
                    continue
                }
                // Set all the needed fields
                plantation.getGrowable()?.setWasCutAtTick(data.getCurrentTick())
                m.setPerformedAction(Action.CUTTING)
                m.setWorkedOnPlant(plantation.getGrowable()?.getCurrentPlant() ?: PlantType.OAT)
                m.setCurrentLocation(plantation.getCoordinate())
                m.worked()
                Logger.logFarmAction(m.getID(), Action.CUTTING, plantation.getID(), m.getDuration())

                // continue the action until the machine can't work no more
                while (m.canIStillWork()) {
                    continueAction(farm, m)
                }
                // move back home
                pathFinder.moveToShedUnloadedMachine(m)
            }
        }
    }

    /**
     * iterates over all machines and lets the ones that haven't worked yet do the lower steps
     *
     */
    private fun performOnLeftOverMachinesById(farm: Farm) {
        // iterate over all machines that haven't worked yet
        for (machine in farm.getMachines().filter {
            it.canIWork()
        }.sortedWith(compareBy<Machine> { it.getDuration() }.thenBy { it.getID() })) {
            // Do the machine steps in the given order
            checkAndIfPossibleIRRIGATINGFields(farm, machine)
            checkAndIfPossibleWEEDINGFields(farm, machine)
            checkAndIfPossibleIRRIGATINGPlantations(farm, machine)
            checkAndIfPossibleMOWINGPlantations(farm, machine)
        }
    }

    /**
     * Do the irrigation step with a given farm and machine
     *
     */
    private fun checkAndIfPossibleIRRIGATINGFields(farm: Farm, m: Machine) {
        // exit if the machine already worked or can't irrigate
        if (!m.canIWork() || m.getPerformedAction() != null || !m.getPossibleActions().contains(Action.IRRIGATING)) {
            return
        }
        // get List of all irrigatable fields for a farm
        val irrigatableFields = data.getMap().getFarmFieldsByID(farm.getID()).filter {
            it.canIBeIrrigated(data.getCurrentTick()) &&
                pathFinder.isTileReachable(farm, m, it) &&
                m.getPossiblePlants().contains(it.getGrowable()?.getCurrentPlant())
        }
        // iterate over all those fields
        if (irrigatableFields.isEmpty()) return
        for (field in irrigatableFields) {
            if (field.checkWorkedOnThisTick(data.getCurrentTick())) {
                continue
            }
            // set the needed fields for irrigation
            field.getGrowable()?.setMoistureExposure(field.getGrowable()?.getMaxMoisture() ?: 1)
            field.getGrowable()?.setWasIrrigatedAtTick(data.getCurrentTick())
            m.setWorkedOnPlant(field.getGrowable()?.getCurrentPlant() ?: PlantType.OAT)
            m.setPerformedAction(Action.IRRIGATING)
            m.setCurrentLocation(field.getCoordinate())

            // Output the needed logger message
            Logger.logFarmAction(m.getID(), Action.IRRIGATING, field.getID(), m.getDuration())
            m.worked()

            // continue this action until the machine can't work no more
            while (m.canIStillWork()) {
                continueAction(farm, m)
            }
            // machine go back home
            pathFinder.moveToShedUnloadedMachine(m)
            return
        }
    }

    /**
     * Do the weeding step per machine and farm
     *
     */
    private fun checkAndIfPossibleWEEDINGFields(farm: Farm, m: Machine) {
        // exit if the machine has worked before
        if (!m.canIWork() || m.getPerformedAction() != null || !m.getPossibleActions().contains(Action.WEEDING)) return
        // get list of a weedable fields for a farm that are reachable by a machine
        val weedableFields = data.getMap().getFarmFieldsByID(farm.getID()).filter {
            it.canIBeWeeded(data.getCurrentTick()) &&
                pathFinder.isTileReachable(farm, m, it) &&
                m.getPossiblePlants().contains(it.getGrowable()?.getCurrentPlant())
        }
        // iterate over those fields
        for (field in weedableFields) {
            // continue if the field was already worked on
            if (field.checkWorkedOnThisTick(data.getCurrentTick())) {
                continue
            }
            // set the needed fields
            field.getGrowable()?.addWasWeededAtTick(data.getCurrentTick())
            field.getGrowable()?.setLastTickWorkedOn(data.getCurrentTick())
            val type = field.getGrowable()?.getCurrentPlant() ?: break
            m.setWorkedOnPlant(type)
            m.setPerformedAction(Action.WEEDING)
            m.setCurrentLocation(field.getCoordinate())
            m.worked()
            // output the logger message
            Logger.logFarmAction(m.getID(), Action.WEEDING, field.getID(), m.getDuration())
            // continue action until the machine can't work no more
            while (m.canIStillWork()) {
                continueAction(farm, m)
            }
            // go back home
            pathFinder.moveToShedUnloadedMachine(m)
            return
        }
    }

    /**
     * Do the irrigation plantation step per farm and machine
     */
    private fun checkAndIfPossibleIRRIGATINGPlantations(farm: Farm, m: Machine) {
        // exit if the machine already worked
        if (!m.canIWork() || m.getPerformedAction() != null || !m.getPossibleActions().contains(Action.IRRIGATING)) {
            return
        }
        // gets a list of all irrigateableplantations
        val irrigatablePlantations = data.getMap().getFarmPlantationsByID(farm.getID()).filter {
            it.canIBeIrrigated(data.getCurrentTick()) &&
                pathFinder.isTileReachable(farm, m, it) &&
                m.getPossiblePlants().contains(it.getGrowable()?.getCurrentPlant())
        }
        // iterate over this list
        for (plantation in irrigatablePlantations) {
            // continue if already worked on through continuation
            if (plantation.checkWorkedOnThisTick(data.getCurrentTick())) {
                continue
            }
            // set needed fields
            plantation.getGrowable()?.setMoistureExposure(plantation.getGrowable()?.getMaxMoisture() ?: 1)
            plantation.getGrowable()?.setWasIrrigatedAtTick(data.getCurrentTick())
            val type = plantation.getGrowable()?.getCurrentPlant() ?: break
            m.setWorkedOnPlant(type)
            m.setPerformedAction(Action.IRRIGATING)
            m.setCurrentLocation(plantation.getCoordinate())
            // print the logger message
            Logger.logFarmAction(m.getID(), Action.IRRIGATING, plantation.getID(), m.getDuration())
            m.worked()
            // continue working until the machine hits 14
            while (m.canIStillWork()) {
                continueAction(farm, m)
            }
            // go back home
            pathFinder.moveToShedUnloadedMachine(m)
            return
        }
    }

    /**
     * Do the Mowing Plantation step of the farm act per machine and farm
     *
     */

    private fun checkAndIfPossibleMOWINGPlantations(farm: Farm, m: Machine) {
        // exit if machine already worked
        if (!m.canIWork() || m.getPerformedAction() != null || !m.getPossibleActions().contains(Action.MOWING)) return
        // get all mowable plantation list
        val mowablePlantations = data.getMap().getFarmPlantationsByID(farm.getID()).filter {
            it.canIBeMowed(data.getCurrentTick(), data.getYearTick()) &&
                pathFinder.isTileReachable(farm, m, it) &&
                m.getPossiblePlants().contains(it.getGrowable()?.getCurrentPlant())
        }
        // iterate over this list
        for (plantation in mowablePlantations) {
            // continue if the tile was already worked on through continuation
            if (plantation.checkWorkedOnThisTick(data.getCurrentTick())) {
                continue
            }
            // set the need fields
            plantation.getGrowable()?.setWasMowedAtTick(data.getCurrentTick())
            val type = plantation.getGrowable()?.getCurrentPlant() ?: break
            m.setWorkedOnPlant(type)
            m.setPerformedAction(Action.MOWING)
            m.setCurrentLocation(plantation.getCoordinate())
            m.worked()
            // write the logger message
            Logger.logFarmAction(m.getID(), Action.MOWING, plantation.getID(), m.getDuration())
            // continue working until machine hits 14
            while (m.canIStillWork()) {
                continueAction(farm, m)
            }
            // go back home
            pathFinder.moveToShedUnloadedMachine(m)
            return
        }
    }

    /**
     * Continue the action that the machine did before in radius 2 of current tile
     *
     */

    private fun continueAction(farm: Farm, m: Machine) {
        val action = m.getPerformedAction() ?: return
        // List of all possible Continuation Tiles for machine
        val possibleNext = pathFinder.getAllReachableTilesInRadiusTwo(farm, m).filter {
            it.canIBe(data.getCurrentTick(), data.getYearTick(), action) &&
                m.getPossiblePlants().contains(it.getGrowable()?.getCurrentPlant()) &&
                !it.checkWorkedOnThisTick(data.getCurrentTick())
        }
        // machine can not work anymore if its possibles are empty
        if (possibleNext.isEmpty()) {
            m.cannotworkanymore()
            return
        }
        // if we harvest we can only work on same plant again and if we irrigate we have to sort differently
        if (m.getPerformedAction() == Action.HARVESTING) {
            val harvestnext = possibleNext.filter {
                it.getGrowable()?.getCurrentPlant() == m.getWorkedOnPlant()
            }.sortedBy { it.getID() }
            if (harvestnext.isEmpty()) {
                m.cannotworkanymore()
                return
            }
            harvest(m, harvestnext[0])
        } else if (m.getPerformedAction() == Action.IRRIGATING) {
            val irrigationnext = possibleNext.sortedWith(compareBy<Tile> { it.getTileType() }.thenBy { it.getID() })
            Logger.logFarmAction(
                m.getID(),
                m.getPerformedAction() ?: Action.CUTTING,
                irrigationnext[0].getID(),
                m.getDuration()
            )
            irrigationnext[0].getGrowable()?.setWasIrrigatedAtTick(data.getCurrentTick())
            irrigationnext[0].getGrowable()?.setMoistureExposure(irrigationnext[0].getGrowable()?.getMaxMoisture() ?: 1)
            m.setCurrentLocation(irrigationnext[0].getCoordinate())
            m.worked()
        } else {
            Logger.logFarmAction(
                m.getID(),
                m.getPerformedAction() ?: Action.CUTTING,
                possibleNext[0].getID(),
                m.getDuration()
            )
            possibleNext[0].getGrowable()?.wasAtTick(
                m.getPerformedAction() ?: Action.SOWING,
                data.getCurrentTick()
            )
            m.setCurrentLocation(possibleNext[0].getCoordinate())
            m.worked()
        }
    }

    /**
     * continu sowing with consideration of used sowing plan
     *
     */

    private fun continueSow(farm: Farm, m: Machine, p: SowingPlan) {
        // list of all fields that would continue this sowing plan in radius two
        val nextField = pathFinder.getAllReachableTilesInRadiusTwo(farm, m).filter {
            it.getGrowable()?.getPossiblePlants()?.contains(p.getPlantToSow()) ?: false &&
                it.canIBeSowed(data.getCurrentTick(), data.getYearTick(), p.getPlantToSow()) &&
                p.getLocations().contains(it.getID())
        }
        // can't work anymore if no tile was found for the continuation
        if (nextField.isEmpty()) {
            m.cannotworkanymore()
            return
        }
        // set the needed fields and log
        nextField[0].getGrowable()?.setCurrentPlant(p.getPlantToSow())
        nextField[0].getGrowable()?.setWasSowedAtTick(data.getCurrentTick())
        Logger.logFarmAction(m.getID(), Action.SOWING, nextField[0].getID(), m.getDuration())
        Logger.logPerformedPlan(m.getID(), p.getPlantToSow(), p.getID())
        m.setCurrentLocation(nextField[0].getCoordinate())
        m.worked()
    }
}
