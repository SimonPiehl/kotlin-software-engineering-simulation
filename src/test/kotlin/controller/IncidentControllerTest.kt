package controller

import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Incident
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Duration
import de.unisaarland.cs.se.selab.util.GeneralConstants
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class IncidentControllerTest {

    private lateinit var data: SimulationData
    private lateinit var map: Map
    private lateinit var tile: Tile
    private lateinit var growable: Growable
    private lateinit var machine: Machine
    private lateinit var controller: IncidentController

    @BeforeEach
    fun setup() {
        data = mock()
        map = mock()
        tile = mock()
        growable = mock()
        machine = mock()

        whenever(data.getMap()).thenReturn(map)
        whenever(tile.getCoordinate()).thenReturn(Coordinate(0, 0))
        whenever(tile.getID()).thenReturn(1)
        whenever(map.getTileByID(anyInt())).thenReturn(tile)

        controller = IncidentController(data)
    }

    @Test
    fun `cloudCreation should add new cloud`() {
        val incident = CloudCreation(1, 5, 9, 1, 1, 5000)

        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf())

        controller.checkIncidentsFor(incident)

        verify(data).addCloud(any())
    }

    @Test
    fun `animalAttack should reduce harvest on FIELD`() {
        val incident = AnimalAttack(1, 0, 1, 1)

        whenever(tile.getTileType()).thenReturn(TileType.FOREST)

        val field = mock(Tile::class.java)
        whenever(field.getTileType()).thenReturn(TileType.FIELD)
        whenever(field.getGrowable()).thenReturn(growable)
        whenever(growable.getCropsExpected()).thenReturn(10)
        whenever(field.getID()).thenReturn(2)

        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf(field))

        controller.checkIncidentsFor(incident)

        verify(data).addTilesHarvestChanged(eq(field), eq(GeneralConstants.ANIMAL_ATTACK_MUL_50))
    }

    @Test
    fun `drought should set moisture exposure to 0`() {
        val incident = Drought(1, 0, 1, 1)

        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(growable.getCropsExpected()).thenReturn(5)

        whenever(map.getTilesByCoordinates(any())).thenReturn(mutableListOf())

        controller.checkIncidentsFor(incident)

        verify(growable).setMoistureExposure(0)
        verify(data).addTilesHarvestChanged(eq(tile), eq(GeneralConstants.DROUGHT_EFFECT_ZERO))
    }

    @Test
    fun `brokenMachine should mark machine as broken`() {
        val duration = Duration(0, 5)
        val brokenMachine = BrokenMachine(1, 1, duration, machine)

        whenever(machine.getIsBroken()).thenReturn(false)
        whenever(machine.getCurrentLocation()).thenReturn(Coordinate(0, 0))
        whenever(map.getTileByCoordinate(any())).thenReturn(tile)

        controller.checkIncidentsFor(brokenMachine)

        verify(machine).setIsBroken(true)
    }

    private fun IncidentController.checkIncidentsFor(incident: Incident) {
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf(0 to mutableListOf(incident))
        )
        whenever(data.getCurrentTick()).thenReturn(0)
        this.checkIncidents()
    }
}
