package incident

import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IncidentControllerIntegrationTests {
    private val data = SimulationData(10, 1)

    @Test
    fun testMachineBrokenBasic() {
        // Init
        val tileLoc = Tile(1, TileType.FARMSTEAD, Coordinate(0, 0), false, Direction.NONE, false, null)
        data.addTile(tileLoc, null)
        val m = Machine(1, "Tractor", emptyList(), emptyList(), 1, Coordinate(0, 0))
        val b = BrokenMachine(1, 1, Duration(1, 11), m)
        data.addIncident(b)
        data.setCurrentTick(1)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        // Check
        assert(m.getIsBroken())
    }

    @Test
    fun testBrokenMachineActiveIncident() {
        val tileLoc = Tile(1, TileType.FARMSTEAD, Coordinate(0, 0), false, Direction.NONE, false, null)
        data.addTile(tileLoc, null)
        // Init
        val m = Machine(1, "Tractor", emptyList(), emptyList(), 1, Coordinate(0, 0))
        val b = BrokenMachine(1, 1, Duration(1, 3), m)
        data.addIncident(b)
        data.setCurrentTick(1)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        data.setCurrentTick(3)

        // Do Active Incident
        controller.checkActiveIncidents()

        // Check
        assertFalse(m.getIsBroken())
    }

    @Test
    fun testDoubleBrokenMachineIncident() {
        val tileLoc = Tile(1, TileType.FARMSTEAD, Coordinate(0, 0), false, Direction.NONE, false, null)
        data.addTile(tileLoc, null)
        // Init
        val m = Machine(1, "Tractor", emptyList(), emptyList(), 1, Coordinate(0, 0))
        val b = BrokenMachine(1, 0, Duration(0, 2), m)
        val b2 = BrokenMachine(2, 0, Duration(0, 3), m)
        data.addIncident(b)
        data.addIncident(b2)
        data.setCurrentTick(0)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        data.setCurrentTick(2)

        // Do Active Incident
        controller.checkActiveIncidents()

        // Check
        assertTrue(m.getIsBroken())
        // Next Tick
        data.setCurrentTick(3)
        controller.checkActiveIncidents()
        assertFalse(m.getIsBroken())
    }

    @Test
    fun testCityExpansionRoadBasic() {
        // Init
        val tile = Tile(0, TileType.ROAD, Coordinate(0, 0), true, Direction.NORTHWEST, false, null)
        val c = CityExpansion(1, 1, 0)
        data.addIncident(c)
        data.getMap().addTile(tile, null)
        data.setCurrentTick(1)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        // Check
        assertTrue(
            tile.getTileType() == TileType.VILLAGE && !tile.getAirflow() && tile.getDirection() == Direction.NONE
        )
    }

    @Test
    fun testCityExpansionRoadWithCloud() {
        // Init
        val tile = Tile(0, TileType.ROAD, Coordinate(0, 0), true, Direction.NORTHWEST, false, null)
        val cloud = Cloud(3, 1000, 9, Coordinate(0, 0))
        val c = CityExpansion(1, 1, 0)
        data.addIncident(c)
        data.addCloud(cloud)
        data.getMap().addTile(tile, null)
        data.setCurrentTick(1)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        // Check if Cloud still exists
        assertFalse(data.getClouds().contains(cloud))
    }

    @Test
    fun testCityExpansionOnField() {
        // Init
        val g = Growable(PlantType.POTATO, listOf(PlantType.POTATO), 10000)
        val tile = Tile(0, TileType.FIELD, Coordinate(0, 0), true, Direction.NORTHWEST, false, g)
        val c = CityExpansion(1, 1, 0)
        data.addIncident(c)
        data.getMap().addTile(tile, null)
        data.setCurrentTick(1)
        val controller = IncidentController(data)

        // Do Incident
        controller.checkIncidents()

        // Check
        assertTrue(
            tile.getTileType() == TileType.VILLAGE && !tile.getAirflow() && tile.getDirection() == Direction.NONE &&
                tile.getGrowable() == null
        )
    }

    @Test
    fun testCloudCreationBasic() {
        // Init
        val c = CloudCreation(1, 1, 9, 0, 0, 10000)
        val tile = Tile(0, TileType.ROAD, Coordinate(0, 0), true, Direction.NORTHWEST, false, null)
        data.setCurrentTick(1)
        data.addIncident(c)
        data.getMap().addTile(tile, null)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        // Check
        val clouds = data.getClouds()
        assertEquals(1, clouds.size)
        assertEquals(0, clouds[0].getID())
        // assertEquals(10, clouds[0].getDuration().getEndTick())
        assertEquals(10000, clouds[0].getAmount())
        assertEquals(Coordinate(0, 0), clouds[0].getLocation())
    }

    @Test
    fun testCloudCreationOnCloud() {
        // Init
        val existingCloud = Cloud(0, 2500, 9, Coordinate(0, 0))
        val c = CloudCreation(1, 1, 8, 0, 0, 10000)
        val tile = Tile(0, TileType.ROAD, Coordinate(0, 0), true, Direction.NORTHWEST, false, null)
        data.addCloud(existingCloud)
        data.setCurrentTick(1)
        data.addIncident(c)
        data.getMap().addTile(tile, null)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        // Check
        val clouds = data.getClouds()
        assertEquals(1, clouds.size)
        assertEquals(2, clouds[0].getID())
        // assertEquals(10, clouds[0].getDuration().getEndTick())
        assertEquals(12500, clouds[0].getAmount())
        assertEquals(Coordinate(0, 0), clouds[0].getLocation())
    }

    /*
    @Test
    fun testAnimalAttackOnGrapePlantation() {
        // Init
        val a = AnimalAttack(1, 1, 0, 1)
        data.addIncident(a)
        data.setCurrentTick(1)
        // Init Tiles in Radius
        val locTile = Tile(0, TileType.ROAD, Coordinate(1, 1), true, Direction.NORTHWEST, false, null)
        data.getMap().addTile(locTile, null)
        val forest = Tile(1, TileType.FOREST, Coordinate(0, 0), true, Direction.EAST, false, null)
        data.getMap().addTile(forest, null)
        val g = Growable(PlantType.GRAPE, 10000)
        g.setCropsExpected(1000)
        val plantation = Tile(2, TileType.PLANTATION, Coordinate(2, 0), true, Direction.EAST, false, g)
        data.getMap().addTile(plantation, null)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        // Check
        assertEquals((1000 * 0.5).toInt(), g.getCropsExpected())
    }

    @Test
    fun testAnimalAttackOnField() {
        // Init
        val a = AnimalAttack(1, 1, 0, 1)
        data.addIncident(a)
        data.setCurrentTick(1)
        // Init Tiles in Radius
        val locTile = Tile(0, TileType.ROAD, Coordinate(1, 1), true, Direction.NORTHWEST, false, null)
        data.getMap().addTile(locTile, null)
        val forest = Tile(1, TileType.FOREST, Coordinate(0, 0), true, Direction.EAST, false, null)
        data.getMap().addTile(forest, null)
        val g = Growable(listOf(PlantType.POTATO), 10000)
        g.setCurrentPlant(PlantType.POTATO)
        g.setCropsExpected(1000)
        val field = Tile(2, TileType.FIELD, Coordinate(2, 0), true, Direction.EAST, false, g)
        data.getMap().addTile(field, null)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        // Check
        assertEquals((1000 * 0.5).toInt(), g.getCropsExpected())
    }

    @Test
    fun testAnimalAttackOnApplePlantation() {
        // Init
        val a = AnimalAttack(1, 1, 0, 1)
        data.addIncident(a)
        data.setCurrentTick(1)
        // Init Tiles in Radius
        val locTile = Tile(0, TileType.ROAD, Coordinate(1, 1), true, Direction.NORTHWEST, false, null)
        data.getMap().addTile(locTile, null)
        val forest = Tile(1, TileType.FOREST, Coordinate(0, 0), true, Direction.EAST, false, null)
        data.getMap().addTile(forest, null)
        val g = Growable(PlantType.APPLE, 10000)
        g.setCropsExpected(1000)
        val plantation = Tile(2, TileType.PLANTATION, Coordinate(2, 0), true, Direction.EAST, false, g)
        data.getMap().addTile(plantation, null)
        // Do
        val controller = IncidentController(data)
        controller.checkIncidents()
        // Check
        assertEquals((1000 * 0.9).toInt(), g.getCropsExpected())
    }
     */
}
