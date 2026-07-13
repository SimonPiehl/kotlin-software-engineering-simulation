package simon

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.PlantType
import kotlin.test.Test
import kotlin.test.assertEquals

class GrowableUnitTest {

    @Test
    fun `check wasAtTick for Harvesting`() {
        val growable = Growable(PlantType.WHEAT, 1000)

        growable.wasAtTick(Action.HARVESTING, 5)
        assertEquals(growable.getWasHarvestedAtTick(), 5)
        assertEquals(growable.getLiesFallowSinceTick(), 5)
        assertEquals(growable.getLastTickWorkedOn(), 5)
    }

    @Test
    fun `check wasAtTick for SOWING`() {
        val growable = Growable(PlantType.WHEAT, 1000)

        growable.wasAtTick(Action.SOWING, 5)
        assertEquals(growable.getWasSowedAtTick(), 5)
        assertEquals(growable.getLastTickWorkedOn(), 5)
    }

    @Test
    fun `check wasAtTick for WEEDING`() {
        val growable = Growable(PlantType.WHEAT, 1000)

        growable.wasAtTick(Action.WEEDING, 5)
        assertEquals(growable.getWasWeededAtTick(), mutableListOf(5))
        assertEquals(growable.getLastTickWorkedOn(), 5)
    }

    @Test
    fun `check wasAtTick for CUTTING`() {
        val growable = Growable(PlantType.CHERRY, 1000)

        growable.wasAtTick(Action.CUTTING, 5)
        assertEquals(growable.getWasCutAtTick(), 5)
        assertEquals(growable.getLastTickWorkedOn(), 5)
    }

    @Test
    fun `check wasAtTick for MOWED`() {
        val growable = Growable(PlantType.CHERRY, 1000)

        growable.wasAtTick(Action.MOWING, 5)
        assertEquals(growable.getWasMowedAtTick(), 5)
        assertEquals(growable.getLastTickWorkedOn(), 5)
    }
}
