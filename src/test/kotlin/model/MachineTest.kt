package model

import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.util.Coordinate
import kotlin.test.Test
import kotlin.test.assertFalse

class MachineTest {
    val machine = Machine(
        0,
        "kleiner roter Traktor",
        listOf(Action.CUTTING),
        listOf(PlantType.APPLE),
        7,
        Coordinate(2, 2)
    )

    @Test
    fun `canIStillWork possible`() {
        machine.worked()
        assert(machine.canIStillWork())
    }

    @Test
    fun `canIStillWork not possible, because not enough days left`() {
        machine.worked()
        machine.worked()
        assertFalse(machine.canIStillWork())
    }

    @Test
    fun `canIStillWork not possible, because broken`() {
        machine.setIsBroken(true)
        assertFalse(machine.canIStillWork())
    }

    @Test
    fun `canIWork not possible, because already worked`() {
        machine.worked()
        assertFalse(machine.canIWork())
    }

    @Test
    fun `canIWork not possible, because broken`() {
        machine.setIsBroken(true)
        assertFalse(machine.canIWork())
    }
}
