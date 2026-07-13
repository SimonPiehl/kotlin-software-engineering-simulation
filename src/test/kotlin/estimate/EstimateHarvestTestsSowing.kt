package estimate
/*
import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import kotlin.test.Test
import kotlin.test.assertEquals

class EstimateHarvestTestsSowing {

    val data = SimulationData(25, 6)
    val ehc = EstimateHarvestController(data)
    val map = Map()
    val coord = Coordinate(0, 0)
    val grow = Growable(
        PlantType.POTATO,
        listOf(PlantType.POTATO, PlantType.PUMPKIN, PlantType.WHEAT, PlantType.OAT, PlantType.APPLE),
        1000
    )
    val tile1 = Tile(
        1,
        TileType.FIELD,
        coord,
        airflow = true,
        Direction.SOUTH,
        false,
        grow
    )

    @Test
    fun sowingBasicInTime() {
        data.setCurrentTick(7)
        data.setYearTick(7)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(7)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            1000000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on first right tick"
        )
    }

    @Test
    fun sowingBasicInTimeOtherTick() {
        data.setCurrentTick(20)
        data.setYearTick(7)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(20)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            1000000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on first right tick"
        )
    }

    @Test
    fun sowingBasicInTimeDuration() {
        data.setCurrentTick(8)
        data.setYearTick(8)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(8)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            1000000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on middle right tick"
        )
    }

    @Test
    fun sowingBasicInTimeLastDuration() {
        data.setCurrentTick(10)
        data.setYearTick(10)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(10)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            1000000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on last right tick"
        )
    }

    @Test
    fun sowingBasicNotSowed() {
        data.setCurrentTick(10)
        data.setYearTick(10)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(0)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            0,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on wrong tick"
        )
    }

    @Test
    fun sowingBasicLongerOneTick() {
        data.setCurrentTick(11)
        data.setYearTick(11)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(11)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            800000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on first right tick after"
        )
    }

    @Test
    fun sowingBasicLongerTwoTick() {
        data.setCurrentTick(12)
        data.setYearTick(12)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(12)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            600000,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on second right tick after"
        )
    }

    @Test
    fun sowingBasicLongerThanTime() {
        data.setCurrentTick(13)
        data.setYearTick(13)
        tile1.getGrowable()?.setCropsExpected(0)
        tile1.getGrowable()?.setSunlightExposure(0)
        tile1.getGrowable()?.setMoistureExposure(1000)
        tile1.getGrowable()?.setWasSowedAtTick(13)
        map.addTile(tile1, 1)

        ehc.calculateSowingEstimation(tile1)
        assertEquals(
            0,
            tile1.getGrowable()?.getCropsExpected(),
            "sowing wrong " +
                "estimation on first wrong tick"
        )
    }
}
 */
