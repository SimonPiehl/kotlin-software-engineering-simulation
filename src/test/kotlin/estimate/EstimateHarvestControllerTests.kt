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

class EstimateHarvestControllerTests {

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

/*
    @Test
    fun testpotatoapplemaisowed() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.POTATO,
            7,
            17,
            600,
            500,
            50,
            50
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile2?.getGrowable()?.setCropsExpected(0)
        tile1?.getGrowable()?.setCropsExpected(1700000)
        tile2?.getGrowable()?.setWasSowedAtTick(17)
        tile1?.getGrowable()?.setWasCutAtTick(12)
        esc.calculateEstimateHarvest()

        assertEquals(1000000, tile2?.getGrowable()?.getCropsExpected(), "sowed initialized")
        assertEquals(1700000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }
*/
    @Test
    fun testpotatoapplemainotsowed() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.POTATO,
            7,
            17,
            600,
            200,
            50,
            50
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile2?.getGrowable()?.setCropsExpected(50)
        tile1?.getGrowable()?.setCropsExpected(1700000)
        tile2?.getGrowable()?.setWasSowedAtTick(0)
        tile1?.getGrowable()?.setWasCutAtTick(12)
        esc.calculateEstimateHarvest()

        assertEquals(0, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(1700000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }

    @Test
    fun testAppleWheatnotcuttednotsowed() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.WHEAT,
            21,
            24,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile1?.getGrowable()?.setCropsExpected(1700000)
        tile2?.getGrowable()?.setCropsExpected(0)
        tile2?.getGrowable()?.setWasSowedAtTick(0)
        tile1?.getGrowable()?.setWasCutAtTick(0)
        esc.calculateEstimateHarvest()

        assertEquals(0, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(1700000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }

    @Test
    fun testAppleWheatCuttednotsowed() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.WHEAT,
            4,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile1?.getGrowable()?.setCropsExpected(1700000)
        tile2?.getGrowable()?.setCropsExpected(0)
        tile2?.getGrowable()?.setWasSowedAtTick(0)
        tile1?.getGrowable()?.setWasCutAtTick(21)
        esc.calculateEstimateHarvest()

        assertEquals(0, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(1700000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }

    @Test
    fun testAppleWheatNotCuttedsowed() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.WHEAT,
            4,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile1?.getGrowable()?.setCropsExpected(1700000)
        tile2?.getGrowable()?.setCropsExpected(1500000)
        tile2?.getGrowable()?.setWasSowedAtTick(20)
        tile1?.getGrowable()?.setWasCutAtTick(-1)
        esc.calculateEstimateHarvest()

        assertEquals(1500000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(850000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }

    @Test
    fun testAppleWheatNotCuttedsowedFurther() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.WHEAT,
            5,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile1?.getGrowable()?.setCropsExpected(850000)
        tile2?.getGrowable()?.setCropsExpected(1500000)
        tile2?.getGrowable()?.setWasSowedAtTick(20)
        tile1?.getGrowable()?.setWasCutAtTick(-1)
        esc.calculateEstimateHarvest()

        assertEquals(1500000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(850000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }

    @Test
    fun testAppleWheatCuttedrecentlysowedFurther() {
        val data = initialization(
            PlantType.APPLE,
            PlantType.WHEAT,
            4,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // apple
        val tile2 = data.getMap().getTileByID(2) // Potato
        tile1?.getGrowable()?.setCropsExpected(170000)
        tile2?.getGrowable()?.setCropsExpected(1500000)
        tile2?.getGrowable()?.setWasSowedAtTick(20)
        tile1?.getGrowable()?.setWasCutAtTick(26)
        esc.calculateEstimateHarvest()

        assertEquals(1500000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
        assertEquals(170000, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
    }
/*
    @Test
    fun testAlmondWheatNothingandlatesowed() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.WHEAT,
            20,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(170000)
        tile2?.getGrowable()?.setCropsExpected(0)

        tile1?.getGrowable()?.setWasHarvestedAtTick(26)
        tile2?.getGrowable()?.setWasSowedAtTick(26)
        esc.calculateEstimateHarvest()

        assertEquals(0, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1500000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }
    @Test
    fun testAlmondWheatHarvestlateedandsowed() {
        val data = initialization(
            PlantType.ALMOND,
            PlantType.WHEAT,
            21,
            26,
            600,
            500,
            50,
            100
        )
        val esc = EstimateHarvestController(data)
        esc.toString()
        val tile1 = data.getMap().getTileByID(1) // Almond
        val tile2 = data.getMap().getTileByID(2) // Wheat
        tile1?.getGrowable()?.setCropsExpected(170000)
        tile2?.getGrowable()?.setCropsExpected(0)

        tile1?.getGrowable()?.setWasHarvestedAtTick(26)
        tile2?.getGrowable()?.setWasSowedAtTick(26)
        esc.calculateEstimateHarvest()

        assertEquals(0, tile1?.getGrowable()?.getCropsExpected(), "Apples Unchanged")
        assertEquals(1200000, tile2?.getGrowable()?.getCropsExpected(), "sowed not initialized")
    }
    */
}
