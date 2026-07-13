package niklas

import de.unisaarland.cs.se.selab.controller.IncidentController
import de.unisaarland.cs.se.selab.controller.SimulationData
import de.unisaarland.cs.se.selab.model.Cloud
import de.unisaarland.cs.se.selab.model.CloudCreation
import de.unisaarland.cs.se.selab.model.Incident
import de.unisaarland.cs.se.selab.model.Map
import de.unisaarland.cs.se.selab.model.Tile
import de.unisaarland.cs.se.selab.model.TileType
import de.unisaarland.cs.se.selab.util.Coordinate
import de.unisaarland.cs.se.selab.view.Logger
import org.junit.jupiter.api.DisplayName
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class IncidentControllerUnitTests {
    private val data: SimulationData = mock()
    private val map: Map = mock()
    private val tile1Farmstead: Tile = mock()
    private val tile2Field: Tile = mock()
    private val tile3Plantation: Tile = mock()
    private val tile4Road: Tile = mock()
    private val tile5Village: Tile = mock()
    private val cloudCreationLoc2Rad0Id0: CloudCreation = mock()
    private val coordinate0And0: Coordinate = mock()
    private val coordinate1And1: Coordinate = mock()
    private val coordinate2And2: Coordinate = mock()
    private val coordinate0And2: Coordinate = mock()
    private val coordinate2And0: Coordinate = mock()
    private val logger: Logger = mock()
    // private val createdCloud6: Cloud = mock()

    @BeforeTest
    fun setup() {
        // For data
        whenever(data.getCurrentTick()).thenReturn(1)
        whenever(data.getMap()).thenReturn(map)
        whenever(data.getMaxCloudID()).thenReturn(5)

        // For map
        whenever(
            map.getTilesByCoordinates(
                listOf(
                    coordinate0And0,
                    coordinate2And2,
                    coordinate0And2,
                    coordinate2And0
                )
            )
        ).thenReturn(mutableListOf(tile2Field, tile3Plantation, tile4Road, tile5Village))
        whenever(map.getTileByID(1)).thenReturn(tile1Farmstead)

        // For Tiles
        whenever(tile1Farmstead.getCoordinate()).thenReturn(coordinate1And1)
        whenever(tile2Field.getCoordinate()).thenReturn(coordinate0And0)
        whenever(tile3Plantation.getCoordinate()).thenReturn(coordinate2And0)
        whenever(tile4Road.getCoordinate()).thenReturn(coordinate0And2)
        whenever(tile5Village.getCoordinate()).thenReturn(coordinate2And2)
        // For TileType
        whenever(tile1Farmstead.getTileType()).thenReturn(TileType.FARMSTEAD)
        whenever(tile2Field.getTileType()).thenReturn(TileType.FIELD)
        whenever(tile3Plantation.getTileType()).thenReturn(TileType.PLANTATION)
        whenever(tile4Road.getTileType()).thenReturn(TileType.ROAD)
        whenever(tile5Village.getTileType()).thenReturn(TileType.VILLAGE)
        // For Ids
        whenever(tile1Farmstead.getID()).thenReturn(1)
        whenever(tile2Field.getID()).thenReturn(2)
        whenever(tile3Plantation.getID()).thenReturn(3)
        whenever(tile4Road.getID()).thenReturn(4)
        whenever(tile5Village.getID()).thenReturn(5)

        // Coordinates
        whenever(coordinate1And1.getX()).thenReturn(1)
        whenever(coordinate1And1.getY()).thenReturn(1)
        whenever(coordinate1And1.getNeighbours(1))
            .thenReturn(listOf(coordinate0And0, coordinate2And2, coordinate0And2, coordinate2And0))

        // CloudCreation cloudCreationLoc2Rad0
        whenever(cloudCreationLoc2Rad0Id0.getID()).thenReturn(0)
        whenever(cloudCreationLoc2Rad0Id0.getLocation()).thenReturn(1)
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(0)
        whenever(cloudCreationLoc2Rad0Id0.getAmount()).thenReturn(6000)
        whenever(cloudCreationLoc2Rad0Id0.getDuration()).thenReturn(6)
        whenever(cloudCreationLoc2Rad0Id0.toString()).thenReturn("CLOUD_CREATION")

        // For Clouds

        // For Logger
        doNothing().whenever(logger).logIncident(any(), any(), any())
    }

    @Test
    @DisplayName("Checks that checkActiveIncidents gets the Current Tick")
    fun validateCheckActiveIncidentsEmpty() {
        val incidentController = IncidentController(data)
        incidentController.checkActiveIncidents()
        verify(data).getCurrentTick()
    }

    @Test
    @DisplayName("Checks cloud Creation on one Tile (FARMSTEAD). ")
    fun validateCloudCreationOneTile() {
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        val cloudCap = argumentCaptor<Cloud>()
        verify(data).addCloud(cloudCap.capture())

        // Checking the created cloud
        assertEquals(6, cloudCap.firstValue.getID()) // maxCloudID (5) + 1
        assertEquals(6000, cloudCap.firstValue.getAmount())
        assertSame(6, cloudCap.firstValue.getDuration())
        assertSame(coordinate1And1, cloudCap.firstValue.getLocation())

        // Check no merges happened
        verify(data, never()).deleteCloud(any())
    }

    @Test
    @DisplayName("Checks cloud Creation on multiple Tiles.")
    fun validateCloudCreationMultipleTiles() {
        // Doing the specific whenever
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(1)
        whenever(data.getMaxCloudID()).thenReturn(5, 6, 7, 8)
        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        // verify: 4 clouds added (radius=1, village filtered out)
        val cloudCap = argumentCaptor<Cloud>()
        // "numInvocations = 4" Checks no 5-th cloud was created on tile VILLAGE
        verify(data, times(4)).addCloud(cloudCap.capture())

        // Getting the created Clouds
        val createdClouds = cloudCap.allValues

        // Check the created Clouds
        // First created Cloud
        assertEquals(6, createdClouds[0].getID()) // maxCloudID (5) + 1
        assertEquals(6000, createdClouds[0].getAmount())
        assertSame(6, createdClouds[0].getDuration())
        assertSame(coordinate1And1, createdClouds[0].getLocation())

        // Second created Cloud
        assertEquals(7, createdClouds[1].getID()) // maxCloudID (6) + 1
        assertEquals(6000, createdClouds[1].getAmount())
        assertSame(6, createdClouds[1].getDuration())
        assertSame(coordinate0And0, createdClouds[1].getLocation())

        // Third created Cloud
        assertEquals(8, createdClouds[2].getID()) // maxCloudID (7) + 1
        assertEquals(6000, createdClouds[2].getAmount())
        assertSame(6, createdClouds[2].getDuration())
        assertSame(coordinate2And0, createdClouds[2].getLocation())

        // Fourth created Cloud
        assertEquals(9, createdClouds[3].getID()) // maxCloudID (8) + 1
        assertEquals(6000, createdClouds[3].getAmount())
        assertSame(6, createdClouds[3].getDuration())
        assertSame(coordinate0And2, createdClouds[3].getLocation())
    }

    @Test
    @DisplayName("Checks cloud Creation on multiple Tiles where on one a cloud already exists.")
    fun validateCloudCreationMultipleTilesAndMerge() {
        // Doing the specific whenever
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        // Creating the cloud that already exists on tile1Farmstead
        val existingCloudAtFarmstead: Cloud = mock {
            on { getID() } doReturn 5
            on { getAmount() } doReturn 1000
            on { getDuration() } doReturn 4
            on { getLocation() } doReturn coordinate1And1
        }
        whenever(
            data.getOtherCloudOnSameCoordinate(
                argThat { getLocation() == coordinate1And1 }
            )
        ).thenReturn(existingCloudAtFarmstead)
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(1)
        whenever(data.getMaxCloudID()).thenReturn(5, 7, 8, 9)

        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        // verify: 4 clouds added (radius=1, village filtered out)
        val cloudCap = argumentCaptor<Cloud>()
        // "numInvocations = 4" Checks no 5-th cloud was created on tile VILLAGE
        verify(data, times(4)).addCloud(cloudCap.capture())

        // Getting the created Clouds
        val createdClouds = cloudCap.allValues

        // Check the created Clouds
        // First created Cloud
        assertEquals(7, createdClouds[0].getID()) // maxCloudID (5) + 1 + 1
        assertEquals(7000, createdClouds[0].getAmount())
        assertSame(4, createdClouds[0].getDuration())
        assertSame(coordinate1And1, createdClouds[0].getLocation())

        // Second created Cloud
        assertEquals(8, createdClouds[1].getID()) // maxCloudID (7) + 1
        assertEquals(6000, createdClouds[1].getAmount())
        assertSame(6, createdClouds[1].getDuration())
        assertSame(coordinate0And0, createdClouds[1].getLocation())

        // Third created Cloud
        assertEquals(9, createdClouds[2].getID()) // maxCloudID (8) + 1
        assertEquals(6000, createdClouds[2].getAmount())
        assertSame(6, createdClouds[2].getDuration())
        assertSame(coordinate2And0, createdClouds[2].getLocation())

        // Fourth created Cloud
        assertEquals(10, createdClouds[3].getID()) // maxCloudID (9) + 1
        assertEquals(6000, createdClouds[3].getAmount())
        assertSame(6, createdClouds[3].getDuration())
        assertSame(coordinate0And2, createdClouds[3].getLocation())
    }

    @Test
    @DisplayName(
        "Checks cloud Creation on multiple Tiles where on one a cloud already exists. " +
            "Cloud Creation Duration is -1"
    )
    fun validateCloudCreationMultipleTilesAndMergeDurationEternal() {
        // Doing the specific whenever
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        whenever(cloudCreationLoc2Rad0Id0.getDuration()).thenReturn(-1)
        // Creating the cloud that already exists on tile1Farmstead
        val existingCloudAtFarmstead: Cloud = mock {
            on { getID() } doReturn 5
            on { getAmount() } doReturn 1000
            on { getDuration() } doReturn 4
            on { getLocation() } doReturn coordinate1And1
        }
        whenever(
            data.getOtherCloudOnSameCoordinate(
                argThat { getLocation() == coordinate1And1 }
            )
        ).thenReturn(existingCloudAtFarmstead)
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(1)
        whenever(data.getMaxCloudID()).thenReturn(5, 7, 8, 9)

        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        // verify: 4 clouds added (radius=1, village filtered out)
        val cloudCap = argumentCaptor<Cloud>()
        // "numInvocations = 4" Checks no 5-th cloud was created on tile VILLAGE
        verify(data, times(4)).addCloud(cloudCap.capture())

        // Getting the created Clouds
        val createdClouds = cloudCap.allValues

        // Check the created Clouds
        // First created Cloud
        assertEquals(7, createdClouds[0].getID()) // maxCloudID (5) + 1 + 1
        assertEquals(7000, createdClouds[0].getAmount())
        assertSame(4, createdClouds[0].getDuration())
        assertSame(coordinate1And1, createdClouds[0].getLocation())

        // Second created Cloud
        assertEquals(8, createdClouds[1].getID()) // maxCloudID (7) + 1
        assertEquals(6000, createdClouds[1].getAmount())
        assertSame(-1, createdClouds[1].getDuration())
        assertSame(coordinate0And0, createdClouds[1].getLocation())

        // Third created Cloud
        assertEquals(9, createdClouds[2].getID()) // maxCloudID (8) + 1
        assertEquals(6000, createdClouds[2].getAmount())
        assertSame(-1, createdClouds[2].getDuration())
        assertSame(coordinate2And0, createdClouds[2].getLocation())

        // Fourth created Cloud
        assertEquals(10, createdClouds[3].getID()) // maxCloudID (9) + 1
        assertEquals(6000, createdClouds[3].getAmount())
        assertSame(-1, createdClouds[3].getDuration())
        assertSame(coordinate0And2, createdClouds[3].getLocation())
    }

    @Test
    @DisplayName(
        "Checks cloud Creation on multiple Tiles where on one a cloud already exists. " +
            "Existing cloud Duration is -1"
    )
    fun validateCloudCreationMultipleTilesAndMergeExistingCloudHasDurationEternal() {
        // Doing the specific whenever
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        // Creating the cloud that already exists on tile1Farmstead
        val existingCloudAtFarmstead: Cloud = mock {
            on { getID() } doReturn 5
            on { getAmount() } doReturn 1000
            on { getDuration() } doReturn -1
            on { getLocation() } doReturn coordinate1And1
        }
        whenever(
            data.getOtherCloudOnSameCoordinate(
                argThat { getLocation() == coordinate1And1 }
            )
        ).thenReturn(existingCloudAtFarmstead)
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(1)
        whenever(data.getMaxCloudID()).thenReturn(5, 7, 8, 9)

        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        // verify: 4 clouds added (radius=1, village filtered out)
        val cloudCap = argumentCaptor<Cloud>()
        // "numInvocations = 4" Checks no 5-th cloud was created on tile VILLAGE
        verify(data, times(4)).addCloud(cloudCap.capture())

        // Getting the created Clouds
        val createdClouds = cloudCap.allValues

        // Check the created Clouds
        // First created Cloud
        assertEquals(7, createdClouds[0].getID()) // maxCloudID (5) + 1 + 1
        assertEquals(7000, createdClouds[0].getAmount())
        assertSame(6, createdClouds[0].getDuration())
        assertSame(coordinate1And1, createdClouds[0].getLocation())

        // Second created Cloud
        assertEquals(8, createdClouds[1].getID()) // maxCloudID (7) + 1
        assertEquals(6000, createdClouds[1].getAmount())
        assertSame(6, createdClouds[1].getDuration())
        assertSame(coordinate0And0, createdClouds[1].getLocation())

        // Third created Cloud
        assertEquals(9, createdClouds[2].getID()) // maxCloudID (8) + 1
        assertEquals(6000, createdClouds[2].getAmount())
        assertSame(6, createdClouds[2].getDuration())
        assertSame(coordinate2And0, createdClouds[2].getLocation())

        // Fourth created Cloud
        assertEquals(10, createdClouds[3].getID()) // maxCloudID (9) + 1
        assertEquals(6000, createdClouds[3].getAmount())
        assertSame(6, createdClouds[3].getDuration())
        assertSame(coordinate0And2, createdClouds[3].getLocation())
    }

    @Test
    @DisplayName(
        "Checks cloud Creation on multiple Tiles where on one a cloud already exists. " +
            "Existing cloud Duration is -1. Cloud creation has duration -1"
    )
    fun validateCloudCreationMultipleTilesAndMergeExistingBothDurationEternal() {
        // Doing the specific whenever
        whenever(data.getIncidents()).thenReturn(
            mutableMapOf<Int, MutableList<Incident>>(
                1 to mutableListOf<Incident>(cloudCreationLoc2Rad0Id0)
            )
        )
        // Creating the cloud that already exists on tile1Farmstead
        val existingCloudAtFarmstead: Cloud = mock {
            on { getID() } doReturn 5
            on { getAmount() } doReturn 1000
            on { getDuration() } doReturn -1
            on { getLocation() } doReturn coordinate1And1
        }
        whenever(
            data.getOtherCloudOnSameCoordinate(
                argThat { getLocation() == coordinate1And1 }
            )
        ).thenReturn(existingCloudAtFarmstead)
        whenever(cloudCreationLoc2Rad0Id0.getDuration()).thenReturn(-1)
        whenever(cloudCreationLoc2Rad0Id0.getRadius()).thenReturn(1)
        whenever(data.getMaxCloudID()).thenReturn(5, 7, 8, 9)

        // Test
        val incidentController = IncidentController(data)
        incidentController.checkIncidents()

        // argumentCaptor (from mockito-kotlin) lets you grab the actual value a mock received
        // so you can assert on it later.
        // verify: 4 clouds added (radius=1, village filtered out)
        val cloudCap = argumentCaptor<Cloud>()
        // "numInvocations = 4" Checks no 5-th cloud was created on tile VILLAGE
        verify(data, times(4)).addCloud(cloudCap.capture())

        // Getting the created Clouds
        val createdClouds = cloudCap.allValues

        // Check the created Clouds
        // First created Cloud
        assertEquals(7, createdClouds[0].getID()) // maxCloudID (5) + 1 + 1
        assertEquals(7000, createdClouds[0].getAmount())
        assertSame(-1, createdClouds[0].getDuration())
        assertSame(coordinate1And1, createdClouds[0].getLocation())

        // Second created Cloud
        assertEquals(8, createdClouds[1].getID()) // maxCloudID (7) + 1
        assertEquals(6000, createdClouds[1].getAmount())
        assertSame(-1, createdClouds[1].getDuration())
        assertSame(coordinate0And0, createdClouds[1].getLocation())

        // Third created Cloud
        assertEquals(9, createdClouds[2].getID()) // maxCloudID (8) + 1
        assertEquals(6000, createdClouds[2].getAmount())
        assertSame(-1, createdClouds[2].getDuration())
        assertSame(coordinate2And0, createdClouds[2].getLocation())

        // Fourth created Cloud
        assertEquals(10, createdClouds[3].getID()) // maxCloudID (9) + 1
        assertEquals(6000, createdClouds[3].getAmount())
        assertSame(-1, createdClouds[3].getDuration())
        assertSame(coordinate0And2, createdClouds[3].getLocation())
    }
}
