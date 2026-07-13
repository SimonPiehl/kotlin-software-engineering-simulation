package simon

import de.unisaarland.cs.se.selab.controller.CloudController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.Growable
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.util.GeneralConstants
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class CloudControllerTest {

    private lateinit var data: SimulationData
    private lateinit var cloudController: CloudController
    private lateinit var map: Map

    @BeforeEach
    fun setup() {
        data = mock()
        map = mock()
        whenever(data.getMap()).thenReturn(map)

        cloudController = CloudController(data)
    }

    @Test
    fun `cloud dissipates when amount zero`() {
        val cloud = mock<Cloud>()
        val tile = mock<Tile>()
        val clouds = mutableListOf(cloud)

        whenever(cloud.getAmount()).thenReturn(0)
        whenever(cloud.getLocation()).thenReturn(Coordinate(0, 0))
        whenever(data.getClouds()).thenAnswer { clouds }
        whenever(map.getTileByCoordinate(any<Coordinate>())).thenReturn(tile)

        doAnswer { invocation ->
            clouds.remove(invocation.arguments[0])
            null
        }.whenever(data).deleteCloud(any<Cloud>())

        cloudController.cloudMovement()

        verify(data).deleteCloud(cloud)
    }

    @Test
    fun `cloud rains on growable tile`() {
        val cloud = mock<Cloud>()
        val tile = mock<Tile>()
        val growable = mock<Growable>()
        val clouds = mutableListOf(cloud)

        whenever(cloud.getAmount()).thenReturn(GeneralConstants.MIN_RAIN_AMOUNT + 5)
        whenever(cloud.getLocation()).thenReturn(Coordinate(1, 1))
        whenever(data.getClouds()).thenAnswer { clouds }
        whenever(map.getTileByCoordinate(any<Coordinate>())).thenReturn(tile)

        whenever(tile.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile.getGrowable()).thenReturn(growable)
        whenever(growable.getMoistureExposure()).thenReturn(0)
        whenever(growable.getMaxMoisture()).thenReturn(10)

        cloudController.cloudMovement()

        verify(cloud, atLeastOnce()).setAmount(any())
        verify(growable, atLeastOnce()).setMoistureExposure(any())
    }
}
