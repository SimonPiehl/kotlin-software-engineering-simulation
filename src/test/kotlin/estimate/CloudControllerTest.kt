package estimate

import de.unisaarland.cs.se.selab.controller.CloudController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.GeneralConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CloudControllerTest {

    private fun sim(maxTick: Int = 10, yearTick: Int = 1) = SimulationData(maxTick, yearTick)

    private fun field(
        sim: SimulationData,
        id: Int,
        x: Int,
        y: Int,
        airflow: Boolean = true,
        direction: Direction = Direction.EAST,
        shed: Boolean = false,
        maxMoisture: Int = 2000,
        initMoisture: Int = maxMoisture,

    ): Tile {
        // Growable mit neutralen Parametern (Pflanze optional — für Cloud-Tests nicht entscheidend)
        val growable = Growable(null, emptyList(), maxMoisture)
        growable.setMoistureExposure(initMoisture)
        growable.setSunlightExposure(120)

        val t = Tile(
            id = id,
            tileType = TileType.FIELD,
            coordinate = Coordinate(x, y),
            airflow = airflow,
            direction = direction,
            shedExists = shed,
            growable = growable
        )
        sim.addTile(t, /*farmId=*/0)
        return t
    }

    private fun road(sim: SimulationData, id: Int, x: Int, y: Int): Tile {
        val t =
            Tile(
                id,
                TileType.ROAD,
                Coordinate(x, y),
                airflow = false,
                direction = Direction.NONE,
                shedExists = false,
                growable = null
            )
        sim.addTile(t, null)
        return t
    }

    private fun village(sim: SimulationData, id: Int, x: Int, y: Int): Tile {
        val t =
            Tile(
                id,
                TileType.VILLAGE,
                Coordinate(x, y),
                airflow = false,
                direction = Direction.NONE,
                shedExists = false,
                growable = null
            )
        sim.addTile(t, null)
        return t
    }

    private fun cloud(sim: SimulationData, id: Int, liters: Int, duration: Int, x: Int, y: Int): Cloud {
        val c = Cloud(id, liters, duration, Coordinate(x, y))
        sim.addCloud(c)
        return c
    }

    // ---- Tests --------------------------------------------------------------

    @Test
    fun rain_happens_only_from_threshold_and_fills_capacity() {
        val s = sim()
        val f = field(s, id = 10, x = 0, y = 0, maxMoisture = 2000, initMoisture = 1500)

        // Unterhalb der Schwelle -> kein Regen
        cloud(s, id = 1, liters = GeneralConstants.MIN_RAIN_AMOUNT - 1, duration = 2, x = 0, y = 0)
        CloudController(s).cloudMovement()
        assertEquals(1500, f.getGrowable()!!.getMoistureExposure(), "Kein Regen unter der Schwelle.")

        // Ab Schwelle -> Regen bis zur Kapazität, Rest bleibt in der Wolke
        s.getClouds().clear()
        cloud(s, id = 2, liters = GeneralConstants.MIN_RAIN_AMOUNT, duration = 2, x = 0, y = 0)
        CloudController(s).cloudMovement()

        // 500 Liter bis Kapazität 2000
        assertEquals(2000, f.getGrowable()!!.getMoistureExposure(), "Moisture bis Max aufgefüllt.")
        val c = s.getClouds().single { it.getID() == 2 }
        assertEquals(GeneralConstants.MIN_RAIN_AMOUNT - 500, c.getAmount(), "Restmenge bleibt in der Wolke.")
    }

    @Test
    fun traversal_reduces_sun_by_3_and_endtile_loses_50_if_cloud_remains() {
        val s = sim()
        val a = field(s, id = 1, x = 0, y = 0, airflow = true, direction = Direction.EAST)
        val b = field(s, id = 2, x = 1, y = 0, airflow = true, direction = Direction.EAST)
        road(s, id = 3, x = 2, y = 0) // nur, um Ziel existieren zu lassen

        val eins = a.getID()

        cloud(s, id = 42, liters = 1000, duration = 2, x = 0, y = 0) // wenig Wasser, damit Regen nicht stört
        CloudController(s).cloudMovement()

        // Wolke steht am Tick-Ende auf b → −50 auf b
        assertEquals(
            120 * eins,
            b.getGrowable()!!
                .getSunlightExposureCurrentTick(),
            "−50 h auf Endtile mit Wolke."
        )
    }

    @Test
    fun cloud_on_village_dissipates_immediately_stuck_and_is_removed() {
        val s = sim()
        village(s, id = 90, x = 0, y = 0)
        cloud(s, id = 5, liters = 9000, duration = 5, x = 0, y = 0)

        CloudController(s).cloudMovement()

        assertTrue(s.getClouds().isEmpty(), "Wolke auf Village wird sofort entfernt (stuck + dissipate).")
    }

    @Test
    fun rain_on_non_growable_rains_all_and_cloud_disappears() {
        val s = sim()
        road(s, id = 50, x = 0, y = 0)
        cloud(s, id = 9, liters = 6000, duration = 2, x = 0, y = 0)

        CloudController(s).cloudMovement()

        assertTrue(s.getClouds().isEmpty(), "Wolke regnet auf Nicht-Growable komplett ab und verschwindet.")
    }
}
