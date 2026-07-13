package model

import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.Incident
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnimalAttackTest {

    private lateinit var animalAttack: AnimalAttack

    @BeforeEach
    fun setUp() {
        animalAttack = AnimalAttack(
            id = 1,
            tick = 10,
            location = 5,
            radius = 3
        )
    }

    @Test
    fun `test constructor initializes correctly`() {
        assertEquals(1, animalAttack.getID())
        assertEquals(10, animalAttack.getTick())
        assertEquals(5, animalAttack.getLocation())
        assertEquals(3, animalAttack.getRadius())
    }

    @Test
    fun `test getLocation returns correct value`() {
        assertEquals(5, animalAttack.getLocation())

        val animalAttack2 = AnimalAttack(2, 5, 0, 2)
        assertEquals(0, animalAttack2.getLocation())

        val animalAttack3 = AnimalAttack(3, 5, 999, 2)
        assertEquals(999, animalAttack3.getLocation())

        val animalAttack4 = AnimalAttack(4, 5, -1, 2)
        assertEquals(-1, animalAttack4.getLocation())
    }

    @Test
    fun `test getRadius returns correct value`() {
        assertEquals(3, animalAttack.getRadius())

        val animalAttack2 = AnimalAttack(2, 5, 1, 0)
        assertEquals(0, animalAttack2.getRadius())

        val animalAttack3 = AnimalAttack(3, 5, 1, 10)
        assertEquals(10, animalAttack3.getRadius())

        val animalAttack4 = AnimalAttack(4, 5, 1, 1)
        assertEquals(1, animalAttack4.getRadius())
    }

    @Test
    fun `test toString returns ANIMAL_ATTACK`() {
        assertEquals("ANIMAL_ATTACK", animalAttack.toString())

        val animalAttack2 = AnimalAttack(2, 5, 1, 2)
        assertEquals("ANIMAL_ATTACK", animalAttack2.toString())
    }

    @Test
    fun `test AnimalAttack inherits from Incident`() {
        val incident: Incident = animalAttack
        assertEquals(1, incident.getID())
        assertEquals(10, incident.getTick())
    }

    @Test
    fun `test multiple AnimalAttack instances are independent`() {
        val attack1 = AnimalAttack(1, 5, 10, 2)
        val attack2 = AnimalAttack(2, 8, 20, 4)

        assertEquals(1, attack1.getID())
        assertEquals(5, attack1.getTick())
        assertEquals(10, attack1.getLocation())
        assertEquals(2, attack1.getRadius())

        assertEquals(2, attack2.getID())
        assertEquals(8, attack2.getTick())
        assertEquals(20, attack2.getLocation())
        assertEquals(4, attack2.getRadius())

        assertNotEquals(attack1.getID(), attack2.getID())
        assertNotEquals(attack1.getLocation(), attack2.getLocation())
        assertNotEquals(attack1.getRadius(), attack2.getRadius())
    }

    @Test
    fun `test AnimalAttack with zero radius`() {
        val zeroRadiusAttack = AnimalAttack(5, 15, 7, 0)

        assertEquals(5, zeroRadiusAttack.getID())
        assertEquals(15, zeroRadiusAttack.getTick())
        assertEquals(7, zeroRadiusAttack.getLocation())
        assertEquals(0, zeroRadiusAttack.getRadius())
        assertEquals("ANIMAL_ATTACK", zeroRadiusAttack.toString())
    }

    @Test
    fun `test AnimalAttack with maximum values`() {
        val maxValuesAttack = AnimalAttack(
            id = Int.MAX_VALUE,
            tick = Int.MAX_VALUE,
            location = Int.MAX_VALUE,
            radius = Int.MAX_VALUE
        )

        assertEquals(Int.MAX_VALUE, maxValuesAttack.getID())
        assertEquals(Int.MAX_VALUE, maxValuesAttack.getTick())
        assertEquals(Int.MAX_VALUE, maxValuesAttack.getLocation())
        assertEquals(Int.MAX_VALUE, maxValuesAttack.getRadius())
    }

    @Test
    fun `test AnimalAttack with minimum values`() {
        val minValuesAttack = AnimalAttack(
            id = Int.MIN_VALUE,
            tick = Int.MIN_VALUE,
            location = Int.MIN_VALUE,
            radius = Int.MIN_VALUE
        )

        assertEquals(Int.MIN_VALUE, minValuesAttack.getID())
        assertEquals(Int.MIN_VALUE, minValuesAttack.getTick())
        assertEquals(Int.MIN_VALUE, minValuesAttack.getLocation())
        assertEquals(Int.MIN_VALUE, minValuesAttack.getRadius())
    }
}
