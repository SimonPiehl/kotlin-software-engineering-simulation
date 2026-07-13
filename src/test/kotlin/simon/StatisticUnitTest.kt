package simon

import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Statistic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Statistic test
 *
 * @constructor Statistic test
 */
class StatisticUnitTest {

    private lateinit var statistic: Statistic

    @BeforeEach
    fun setUp() {
        statistic = Statistic()
    }

    @Test
    fun setterAndGetterTest() {
        statistic.addPlantAmount(PlantType.POTATO, 1)
        statistic.addPlantAmount(PlantType.WHEAT, 2)
        statistic.addPlantAmount(PlantType.OAT, 3)
        statistic.addPlantAmount(PlantType.PUMPKIN, 4)
        statistic.addPlantAmount(PlantType.APPLE, 5)
        statistic.addPlantAmount(PlantType.ALMOND, 6)
        statistic.addPlantAmount(PlantType.CHERRY, 7)
        statistic.addPlantAmount(PlantType.GRAPE, 8)

        assertEquals(1, statistic.getPotatoAmount())
        assertEquals(2, statistic.getWheatAmount())
        assertEquals(3, statistic.getOatAmount())
        assertEquals(4, statistic.getPumpkinAmount())
        assertEquals(5, statistic.getAppleAmount())
        assertEquals(6, statistic.getAlmondAmount())
        assertEquals(7, statistic.getCherryAmount())
        assertEquals(8, statistic.getGrapeAmount())
    }

    @Test
    fun combinedHarvestTest() {
        statistic.addPlantAmount(PlantType.POTATO, 1)
        statistic.addPlantAmount(PlantType.WHEAT, 2)
        statistic.addPlantAmount(PlantType.OAT, 3)
        statistic.addPlantAmount(PlantType.PUMPKIN, 4)
        statistic.addPlantAmount(PlantType.APPLE, 5)
        statistic.addPlantAmount(PlantType.ALMOND, 6)
        statistic.addPlantAmount(PlantType.CHERRY, 7)
        statistic.addPlantAmount(PlantType.GRAPE, 8)

        val expectedTotal = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8
        assertEquals(expectedTotal, statistic.computeCombinedHarvest())
    }
}
