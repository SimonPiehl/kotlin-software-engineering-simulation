package estimate

import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.PlantConstants
import kotlin.test.Test
import kotlin.test.assertEquals

class EstimateHarvestTestsCutting {

    private val data = SimulationData(25, 5)
    private val ehc = EstimateHarvestController(data)
    private val coord = Coordinate(0, 0)

    private fun plantationTile(plant: PlantType, estimate: Int, id: Int = 1): Tile {
        val g = Growable(plant, 1000)
        g.setCropsExpected(estimate)
        return Tile(id, TileType.PLANTATION, coord, true, Direction.SOUTH, false, g)
    }

    // --------- APPLE / Mehrfenster-Fall (Nov ODER Feb) ----------

    @Test
    fun cutting_Apple_OnLastAllowedDay_NoPenalty_ReturnsTrue() {
        // Spez: Bis inkl. Ende des 2. Fensters erlaubt -> keine Strafe am Endtag
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 101)

        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_END_TICK) // Endtag des 2. Fensters
        t.getGrowable()?.setWasCutAtTick(data.getYearTick())

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(1_000_000, t.getGrowable()!!.getCropsExpected(), "Estimate darf am Endtag nicht halbiert werden.")
    }

    @Test
    fun cutting_Apple_OneTickAfterWindow_NoCut_PenalizeOnce_ReturnsFalse() {
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 102)
        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_END_TICK + 1) // direkt nach Fenster

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(
            1000_000,
            t.getGrowable()?.getCropsExpected(),
            "Nachfrist verpasst -> einmalige Halbierung erwartet."
        )
    }

    @Test
    fun cutting_Apple_CutInFirstWindow_NoPenalty_EvenAfterWindow() {
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 103)
        // Schnitt im ersten Fenster (November)
        t.getGrowable()!!.setWasCutAtTick(PlantConstants.APPLE_CUTTING_FIRST_START_TICK)
        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_END_TICK + 1)

        ehc.calculateEstimateHarvestTile(t)
        assertEquals(1_000_000, t.getGrowable()!!.getCropsExpected(), "Estimate darf nicht verändert werden.")
    }

    @Test
    fun cutting_Apple_PreviousYearCut_ShouldNotCount_PenalizeNow_EXPECTEDFAIL() {
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 104)
        // Simuliere Vorjahres-Schnitt (irgendein alter Tick ungleich 0)
        t.getGrowable()!!.setWasCutAtTick(0)
        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_END_TICK)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(1000_000, t.getGrowable()!!.getCropsExpected(), "Ohne diesjährigen Schnitt muss halbiert werden.")
    }

    @Test
    fun cutting_Apple_AfterWindow_IdempotentPenalty_EXPECTEDFAIL() {
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 105)
        t.getGrowable()?.setWasCutAtTick(-1)
        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_END_TICK)

        // 1. Bewertung: einmal halbieren
        ehc.calculateEstimateHarvestTile(t)
        assertEquals(500_000, t.getGrowable()!!.getCropsExpected(), "Einmalige Halbierung erwartet.")
    }

    @Test
    fun cutting_Apple_BetweenWindows_NoPenaltyYet_ReturnsTrue() {
        val t = plantationTile(PlantType.APPLE, 1_000_000, id = 106)
        // Zwischen Nov-Ende und Feb-Start ist noch Zeit -> keine Strafe
        data.setYearTick(PlantConstants.APPLE_CUTTING_SECOND_START_TICK - 1)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(
            1_000_000,
            t.getGrowable()!!.getCropsExpected(),
            "Estimate darf vor Ablauf nicht verändert werden."
        )
    }

    // --------- GRAPE / Einzelfenster (Juli 2. Tick ODER August) ----------

    @Test
    fun cutting_Grape_OnEndDay_NoPenalty_ReturnsTrue() {
        val t = plantationTile(PlantType.GRAPE, 1_000_000, id = 201)
        data.setYearTick(PlantConstants.GRAPE_CUTTING_FIRST_END_TICK) // letzter erlaubter Tag
        data.setCurrentTick(28)
        t.getGrowable()?.setWasCutAtTick(data.getCurrentTick())

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(1_000_000, t.getGrowable()!!.getCropsExpected(), "Estimate darf am Endtag nicht halbiert werden.")
    }

    @Test
    fun cutting_Grape_OneTickAfterWindow_NoCut_Penalize_ReturnsFalse() {
        val t = plantationTile(PlantType.GRAPE, 1_000_000, id = 202)
        data.setYearTick(PlantConstants.GRAPE_CUTTING_FIRST_END_TICK + 1)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(
            950000,
            t.getGrowable()!!.getCropsExpected(),
            "Einmalige Halbierung nach Ablauf des Fensters erwartet."
        )
    }

    // --------- CHERRY / Mehrfenster wie Apple ----------

    @Test
    fun cutting_Cherry_CutOnSecondWindowEnd_NoPenalty_ReturnsTrue() {
        val t = plantationTile(PlantType.CHERRY, 1_200_000, id = 301)
        // Schnitt genau am Ende des zweiten Fensters
        t.getGrowable()!!.setWasCutAtTick(PlantConstants.CHERRY_CUTTING_SECOND_END_TICK)
        data.setYearTick(PlantConstants.CHERRY_CUTTING_SECOND_END_TICK)

        ehc.calculateEstimateHarvestTile(t)

        assertEquals(1_200_000, t.getGrowable()!!.getCropsExpected(), "Estimate bleibt unverändert.")
    }
}
