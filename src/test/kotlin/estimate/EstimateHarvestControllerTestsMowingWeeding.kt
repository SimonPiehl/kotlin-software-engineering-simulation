package estimate

import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertEquals

class EstimateHarvestControllerTestsMowingWeeding {

    val coord1 = Coordinate(0, 0)
    val coord2 = Coordinate(1, 0)

    val growPlant = Growable(
        PlantType.APPLE,
        listOf(PlantType.APPLE, PlantType.ALMOND, PlantType.GRAPE, PlantType.CHERRY),
        1000
    )
    val growPotato = Growable(
        PlantType.POTATO,
        listOf(PlantType.POTATO),
        1000
    )
    val growOat = Growable(
        PlantType.OAT,
        listOf(PlantType.OAT),
        1000
    )

    val growWheat = Growable(
        PlantType.WHEAT,
        listOf(PlantType.WHEAT),
        1000
    )
    val growPumpkin = Growable(
        PlantType.PUMPKIN,
        listOf(PlantType.PUMPKIN),
        1000
    )

    fun initialization(
        plant1: PlantType,
        plant2: PlantType,
        yearTick: Int,
        currentTick: Int,
        moisture1: Int,
        moisture2: Int,
        sun1: Int,
        sun2: Int
    ): SimulationData {
        growPlant.setCurrentPlant(plant1)
        val tile1 = Tile(
            1,
            TileType.PLANTATION,
            coord1,
            true,
            Direction.EAST,
            false,
            growPlant
        )
        val tile2: Tile
        when (plant2) {
            PlantType.OAT -> tile2 = Tile(
                2, TileType.FIELD, coord2, true, Direction.NORTH,
                false, growOat
            )

            PlantType.POTATO -> tile2 = Tile(
                2, TileType.FIELD, coord2, true, Direction.NORTH,
                false, growPotato
            )

            PlantType.WHEAT -> tile2 = Tile(
                2, TileType.FIELD, coord2, true, Direction.NORTH,
                false, growWheat
            )

            PlantType.PUMPKIN -> tile2 = Tile(
                2, TileType.FIELD, coord2, true, Direction.NORTH,
                false, growPumpkin
            )

            else -> tile2 = Tile(
                2, TileType.FIELD, coord2, true, Direction.NORTH,
                false, growOat
            )
        }

        tile1.getGrowable()?.setSunlightExposure(sun1)
        tile2.getGrowable()?.setSunlightExposure(sun2)
        tile1.getGrowable()?.setMoistureExposure(moisture1)
        tile2.getGrowable()?.setMoistureExposure(moisture2)

        val data = SimulationData(20, yearTick)
        data.setCurrentTick(currentTick)
        data.addTile(tile1, 1)
        data.addTile(tile2, 2)

        return data
    }

    @Test
    fun testAlmondPotatoMowingWeedingmissed() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            12,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(34)
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(900000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingmissedWeedingmissed() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(34)
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(900000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingmissedWeedingnotneeded() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(33)
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingmissedWeedingpeformed() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(34, 36))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingmissedWeedingmissedonlythistick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(34))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(900000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingmissedWeedingmissedonlylasttick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingDoneWeedingmissedonlylasttick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            11,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(36)
        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingDoneTick2Weedingmissedonlylasttick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            17,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(36)
        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingNotWeededWeedingmissedonlylasttick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            17,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(0)
        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testAlmondPotatoMowingWeededLastPointWeedingmissedonlylasttick() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.POTATO,
            17,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(30)
        tile2?.getGrowable()?.setWasSowedAtTick(32)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testGrapeWheatMowingDoneWeedingDone() {
        val data = initialization(
            PlantType.GRAPE,
            PlantType.OAT,
            13,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(36)
        tile2?.getGrowable()?.setWasSowedAtTick(35)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testGrapeWheatMowingNotDoneWeedingNot() {
        val data = initialization(
            PlantType.GRAPE,
            PlantType.OAT,
            13,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(3)
        tile2?.getGrowable()?.setWasSowedAtTick(35)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(30))
        esc.calculateEstimateHarvest()

        assertEquals(720000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(900000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testWheatMowingNotDoneWheatWeedingDone() {
        val data = initialization(
            PlantType.GRAPE,
            PlantType.WHEAT,
            2,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(3)
        tile2?.getGrowable()?.setWasSowedAtTick(33)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(36))
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }

    @Test
    fun testWheatMowingNotDoneWheatWeedingNotDone() {
        val data = initialization(
            PlantType.GRAPE,
            PlantType.WHEAT,
            2,
            36,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(800000)
        tile2?.getGrowable()?.setCropsExpected(1000000)

        tile1?.getGrowable()?.setWasMowedAtTick(3)
        tile2?.getGrowable()?.setWasSowedAtTick(33)
        tile2?.getGrowable()?.setWasWeededAtTick(mutableListOf(30))
        esc.calculateEstimateHarvest()

        assertEquals(800000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(900000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }
    /*
        // Tim
        private fun run(type: PlantType, year: Int, exp: Int): Growable {
            val data = SimulationData(100, year)
            val g = Growable(type, 10000)
            val tile = Tile(1, TileType.PLANTATION, Coordinate(0, 0), false, Direction.NONE, false, g)
            data.addTile(tile, 0)
            g.setCropsExpected(exp)
            return g
        }

        @Test
        fun testMowingMissedAppleOne() {
            val g = run(PlantType.APPLE, YearTicks.EARLY_JUNE, 1700000)
            assertEquals(1530000, g.getCropsExpected())
        }
        @Test
        fun testMowingMissedAppleTwo() {
            val g = run(PlantType.APPLE, YearTicks.EARLY_JUNE, 1700000)
            assertEquals(1530000, g.getCropsExpected())
        }
     */
}
