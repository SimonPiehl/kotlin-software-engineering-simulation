package incidenttests

import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Action
import de.unisaarland.cs.se.selab.model.AnimalAttack
import de.unisaarland.cs.se.selab.model.BeeHappy
import de.unisaarland.cs.se.selab.model.BrokenMachine
import de.unisaarland.cs.se.selab.model.CityExpansion
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Drought
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Machine
import de.unisaarland.cs.se.selab.model.PlantType
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.Direction
import de.unisaarland.cs.se.selab.util.Duration
import de.unisaarland.cs.se.selab.util.GeneralConstants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IncidentControllerTest {

    private fun createSimulation(): Pair<SimulationData, IncidentController> {
        val data = SimulationData(maxTick = 10, yearTick = 1)
        val controller = IncidentController(data)
        return Pair(data, controller)
    }

    @Test
    fun testCloudCreationCreatesClouds() {
        val (data, controller) = createSimulation()
        val tile = Tile(
            1,
            TileType.FIELD,
            Coordinate(0, 0),
            airflow = true,
            direction = Direction.NORTH,
            false,
            growable = null
        )
        data.addTile(tile, farmId = null)

        val incident = CloudCreation(
            id = 5,
            tick = 0,
            duration = 1,
            location = 1,
            radius = 0,
            amount = 1000
        )
        data.addIncident(incident)

        data.setCurrentTick(0)
        controller.checkIncidents()

        assertEquals(1, data.getClouds().size)
        assertEquals(Coordinate(0, 0), data.getClouds()[0].getLocation())
    }

    @Test
    fun testDroughtSetsMoistureToZeroAndMarksTile() {
        val (data, controller) = createSimulation()
        val growable: Growable = Growable(PlantType.WHEAT, maxMoisture = 2000)

        val tile = Tile(
            2,
            TileType.PLANTATION,
            Coordinate(2, 0),
            airflow = false,
            direction = Direction.NORTH,
            false,
            growable = growable
        )
        data.addTile(tile, farmId = 1)

        val drought = Drought(
            id = 7,
            tick = 0,
            location = 2,
            radius = 0
        )
        data.addIncident(drought)

        data.setCurrentTick(0)
        controller.checkIncidents()

        assertEquals(0, tile.getGrowable()?.getMoistureExposure())
        assertTrue(data.getTilesWhereDrought().contains(tile))
    }

    @Test
    fun testBrokenMachineSetsMachineBroken() {
        val (data, controller) = createSimulation()
        val machine = Machine(1, "Harvester", listOf(Action.HARVESTING), listOf(PlantType.OAT), 5, Coordinate(0, 0))
        val farm = Farm(1, "farm", listOf<Machine>(machine), mutableListOf())
        data.addFarm(farm)

        val tile = Tile(3, TileType.FARMSTEAD, Coordinate(0, 0), true, Direction.NORTH, true, null)
        data.addTile(tile, farm.getID())

        val incident = BrokenMachine(
            id = 10,
            tick = 0,
            machine = machine,
            duration = Duration(0, 1)
        )
        data.addIncident(incident)

        data.setCurrentTick(0)
        controller.checkIncidents()

        assertTrue(machine.getIsBroken())
    }

    @Test
    fun testCloudCreationWithRadiusSkipsVillageTiles() {
        val (data, controller) = createSimulation()

        // center + Nachbarn (N, E) existieren
        val center = Tile(10, TileType.FIELD, Coordinate(0, 0), true, Direction.EAST, false, null)
        val nTile = Tile(11, TileType.MEADOW, Coordinate(0, -2), false, Direction.EAST, false, null)
        val eTile = Tile(
            12,
            TileType.VILLAGE,
            Coordinate(2, 0),
            false,
            Direction.EAST,
            false,
            null
        ) // sollte übersprungen werden
        data.addTile(center, null)
        data.addTile(nTile, null)
        data.addTile(eTile, null)

        val incident = CloudCreation(
            id = 1,
            tick = 0,
            duration = 2,
            location = center.getID(),
            radius = 1,
            amount = 1500
        )
        data.addIncident(incident)

        data.setCurrentTick(0)
        controller.checkIncidents()

        // Es sollten Clouds auf center und N liegen, aber nicht auf Village
        val clouds = data.getClouds()
        val coords = clouds.map { it.getLocation() }.toSet()
        assertTrue(Coordinate(0, 0) in coords)
        assertTrue(Coordinate(0, -2) in coords)
        assertFalse(Coordinate(2, 0) in coords)
    }

    @Test
    fun testCloudCreationMergesWithExistingCloud() {
        val (data, controller) = createSimulation()

        val tile = Tile(20, TileType.FIELD, Coordinate(4, 0), true, Direction.EAST, false, null)
        data.addTile(tile, null)

        // Bereits existierende Wolke am selben Ort
        val existing = Cloud(
            id = 7,
            amount = 2000,
            duration = 4,
            location = tile.getCoordinate()
        )
        data.addCloud(existing)

        // Incident erstellt eine weitere Cloud auf demselben Tile → Merge
        val incident = CloudCreation(
            id = 2,
            tick = 0,
            duration = 1,
            location = tile.getID(),
            radius = 0,
            amount = 4000
        )
        data.addIncident(incident)

        data.setCurrentTick(0)
        controller.checkIncidents()

        val clouds = data.getClouds()
        assertEquals(1, clouds.size) // merged
        assertEquals(6000, clouds[0].getAmount()) // 2000 + 4000

        // Dauer = min(alt, neu), Start = currentTick (laut Implementierung)
        // Hier prüfen wir nur, dass Endtick dem Minimum entspricht
        // val end = clouds[0].getDuration().getEndTick()
        // assertEquals(kotlin.math.min(5, 2), end)
    }

    @Test
    fun testBrokenMachineBecomesUnbrokenAtEndTick() {
        val (data, controller) = createSimulation()

        val machine = Machine(
            300,
            "M",
            listOf(Action.HARVESTING),
            listOf(PlantType.OAT),
            5,
            Coordinate(0, 0)
        )

        val farm = Farm(30, "farmS", listOf<Machine>(machine), mutableListOf())
        data.addFarm(farm)

        val shed = Tile(31, TileType.FARMSTEAD, Coordinate(0, 0), false, Direction.EAST, true, null)
        data.addTile(shed, farm.getID())

        // BrokenMachine: Ende bei Tick 2
        val bm = BrokenMachine(id = 3, tick = 0, machine = machine, duration = Duration(0, 2))
        data.addIncident(bm)

        data.setCurrentTick(0)
        controller.checkIncidents()
        assertTrue(machine.getIsBroken())

        // jetzt Endtick
        data.setCurrentTick(2)
        controller.checkActiveIncidents()
        assertFalse(machine.getIsBroken())
    }

    @Test
    fun testCityExpansionTransformsToVillageAndKillsCloud() {
        val (data, controller) = createSimulation()

        val road = Tile(40, TileType.ROAD, Coordinate(6, 0), false, Direction.EAST, false, null)
        val villageNeighbor = Tile(41, TileType.VILLAGE, Coordinate(8, 0), false, Direction.EAST, false, null)
        data.addTile(road, null)
        data.addTile(villageNeighbor, null)

        // Wolke auf dem Road-Tile (soll danach verschwinden)
        val cloud = Cloud(5, 3000, 4, road.getCoordinate())
        data.addCloud(cloud)

        val city = CityExpansion(id = 4, tick = 0, location = road.getID())
        data.addIncident(city)

        data.setCurrentTick(0)
        controller.checkIncidents()

        // Tile sollte jetzt Village sein
        val changed = data.getMap().getTileByID(40)!!
        assertEquals(TileType.VILLAGE, changed.getTileType())

        // Cloud sollte gelöscht worden sein
        assertTrue(data.getClouds().isEmpty())
    }

    @Test
    fun testAnimalAttackAddsExpectedMultipliers() {
        val (data, controller) = createSimulation()

        // Forest + angrenzend ein Field und eine Plantation (GRAPE & APPLE)
        val forest = Tile(50, TileType.FOREST, Coordinate(0, 0), false, Direction.EAST, false, null)
        val fieldGrow = Growable(PlantType.WHEAT, listOf<PlantType>(PlantType.WHEAT), 1000)
        val grapeGrow = Growable(PlantType.GRAPE, listOf<PlantType>(PlantType.GRAPE), 1000) // 50% Malus
        val appleGrow = Growable(
            PlantType.APPLE,
            listOf<PlantType>(PlantType.APPLE),
            1000
        ) // 90% Malus + evtl. Mowing-Reset
        fieldGrow.setCropsExpected(1000)
        grapeGrow.setCropsExpected(1000)
        appleGrow.setCropsExpected(1000)
        val field = Tile(51, TileType.FIELD, Coordinate(2, 0), false, Direction.EAST, false, fieldGrow)
        val plantationGrape = Tile(52, TileType.PLANTATION, Coordinate(0, 2), false, Direction.EAST, false, grapeGrow)
        val plantationApple = Tile(53, TileType.PLANTATION, Coordinate(-2, 0), false, Direction.EAST, false, appleGrow)

        data.addTile(forest, null)
        data.addTile(field, 1)
        data.addTile(plantationGrape, 1)
        data.addTile(plantationApple, 1)

        val aa = AnimalAttack(id = 6, tick = 0, location = forest.getID(), radius = 0)
        data.addIncident(aa)

        data.setCurrentTick(0)
        controller.checkIncidents()

        val incMap = data.getTilesHarvestChanged()

        // FIELD: 50% (0.5)
        val fMul = incMap[field]?.firstOrNull()
        assertEquals(GeneralConstants.ANIMAL_ATTACK_MUL_50, fMul)

        // GRAPE: 50% (0.5)
        val gMul = incMap[plantationGrape]?.firstOrNull()
        assertEquals(GeneralConstants.ANIMAL_ATTACK_MUL_50, gMul)

        // APPLE: 90% (0.9)
        val aMul = incMap[plantationApple]?.firstOrNull()
        assertEquals(GeneralConstants.ANIMAL_ATTACK_MUL_90, aMul)
    }

    @Test
    fun testBeeHappyAddsEffectFactorOnlyToEligibleTiles() {
        val (data, controller) = createSimulation()

        // Meadow in der Mitte, Field im Radius 2
        val meadow = Tile(60, TileType.MEADOW, Coordinate(0, 0), false, Direction.EAST, false, null)
        val grow = Growable(PlantType.APPLE, listOf<PlantType>(PlantType.APPLE), 1000)
        grow.setCropsExpected(1000)
        val field = Tile(61, TileType.FIELD, Coordinate(2, 0), false, Direction.EAST, false, grow)

        data.addTile(meadow, null)
        data.addTile(field, 1)

        val bh = BeeHappy(id = 7, tick = 0, location = field.getID(), radius = 3, effect = 20) // +20%
        data.addIncident(bh)

        data.setCurrentTick(0)
        data.setYearTick(8)
        controller.checkIncidents()

        // IncidentController trägt Multiplikator (1 + effect/100) in tilesHarvestChanged ein
        val incMap = data.getTilesHarvestChanged()
        val muls = incMap[field].orEmpty()
        val res: Double = muls.first()
        assertEquals(1.2, res, "not calculated")
    }
}
