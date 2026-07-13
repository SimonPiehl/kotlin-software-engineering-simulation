package niklas

import de.unisaarland.cs.se.selab.controller.CloudController
import de.unisaarland.cs.se.selab.controller.EstimateHarvestController
import de.unisaarland.cs.se.selab.controller.FarmController
import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.controller.Simulator
import de.unisaarland.cs.se.selab.controller.SoilMoistureAndSunController
import de.unisaarland.cs.se.selab.model.Farm
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Statistic
import de.unisaarland.cs.se.selab.model.Tile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.mockito.MockedConstruction
import org.mockito.Mockito.mockConstruction
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest
import kotlin.test.Test

class SimulatorUnitTests {
    private val data: SimulationData = mock()
    private val map: Map = mock()
    private val farm1: Farm = mock()
    private val farm2: Farm = mock()
    private val tile1: Tile = mock()
    private val tile2: Tile = mock()
    private val tile3: Tile = mock()
    private val growable1: Growable = mock()
    private val growable2: Growable = mock()
    private val growable3: Growable = mock()
    private val statistic1: Statistic = mock()
    private val statistic2: Statistic = mock()

    // Conrstucted Contorllers capture
    private lateinit var harvestCtor: MockedConstruction<EstimateHarvestController>
    private lateinit var soilCtor: MockedConstruction<SoilMoistureAndSunController>
    private lateinit var cloudCtor: MockedConstruction<CloudController>
    private lateinit var farmCtor: MockedConstruction<FarmController>
    private lateinit var incidentCtor: MockedConstruction<IncidentController>

    @BeforeTest
    fun setup() {
        // DoNothing for the created controllers
        harvestCtor = mockConstruction(EstimateHarvestController::class.java) { mock, _ ->
            doNothing().whenever(mock).firstroundsimulation()
            doNothing().whenever(mock).calculateEstimateHarvest()
        }
        soilCtor = mockConstruction(SoilMoistureAndSunController::class.java) { mock, _ ->
            doNothing().whenever(mock).reduceSoilMoistureAndAdaptSun()
        }
        cloudCtor = mockConstruction(CloudController::class.java) { mock, _ ->
            doNothing().whenever(mock).cloudMovement()
        }
        farmCtor = mockConstruction(FarmController::class.java) { mock, _ ->
            doNothing().whenever(mock).farmsAction()
        }
        incidentCtor = mockConstruction(IncidentController::class.java) { mock, _ ->
            doNothing().whenever(mock).checkActiveIncidents()
            doNothing().whenever(mock).checkIncidents()
        }

        // Data: We have two farms
        whenever(data.getFarms()).thenReturn(mutableListOf(farm1, farm2))
        whenever(data.getMap()).thenReturn(map)

        // Make simulate() run 3 full rounds without stateful doAnswer
        whenever(data.getMaxTick()).thenReturn(3)

        // Each round causes multiple reads of getCurrentTick(), so duplicate each value
        whenever(data.getCurrentTick()).thenReturn(
            0, 0, 0, // round 1: cond, cond, increment
            1, 1, 1, // round 2
            2, 2, 2, // round 3
            3, 3 // final while check -> exit
        )

        whenever(data.getYearTick()).thenReturn(1)
        doNothing().whenever(data).setCurrentTick(any())
        doNothing().whenever(data).setYearTick(any())

        // Map
        whenever(map.getAllGrowable()).thenReturn(listOf(tile1, tile2, tile3))

        // Tiles
        // Tile 1
        whenever(tile1.getGrowable()).thenReturn(growable1)
        // Tile 2
        whenever(tile2.getGrowable()).thenReturn(growable2)
        // Tile 3
        whenever(tile3.getGrowable()).thenReturn(growable3)

        // Growables
        // Growable 1
        whenever(growable1.getCropsExpected()).thenReturn(50000)
        // Growable 2
        whenever(growable2.getCropsExpected()).thenReturn(20000)
        // Growable 3
        whenever(growable3.getCropsExpected()).thenReturn(10000)

        // Farm 1
        whenever(farm1.getID()).thenReturn(1)
        whenever(farm1.getStatistic()).thenReturn(statistic1)

        // Farm 2
        whenever(farm2.getID()).thenReturn(2)
        whenever(farm2.getStatistic()).thenReturn(statistic2)
        detektSetup()
    }

    fun detektSetup() {
        // Mocking the statistics
        // Statistic 1
        whenever(statistic1.getPotatoAmount()).thenReturn(10020)
        whenever(statistic1.getWheatAmount()).thenReturn(22300)
        whenever(statistic1.getOatAmount()).thenReturn(5)
        whenever(statistic1.getPumpkinAmount()).thenReturn(250003)
        whenever(statistic1.getAppleAmount()).thenReturn(3023231)
        whenever(statistic1.getGrapeAmount()).thenReturn(402525)
        whenever(statistic1.getAlmondAmount()).thenReturn(251231)
        whenever(statistic1.getCherryAmount()).thenReturn(2012312)
        whenever(statistic1.computeCombinedHarvest()).thenReturn(6063627)

        // Statistic 2
        whenever(statistic2.getPotatoAmount()).thenReturn(10020)
        whenever(statistic2.getWheatAmount()).thenReturn(363623)
        whenever(statistic2.getOatAmount()).thenReturn(34634)
        whenever(statistic2.getPumpkinAmount()).thenReturn(789343)
        whenever(statistic2.getAppleAmount()).thenReturn(2343241)
        whenever(statistic2.getGrapeAmount()).thenReturn(64297)
        whenever(statistic2.getAlmondAmount()).thenReturn(4982374)
        whenever(statistic2.getCherryAmount()).thenReturn(43984)
        whenever(statistic2.computeCombinedHarvest()).thenReturn(8223887)
    }

    @AfterEach
    fun cleanupMocks() {
        // 🔹 4. Clean up (important!)
        harvestCtor.close()
        soilCtor.close()
        cloudCtor.close()
        farmCtor.close()
        incidentCtor.close()
    }

    @Test
    @DisplayName("Three Round simulation. Ensures that Simulator calculates the statistics right")
    fun threeRoundSimulation() {
        // Call Simulator
        val simulator = Simulator(data)
        simulator.simulate()

        // ---- Verify controller calls per tick (3 rounds) ----
        val harvestMock = harvestCtor.constructed().single()
        val soilMock = soilCtor.constructed().single()
        val cloudMock = cloudCtor.constructed().single()
        val farmMock = farmCtor.constructed().single()
        val incidentMock = incidentCtor.constructed().single()

        // Verify Initial estimation setting
        verify(harvestMock).firstroundsimulation()

        // Verify per-round calls
        verify(soilMock, times(3)).reduceSoilMoistureAndAdaptSun()
        verify(cloudMock, times(3)).cloudMovement()
        verify(farmMock, times(3)).farmsAction()
        verify(incidentMock, times(3)).checkActiveIncidents()
        verify(incidentMock, times(3)).checkIncidents()
        verify(harvestMock, times(3)).calculateEstimateHarvest()

        // Verify Statistics

        verify(statistic1).computeCombinedHarvest()
        verify(statistic2).computeCombinedHarvest()

        // Each per-plant getter is read once per farm in calculateStatistic()
        verify(statistic1).getPotatoAmount()
        verify(statistic1).getWheatAmount()
        verify(statistic1).getOatAmount()
        verify(statistic1).getPumpkinAmount()
        verify(statistic1).getAppleAmount()
        verify(statistic1).getGrapeAmount()
        verify(statistic1).getAlmondAmount()
        verify(statistic1).getCherryAmount()

        verify(statistic2).getPotatoAmount()
        verify(statistic2).getWheatAmount()
        verify(statistic2).getOatAmount()
        verify(statistic2).getPumpkinAmount()
        verify(statistic2).getAppleAmount()
        verify(statistic2).getGrapeAmount()
        verify(statistic2).getAlmondAmount()
        verify(statistic2).getCherryAmount()

        // Map traversal: getAllGrowable called once; each tile’s growable and cropsExpected read
        verify(map).getAllGrowable()
        verify(tile1).getGrowable()
        verify(tile2).getGrowable()
        verify(tile3).getGrowable()
        verify(growable1).getCropsExpected()
        verify(growable2).getCropsExpected()
        verify(growable3).getCropsExpected()
    }
}
