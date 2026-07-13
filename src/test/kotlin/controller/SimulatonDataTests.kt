package controller

import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimulatonDataTests {

    // --------- Test-Helper (bei Bedarf Signaturen hier anpassen) ---------

    private fun makeFieldTile(id: Int): Tile {
        val g = Growable(mutableListOf(), /*maxMoisture*/ 10)
        return Tile(
            id = id,
            tileType = TileType.FIELD,
            coordinate = Coordinate(0, id),
            airflow = false,
            direction = Direction.NORTH,
            shedExists = false,
            growable = g
        )
    }

    private fun makePlantationTile(id: Int): Tile {
        val g = Growable(PlantType.APPLE, emptyList(), /*maxMoisture*/ 10)
        return Tile(
            id = id,
            tileType = TileType.PLANTATION,
            coordinate = Coordinate(1, id),
            airflow = false,
            direction = Direction.NORTH,
            shedExists = false,
            growable = g
        )
    }

    // -------------------- Grundlegende Getter/Setter ---------------------

    @Test
    fun `ticks getter setter roundtrip`() {
        val data = SimulationData(maxTick = 10, yearTick = 7)
        assertEquals(10, data.getMaxTick())
        assertEquals(7, data.getYearTick())
        assertEquals(0, data.getCurrentTick())

        data.setCurrentTick(5)
        data.setYearTick(12)
        assertEquals(5, data.getCurrentTick())
        assertEquals(12, data.getYearTick())
    }

    @Test
    fun `tilesWhereDrought add sorts by tile id and clear empties`() {
        val data = SimulationData(1, 1)
        val t3 = makePlantationTile(3)
        val t1 = makeFieldTile(1)
        val t2 = makePlantationTile(2)

        data.addTilesWhereDrought(t3)
        data.addTilesWhereDrought(t1)
        data.addTilesWhereDrought(t2)

        assertEquals(listOf(1, 2, 3), data.getTilesWhereDrought().map { it.getID() })

        data.clearTilesWhereDrought()
        assertTrue(data.getTilesWhereDrought().isEmpty())
    }
}
