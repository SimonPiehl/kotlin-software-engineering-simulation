package estimate

import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.GeneralConstants
import de.unisaarland.cs.se.selab.util.PlantConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EstimateBranch {

    private fun fieldTile(
        id: Int,
        plant: PlantType,
        crops: Int = plant.idealEstimate,
        coord: Coordinate = Coordinate(0, id)
    ): Tile {
        val g = Growable(mutableListOf(plant), /*maxMoisture*/ 10)
        g.setCurrentPlant(plant)
        g.setCropsExpected(crops)
        return Tile(
            id,
            TileType.FIELD,
            coord,
            airflow = false,
            direction = Direction.NORTH,
            shedExists = false,
            growable = g
        )
    }

    private fun plantationTile(
        id: Int,
        plant: PlantType,
        crops: Int = plant.idealEstimate,
        coord: Coordinate = Coordinate(1, id)
    ): Tile {
        val g = Growable(plant, emptyList(), /*maxMoisture*/ 10)
        g.setCropsExpected(crops)
        return Tile(
            id,
            TileType.PLANTATION,
            coord,
            airflow = false,
            direction = Direction.NORTH,
            shedExists = false,
            growable = g
        )
    }

    private fun dataWithTiles(maxTick: Int, yearTick: Int, vararg tiles: Tile): SimulationData {
        val data = SimulationData(maxTick, yearTick)
        tiles.forEach { data.addTile(it, null) }
        return data
    }

    // ---------- calculateMoistureAndSunReduction ----------
    @Test
    fun `MoistureAndSunReduction - zero moisture hard-sets zero and returns false`() {
        val t = fieldTile(1, PlantType.POTATO, crops = 100)
        t.getGrowable()!!.setMoistureExposure(0) // <= 0 -> sofort 0 + false
        t.getGrowable()!!.setSunlightExposure(PlantType.POTATO.idealSunlight)

        val data = dataWithTiles(1, 1, t)
        val ctrl = EstimateHarvestController(data)
        ctrl.calculateEstimateHarvestTile(t)
        assertEquals(0, t.getGrowable()!!.getCropsExpected())
    }

    // ---------- calculateSowingEstimation ----------
    @Test
    fun `SowingEstimation - penalty when sowed at currentTick and yearTick == end+1`() {
        val plant = PlantType.POTATO
        val t = fieldTile(3, plant, crops = 1000)
        val schedule = plant.schedule.getTicks(Action.SOWING)
        val end = schedule[1]
        val desiredYearTick = (end + 1) % GeneralConstants.AMOUNT_24
        val data = dataWithTiles(1, desiredYearTick, t)
        val ctrl = EstimateHarvestController(data)

        // Aussaat genau in diesem Tick
        t.getGrowable()!!.setWasSowedAtTick(data.getCurrentTick())
        val before = t.getGrowable()!!.getCropsExpected()
        ctrl.calculateEstimateHarvestTile(t)
        val after = t.getGrowable()!!.getCropsExpected()

        assertTrue(after < before)
        // -20% einmal
        assertEquals(600, after)
    }

    @Test
    fun `SowingEstimation - double penalty when yearTick == end+2`() {
        val plant = PlantType.POTATO
        val t = fieldTile(4, plant, crops = 1000)
        val schedule = plant.schedule.getTicks(Action.SOWING)
        val end = schedule[1]
        val desiredYearTick = (end + 2) % GeneralConstants.AMOUNT_24
        val data = dataWithTiles(1, desiredYearTick, t)
        val ctrl = EstimateHarvestController(data)

        t.getGrowable()!!.setWasSowedAtTick(data.getCurrentTick())
        val before = t.getGrowable()!!.getCropsExpected()
        ctrl.calculateEstimateHarvestTile(t)
        val after = t.getGrowable()!!.getCropsExpected()

        assertEquals(440, after)
    }

    // ---------- calculateHarvestEstimation ----------
    @Test
    fun `HarvestEstimation - if already harvested or estimate zero then sets zero and returns true`() {
        val t = fieldTile(5, PlantType.POTATO, crops = 0)
        val data = dataWithTiles(1, 1, t)
        val ctrl = EstimateHarvestController(data)
        // currentEstimation == 0 -> bleibt 0, true
        ctrl.calculateEstimateHarvestTile(t)
        assertEquals(0, t.getGrowable()!!.getCropsExpected())

        // harvested in diesem Tick
        t.getGrowable()!!.setCropsExpected(123)
        t.getGrowable()!!.setWasHarvestedAtTick(data.getCurrentTick())
        ctrl.calculateEstimateHarvestTile(t)

        assertEquals(0, t.getGrowable()!!.getCropsExpected())
    }

    // ---------- calculateWeedingEstimation ----------
    @Test
    fun `WeedingEstimation - POTATO requires weeding every 2 ticks after sowing`() {
        val t = fieldTile(7, PlantType.POTATO, crops = 1000)
        val data = dataWithTiles(100, 1, t)
        val ctrl = EstimateHarvestController(data)

        // Sowing at tick 0 -> at tick 2 should weed; if not, penalty
        t.getGrowable()!!.setWasSowedAtTick(0)
        data.setCurrentTick(2)
        val before = t.getGrowable()!!.getCropsExpected()
        ctrl.calculateEstimateHarvestTile(t) // nicht gejätet -> penalty angewandt
        assertTrue(t.getGrowable()!!.getCropsExpected() < before)

        // Now weed at tick 4 -> no penalty
        data.setCurrentTick(4)
        t.getGrowable()!!.addWasWeededAtTick(4)
    }

    @Test
    fun WeedingEstimationOAT() {
        val t = fieldTile(8, PlantType.OAT, crops = 1000)
        val data = dataWithTiles(100, 1, t)
        val ctrl = EstimateHarvestController(data)

        t.getGrowable()!!.setWasSowedAtTick(10)
        data.setCurrentTick(11) // in Fenster
        val before = t.getGrowable()!!.getCropsExpected()
        ctrl.calculateEstimateHarvestTile(t)
        assertTrue(t.getGrowable()!!.getCropsExpected() < before)
    }

    @Test
    fun `WeedingEstimation - WHEAT must weed at +3 and +9`() {
        val t = fieldTile(9, PlantType.WHEAT, crops = 1000)
        val data = dataWithTiles(100, 1, t)
        val ctrl = EstimateHarvestController(data)

        t.getGrowable()!!.setWasSowedAtTick(0)

        data.setCurrentTick(3) // Pflichtzeitpunkt
        val before3 = t.getGrowable()!!.getCropsExpected()
        ctrl.calculateEstimateHarvestTile(t)
        assertTrue(t.getGrowable()!!.getCropsExpected() < before3)

        data.setCurrentTick(9) // Pflichtzeitpunkt
        t.getGrowable()!!.addWasWeededAtTick(9)
        ctrl.calculateEstimateHarvestTile(t)
    }

    // ---------- calculateCutting ----------
    @Test
    fun `Cutting - if not cut at last allowed tick, apply reduction and return false`() {
        val t = plantationTile(10, PlantType.GRAPE, crops = 1000)
        val data = dataWithTiles(10, 1, t)
        val ctrl = EstimateHarvestController(data)

        val cuttingTicks = PlantType.GRAPE.schedule.getTicks(Action.CUTTING)
        // „last()“ ist das Ende der Cutting-Periode; genau dort nicht geschnitten -> Malus
        data.setYearTick(cuttingTicks.last())
        t.getGrowable()!!.setWasCutAtTick(-1)

        ctrl.calculateEstimateHarvestTile(t)
        assertTrue(t.getGrowable()!!.getCropsExpected() < 1000)
    }

    @Test
    fun `Cutting - if cut at last allowed tick, state resets and returns true`() {
        val t = plantationTile(11, PlantType.GRAPE, crops = 1000)
        val data = dataWithTiles(10, 1, t)
        val ctrl = EstimateHarvestController(data)

        val cuttingTicks = PlantType.GRAPE.schedule.getTicks(Action.CUTTING)
        data.setYearTick(cuttingTicks.last())
        t.getGrowable()!!.setWasCutAtTick(data.getCurrentTick())

        ctrl.calculateEstimateHarvestTile(t)
        assertEquals(-1, t.getGrowable()!!.getWasCutAtTick()) // wird im Code auf -1 zurückgesetzt
    }

    // ---------- calculateMowingEstimation ----------
    @Test
    fun `Mowing - missed mowing on CHERRY reduces estimate and returns false`() {
        val t = plantationTile(12, PlantType.CHERRY, crops = 1000)
        val data = dataWithTiles(10, 1, t)
        val ctrl = EstimateHarvestController(data)

        val mowingTicks = PlantType.CHERRY.schedule.getTicks(Action.MOWING)
        data.setYearTick(mowingTicks[0]) // CHERRY hat ein Tickfenster
        // nicht gemäht
        t.getGrowable()!!.setWasMowedAtTick(-1)

        ctrl.calculateEstimateHarvestTile(t)
        assertTrue(t.getGrowable()!!.getCropsExpected() < 1000)
    }

    @Test
    fun `Mowing - if harvested this tick, mowing ignored and returns true`() {
        val t = plantationTile(13, PlantType.APPLE, crops = 1000)
        val data = dataWithTiles(10, 1, t)
        val ctrl = EstimateHarvestController(data)

        val mowingTicks = PlantType.APPLE.schedule.getTicks(Action.MOWING)
        data.setYearTick(mowingTicks[0])
        t.getGrowable()!!.setWasHarvestedAtTick(data.getCurrentTick())

        ctrl.calculateEstimateHarvestTile(t)
        assertEquals(0, t.getGrowable()!!.getCropsExpected())
    }

    // ---------- droughtCheck & droughtCheckTile ----------
    @Test
    fun `droughtCheck - disables plantation growables permanently`() {
        val p = plantationTile(14, PlantType.APPLE, crops = 500)
        val f = fieldTile(15, PlantType.POTATO, crops = 500)
        val data = dataWithTiles(1, 1, p, f)
        data.addTilesWhereDrought(p)
        data.addTilesWhereDrought(f) // sollte ignoriert werden für FIELD

        val ctrl = EstimateHarvestController(data)
        ctrl.calculateEstimateHarvest()

        assertTrue(p.getGrowable()!!.getPermanentDisabled())
        assertFalse(f.getGrowable()!!.getPermanentDisabled())
    }

    // ---------- firstroundsimulation, estimateGrapeFirstRound, firstRoundZero ----------

    // ---------- calculateEstimateHarvestTile (wichtig: beide Tile-Typen, Aktionenlisten) ----------

    @Test
    fun `calculateEstimateHarvestTile - PLANTATION triggers moisture, cutting, mowing, harvest`() {
        val t = plantationTile(20, PlantType.GRAPE, crops = 1000)
        val data = dataWithTiles(1, 1, t)
        val ctrl = EstimateHarvestController(data)

        // moisture unter Ideal
        t.getGrowable()!!.setMoistureExposure(PlantType.GRAPE.idealMoisture - 2 * PlantConstants.MOISTURE_DIFFERENCE)
        // cutting verpasst am letzten Tick
        val cuttingEnd = PlantType.GRAPE.schedule.getTicks(Action.CUTTING).last()
        data.setYearTick(cuttingEnd)
        t.getGrowable()!!.setWasCutAtTick(-1)
        // mowing verpasst in Fenster
        val mowingTicks = PlantType.GRAPE.schedule.getTicks(Action.MOWING)
        data.setYearTick(mowingTicks[0])
        t.getGrowable()!!.setWasMowedAtTick(-1)
        // harvest im Straf-Fenster
        val end = PlantType.GRAPE.schedule.getTicks(Action.HARVESTING)[1]
        data.setYearTick(end)

        ctrl.calculateEstimateHarvestTile(t)
        assertTrue(t.getGrowable()!!.getCropsExpected() in 1..999)
    }
}
