package gateway

import color.StandardRgbColor
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.Gateway
import lightOrgan.gateway.GatewayDetails
import lightOrgan.gateway.GatewayFinder
import lightOrgan.gateway.GatewayManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextString

fun nextGatewayDetails(): GatewayDetails {
    return GatewayDetails(
        systemPath = nextString("path"),
        macAddress = nextString("mac"),
        firmwareVersion = nextString("version")
    )
}

class FakeGateway(
    override val details: GatewayDetails = nextGatewayDetails()
) : Gateway {

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    val sentColors = mutableListOf<StandardRgbColor>()

    override fun connect() {
        _isConnected.value = true
    }

    override fun disconnect() {
        _isConnected.value = false
    }

    override fun sendColor(color: StandardRgbColor) {
        sentColors.add(color)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GatewayManagerTests {

    private val gatewayFinder: GatewayFinder = mockk()

    private lateinit var gateway: FakeGateway

    @BeforeEach
    fun setupHappyPath() {
        gateway = FakeGateway()

        coEvery { gatewayFinder.find() } returns gateway
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): GatewayManager {
        return GatewayManager(
            gatewayFinder = gatewayFinder
        )
    }

    // Find
    @Test
    fun `given a gateway is available, then the gateway is found`() = runTest {
        val sut = createSUT()

        sut.findGateway()

        assertEquals(gateway.details, sut.gatewayDetails.first())
    }

    @Test
    fun `given no gateway is available, then no gateway is found`() = runTest {
        val sut = createSUT()
        coEvery { gatewayFinder.find() } returns null

        sut.findGateway()

        assertEquals(null, sut.gatewayDetails.first())
    }

    // Searching state

//    // Connect
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `connect to a gateway`() = runTest {
//        val sut = createSUT()
//
//        launch {
//            sut.connect()
//        }
//
//        runCurrent()
//        assertEquals(true, sut.isSearching.value)
//
//        advanceTimeBy(findDuration)
//        runCurrent()
//
//        assertEquals(gateway, sut.currentGateway.value)
//        assertEquals(false, sut.isSearching.value)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `given a gateway is already connected, then don't begin a search`() = runTest {
//        val sut = createSUT()
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = true
//
//        sut.connect()
//
//        runCurrent()
//
//        coVerify(exactly = 0) { gatewayFinder.find() }
//        assertEquals(false, sut.isSearching.value)
//    }
//
//    @Test
//    fun `given a search is already underway, then calling connect throws an error`() = runTest {
//        val sut = createSUT()
//        isSearchingFlow.value = true
//
//        val exception = assertThrows<IllegalStateException> {
//            sut.connect()
//        }
//
//        assertEquals("Already attempting to connect. Please wait.", exception.message)
//    }
//
//    // Disconnect
//    @Test
//    fun `disconnect from a gateway`() = runTest {
//        val sut = createSUT()
//        currentGatewayFlow.value = gateway
//
//        sut.disconnect()
//
//        verify { gateway.disconnect() }
//        assertEquals(null, sut.currentGateway.value)
//    }

    // Auto-reconnect
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `when a gateway becomes disconnected, attempt to reconnect`() = runTest {
//        val sut = createSUT()
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = true
//
//        isConnectedFlow.value = false
//        runCurrent()
//
//        verify { gateway.connect() }
//    }

}