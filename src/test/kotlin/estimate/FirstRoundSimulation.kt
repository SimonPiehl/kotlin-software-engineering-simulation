package estimate

import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.SoilMoistureAndSunController
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertEquals

class FirstRoundSimulation {

    private val data = SimulationData(25, 5)
    private val ehc = EstimateHarvestController(data)
    private val coord1 = Coordinate(0, 0)
    private val coord2 = Coordinate(2, 0)
    private val coord3 = Coordinate(4, 0)
    private val coord4 = Coordinate(6, 0)
    private val smc = SoilMoistureAndSunController(data)

    private fun plantationTile(plant: PlantType, estimate: Int, id: Int = 1, coord: Coordinate): Tile {
        val g = Growable(plant, 1000)
        g.setCropsExpected(estimate)
        return Tile(id, TileType.PLANTATION, coord, true, Direction.SOUTH, false, g)
    }

    @Test
    fun firstround() {
        // Spez: Bis inkl. Ende des 2. Fensters erlaubt -> keine Strafe am Endtag
        val t1 = plantationTile(PlantType.APPLE, 1_000_000, id = 101, coord1)
        val t2 = plantationTile(PlantType.ALMOND, 1000000, 102, coord2)
        val t3 = plantationTile(PlantType.CHERRY, 1000000, 103, coord3)
        val t4 = plantationTile(PlantType.GRAPE, 1000000, 104, coord4)
        data.getMap().addTile(t1, 1)
        data.getMap().addTile(t2, 1)
        data.getMap().addTile(t3, 1)
        data.getMap().addTile(t4, 1)

        data.setYearTick(20) // Endtag des 2. Fensters
        data.setCurrentTick(0)
        ehc.firstroundsimulation()

        assertEquals(500000, t1.getGrowable()!!.getCropsExpected(), "Estimate Apple nicht halbier.")
        assertEquals(900000, t2.getGrowable()!!.getCropsExpected(), "Estimate Almond nicht applied")
        assertEquals(0, t3.getGrowable()!!.getCropsExpected(), "Estimate Cherry nicht applied.")
        assertEquals(1028850, t4.getGrowable()!!.getCropsExpected(), "Estimate Grape nicht applied.")
    }

    @Test
    fun firstroundagain() {
        // Spez: Bis inkl. Ende des 2. Fensters erlaubt -> keine Strafe am Endtag
        val t1 = plantationTile(PlantType.APPLE, 1_000_000, id = 101, coord1)
        val t2 = plantationTile(PlantType.ALMOND, 1000000, 102, coord2)
        val t3 = plantationTile(PlantType.CHERRY, 1000000, 103, coord3)
        val t4 = plantationTile(PlantType.GRAPE, 1000000, 104, coord4)
        data.getMap().addTile(t1, 1)
        data.getMap().addTile(t2, 1)
        data.getMap().addTile(t3, 1)
        data.getMap().addTile(t4, 1)

        data.setYearTick(19) // Endtag des 2. Fensters
        data.setCurrentTick(0)
        ehc.firstroundsimulation()

        assertEquals(1000000, t1.getGrowable()!!.getCropsExpected(), "Estimate Apple nicht halbier.")
        assertEquals(1000000, t2.getGrowable()!!.getCropsExpected(), "Estimate Almond nicht applied")
        assertEquals(0, t3.getGrowable()!!.getCropsExpected(), "Estimate Cherry nicht applied.")
        assertEquals(1083000, t4.getGrowable()!!.getCropsExpected(), "Estimate Grape nicht applied.")
    }

    @Test
    fun firstroundagaincherryapplied() {
        // Spez: Bis inkl. Ende des 2. Fensters erlaubt -> keine Strafe am Endtag
        val t1 = plantationTile(PlantType.APPLE, 1_000_000, id = 101, coord1)
        val t2 = plantationTile(PlantType.ALMOND, 1000000, 102, coord2)
        val t3 = plantationTile(PlantType.CHERRY, 1000000, 103, coord3)
        val t4 = plantationTile(PlantType.GRAPE, 1000000, 104, coord4)
        data.getMap().addTile(t1, 1)
        data.getMap().addTile(t2, 1)
        data.getMap().addTile(t3, 1)
        data.getMap().addTile(t4, 1)

        data.setYearTick(15) // Endtag des 2. Fensters
        data.setCurrentTick(0)
        ehc.firstroundsimulation()

        assertEquals(1000000, t1.getGrowable()!!.getCropsExpected(), "Estimate Apple nicht halbier.")
        assertEquals(1000000, t2.getGrowable()!!.getCropsExpected(), "Estimate Almond nicht applied")
        assertEquals(300000, t3.getGrowable()!!.getCropsExpected(), "Estimate Cherry nicht applied.")
        assertEquals(1000000, t4.getGrowable()!!.getCropsExpected(), "Estimate Grape nicht applied.")
    }

    @Test
    fun firstroundagain21th() {
        // Spez: Bis inkl. Ende des 2. Fensters erlaubt -> keine Strafe am Endtag
        val t1 = plantationTile(PlantType.APPLE, 1_000_000, id = 101, coord1)
        val t2 = plantationTile(PlantType.ALMOND, 1000000, 102, coord2)
        val t3 = plantationTile(PlantType.CHERRY, 1000000, 103, coord3)
        val t4 = plantationTile(PlantType.GRAPE, 1000000, 104, coord4)
        data.getMap().addTile(t1, 1)
        data.getMap().addTile(t2, 1)
        data.getMap().addTile(t3, 1)
        data.getMap().addTile(t4, 1)

        data.setYearTick(21) // Endtag des 2. Fensters
        data.setCurrentTick(0)
        ehc.firstroundsimulation()
        smc.reduceSoilMoistureAndAdaptSun()

        assertEquals(1700000, t1.getGrowable()!!.getCropsExpected(), "Estimate Apple nicht halbier.")
        assertEquals(800000, t2.getGrowable()!!.getCropsExpected(), "Estimate Almond nicht applied")
        assertEquals(1200000, t3.getGrowable()!!.getCropsExpected(), "Estimate Cherry nicht applied.")
        assertEquals(1200000, t4.getGrowable()!!.getCropsExpected(), "Estimate Grape nicht applied.")
    }
}
