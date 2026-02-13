package gateway

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextPositiveInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun nextDuration(): Duration {
    return nextPositiveInt().milliseconds
}

class GatewayManagerTests {

    val gatewayFinder: GatewayFinder = mockk(relaxed = true)
    val isSearchingFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val currentGatewayFlow: MutableStateFlow<Gateway?> = MutableStateFlow(null)

    val findDuration: Duration = nextDuration()
    val gateway: Gateway = mockk(relaxed = true)
    val isConnectedFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @BeforeEach
    fun setupHappyPath() {
        coEvery { gatewayFinder.find() } coAnswers { delay(findDuration); gateway }
        isSearchingFlow.value = false
        currentGatewayFlow.value = null

        every { gateway.isConnected } returns isConnectedFlow.asStateFlow()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): GatewayManager {
        return GatewayManager(
            gatewayFinder,
            isSearchingFlow,
            currentGatewayFlow
        )
    }

    // Connect
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `connect to a gateway`() = runTest {
        val sut = createSUT()

        launch {
            sut.connect()
        }

        runCurrent()
        assertEquals(true, sut.isSearching.value)

        advanceTimeBy(findDuration)
        runCurrent()

        assertEquals(gateway, sut.currentGateway.value)
        assertEquals(false, sut.isSearching.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a gateway is already connected, then don't begin a search`() = runTest {
        val sut = createSUT()
        currentGatewayFlow.value = gateway
        isConnectedFlow.value = true

        sut.connect()

        runCurrent()

        coVerify(exactly = 0) { gatewayFinder.find() }
        assertEquals(false, sut.isSearching.value)
    }

    @Test
    fun `given a search is already underway, then calling connect throws an error`() = runTest {
        val sut = createSUT()
        isSearchingFlow.value = true

        val exception = assertThrows<IllegalStateException> {
            sut.connect()
        }

        assertEquals("Already attempting to connect. Please wait.", exception.message)
    }

    // Disconnect
    @Test
    fun `disconnect from a gateway`() = runTest {
        val sut = createSUT()
        currentGatewayFlow.value = gateway

        sut.disconnect()

        verify { gateway.disconnect() }
        assertEquals(null, sut.currentGateway.value)
    }

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