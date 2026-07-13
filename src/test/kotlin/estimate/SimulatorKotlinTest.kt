package estimate

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.Simulator
import de.unisaarland.cs.se.selab.util.GeneralConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimulatorKotlinTest {

    @Test
    fun simulate_stops_at_maxTick_and_advances_yearTick() {
        // given
        val startYearTick = 5
        val maxTick = 3
        val data = SimulationData(maxTick, startYearTick)
        val sut = Simulator(data)

        // when
        sut.simulate()

        // then
        assertEquals(maxTick, data.getCurrentTick(), "currentTick sollte exakt maxTick sein")
        // yearTick: 5 -> 6 -> 7 -> 8 (3 Runden)
        assertEquals(8, data.getYearTick(), "yearTick sollte pro Runde via (yt % 24) + 1 weiterlaufen")
    }

    @Test
    fun yearTick_wraps_from_24_to_1() {
        // given: 3 Runden ab 23: 23 -> 24 -> 1 -> 2
        val data = SimulationData(3, 23)
        val sut = Simulator(data)

        // when
        sut.simulate()

        // then
        assertEquals(3, data.getCurrentTick())
        assertEquals(2, data.getYearTick(), "yearTick sollte nach 24 wieder auf 1 springen")
    }

    @Test
    fun simulate_is_capped_by_MAX_MAX_TICK() {
        // given: maxTick deutlich größer als Cap
        val cap = GeneralConstants.MAX_MAX_TICK
        val data = SimulationData(cap + 100, 10)
        val sut = Simulator(data)

        // when
        sut.simulate()

        // then
        assertEquals(cap, data.getCurrentTick(), "Simulation muss am Cap stoppen")
        // (yearTick wurde cap-mal inkrementiert; genauer Wert hängt vom Start + Wraps ab)
        assertTrue(data.getYearTick() in 1..24, "yearTick bleibt immer im Bereich 1..24")
    }

    @Test
    fun simulate_does_not_throw_with_empty_world() {
        // given: keine besonderen Entities nötig – leeres Setup reicht
        val data = SimulationData(1, 1)
        val sut = Simulator(data)

        // when
        sut.simulate()

        // then
        assertEquals(1, data.getCurrentTick())
        assertEquals(2, data.getYearTick())
    }

    private fun expectedYearTick(start: Int, steps: Int): Int {
        // Entspricht (yt % 24) + 1 pro Tick
        return ((start - 1 + steps) % 24) + 1
    }

    @Test
    fun `simulate resets currentTick to 0 even if it was nonzero before`() {
        val data = SimulationData(2, 10)
        // Simuliere "verschmutzten" Zustand vor dem Start:
        data.setCurrentTick(999)

        val sut = Simulator(data)
        sut.simulate()

        assertEquals(2, data.getCurrentTick(), "Nach simulate() sollte currentTick == maxTick sein")
        assertEquals(expectedYearTick(10, 2), data.getYearTick())
    }

    @Test
    fun `no rounds when maxTick is 0, but currentTick is reset and yearTick unchanged`() {
        val startYt = 17
        val data = SimulationData(0, startYt)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(0, data.getCurrentTick(), "Ohne Runden bleibt currentTick bei 0 (wird vorher auf 0 gesetzt).")
        assertEquals(startYt, data.getYearTick(), "yearTick ändert sich nicht ohne Runden.")
    }

    @Test
    fun `single tick from 24 wraps to 1`() {
        val data = SimulationData(1, 24)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(1, data.getCurrentTick())
        assertEquals(1, data.getYearTick(), "24 -> 1 bei genau einem Tick.")
    }

    @Test
    fun `exact 24 ticks returns to same yearTick`() {
        val startYt = 7
        val data = SimulationData(24, startYt)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(24, data.getCurrentTick())
        assertEquals(startYt, data.getYearTick(), "Nach genau 24 Ticks ist man wieder beim Start-YearTick.")
    }

    @Test
    fun `multiple full cycles advance correctly`() {
        val startYt = 3
        val ticks = 48 // zwei vollständige 24er-Zyklen
        val data = SimulationData(ticks, startYt)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(ticks, data.getCurrentTick())
        assertEquals(expectedYearTick(startYt, ticks), data.getYearTick())
    }

    @Test
    fun `simulate can be called twice back to back`() {
        val startYt = 20
        val ticks = 5
        val data = SimulationData(ticks, startYt)
        val sut = Simulator(data)

        // Run 1
        sut.simulate()
        val ytAfterRun1 = expectedYearTick(startYt, ticks)
        assertEquals(ticks, data.getCurrentTick())
        assertEquals(ytAfterRun1, data.getYearTick())

        // Run 2 (mit gleichem maxTick; Simulator setzt currentTick erneut auf 0)
        sut.simulate()
        val ytAfterRun2 = expectedYearTick(ytAfterRun1, ticks)
        assertEquals(ticks, data.getCurrentTick())
        assertEquals(ytAfterRun2, data.getYearTick())
    }

    @Test
    fun `cap at MAX_MAX_TICK when maxTick is huge`() {
        val cap = GeneralConstants.MAX_MAX_TICK
        val data = SimulationData(cap * 2, 22)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(cap, data.getCurrentTick(), "Laufzeit wird am globalen Cap begrenzt.")
        assertTrue(data.getYearTick() in 1..24, "yearTick bleibt im Bereich 1..24.")
    }

    @Test
    fun `small run starting at 1 advances to expected`() {
        val data = SimulationData(7, 1)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(7, data.getCurrentTick())
        assertEquals(8, data.getYearTick(), "1 -> 8 nach 7 Ticks.")
    }

    @Test
    fun `boundary start at 24 with many steps`() {
        val startYt = 24
        val ticks = 37
        val data = SimulationData(ticks, startYt)
        val sut = Simulator(data)

        sut.simulate()

        assertEquals(ticks, data.getCurrentTick())
        assertEquals(expectedYearTick(startYt, ticks), data.getYearTick(), "Wrap + Versatz korrekt berechnet.")
    }
}
