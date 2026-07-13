package model

import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.SowingPlan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SowingPlanTest {

    private lateinit var sowingPlan: SowingPlan

    @BeforeEach
    fun setUp() {
        sowingPlan = SowingPlan(
            id = 1,
            startsAtTick = 5,
            plantToSow = PlantType.POTATO,
            locations = listOf(10, 20, 30)
        )
    }

    @Test
    fun `test constructor initializes correctly`() {
        assertEquals(1, sowingPlan.getID())
        assertEquals(5, sowingPlan.getStartTick())
        assertEquals(PlantType.POTATO, sowingPlan.getPlantToSow())
        assertEquals(listOf(10, 20, 30), sowingPlan.getLocations())
        assertEquals(-1, sowingPlan.getwasUsedInTick())
    }

    @Test
    fun `test setWasUsedInTick and getwasUsedInTick`() {
        assertEquals(-1, sowingPlan.getwasUsedInTick())

        sowingPlan.setWasUsedInTick(10)
        assertEquals(10, sowingPlan.getwasUsedInTick())

        sowingPlan.setWasUsedInTick(15)
        assertEquals(15, sowingPlan.getwasUsedInTick())

        sowingPlan.setWasUsedInTick(0)
        assertEquals(0, sowingPlan.getwasUsedInTick())

        sowingPlan.setWasUsedInTick(-5)
        assertEquals(-5, sowingPlan.getwasUsedInTick())
    }

    @Test
    fun `test getStartTick returns correct value`() {
        assertEquals(5, sowingPlan.getStartTick())

        val sowingPlan2 = SowingPlan(2, 0, PlantType.WHEAT, emptyList())
        assertEquals(0, sowingPlan2.getStartTick())

        val sowingPlan3 = SowingPlan(3, 100, PlantType.APPLE, listOf(1))
        assertEquals(100, sowingPlan3.getStartTick())
    }

    @Test
    fun `test getPlantToSow returns correct plant type`() {
        assertEquals(PlantType.POTATO, sowingPlan.getPlantToSow())

        val sowingPlan2 = SowingPlan(2, 5, PlantType.WHEAT, emptyList())
        assertEquals(PlantType.WHEAT, sowingPlan2.getPlantToSow())

        val sowingPlan3 = SowingPlan(3, 5, PlantType.APPLE, emptyList())
        assertEquals(PlantType.APPLE, sowingPlan3.getPlantToSow())
    }

    @Test
    fun `test getLocations returns correct locations`() {
        assertEquals(listOf(10, 20, 30), sowingPlan.getLocations())

        val sowingPlan2 = SowingPlan(2, 5, PlantType.POTATO, emptyList())
        assertEquals(emptyList<Int>(), sowingPlan2.getLocations())

        val sowingPlan3 = SowingPlan(3, 5, PlantType.POTATO, listOf(42))
        assertEquals(listOf(42), sowingPlan3.getLocations())

        val sowingPlan4 = SowingPlan(4, 5, PlantType.POTATO, listOf(1, 2, 3, 4, 5))
        assertEquals(listOf(1, 2, 3, 4, 5), sowingPlan4.getLocations())
    }

    @Test
    fun `test getID returns correct id`() {
        assertEquals(1, sowingPlan.getID())

        val sowingPlan2 = SowingPlan(0, 5, PlantType.POTATO, emptyList())
        assertEquals(0, sowingPlan2.getID())

        val sowingPlan3 = SowingPlan(999, 5, PlantType.POTATO, emptyList())
        assertEquals(999, sowingPlan3.getID())

        val sowingPlan4 = SowingPlan(-1, 5, PlantType.POTATO, emptyList())
        assertEquals(-1, sowingPlan4.getID())
    }

    @Test
    fun `test wasUsedInTick default value is -1`() {
        val newSowingPlan = SowingPlan(
            id = 2,
            startsAtTick = 3,
            plantToSow = PlantType.WHEAT,
            locations = listOf(1, 2)
        )
        assertEquals(-1, newSowingPlan.getwasUsedInTick())
    }

    @Test
    fun `test multiple sowing plans are independent`() {
        val plan1 = SowingPlan(1, 5, PlantType.POTATO, listOf(10, 20))
        val plan2 = SowingPlan(2, 8, PlantType.WHEAT, listOf(30, 40))

        plan1.setWasUsedInTick(6)
        plan2.setWasUsedInTick(9)

        assertEquals(6, plan1.getwasUsedInTick())
        assertEquals(9, plan2.getwasUsedInTick())
        assertNotEquals(plan1.getwasUsedInTick(), plan2.getwasUsedInTick())

        assertEquals(1, plan1.getID())
        assertEquals(2, plan2.getID())
        assertEquals(PlantType.POTATO, plan1.getPlantToSow())
        assertEquals(PlantType.WHEAT, plan2.getPlantToSow())
    }
}
