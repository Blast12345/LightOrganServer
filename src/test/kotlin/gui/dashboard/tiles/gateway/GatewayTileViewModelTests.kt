package gui.dashboard.tiles.gateway

import gui.dashboard.snackbar.FakeSnackbarController
import gui.tiles.gateway.GatewayTileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.FakeGatewayManager
import lightOrgan.gateway.GatewayEvent
import lightOrgan.gateway.GatewayManagerState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextException

@OptIn(ExperimentalCoroutinesApi::class)
class GatewayTileViewModelTests {

    private lateinit var fakeGatewayManager: FakeGatewayManager
    private lateinit var fakeSnackbarController: FakeSnackbarController

    private val exception = nextException()

    @BeforeEach
    fun setupHappyPath() {
        fakeGatewayManager = FakeGatewayManager()
        fakeSnackbarController = FakeSnackbarController()
    }

    private fun createSUT(scope: CoroutineScope): GatewayTileViewModel {
        return GatewayTileViewModel(
            gatewayManager = fakeGatewayManager,
            snackbarController = fakeSnackbarController,
            scope = scope,
        )
    }

    // Connect
    @Test
    fun `connect to a gateway`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.connect()
        runCurrent()

        assert(fakeGatewayManager.connectionState.value is GatewayManagerState.Connected)
    }

    @Test
    fun `when connect fails, the error is shown`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayManager.connectError = exception

        sut.connect()
        runCurrent()

        assertEquals(exception.message, fakeSnackbarController.lastMessage)
    }

    // Disconnect
    @Test
    fun `disconnect from a gateway`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayManager.connectionState.value = GatewayManagerState.Connected(fakeGatewayManager.gateway)

        sut.disconnect()
        runCurrent()

        assert(fakeGatewayManager.connectionState.value is GatewayManagerState.NoGateway)
    }

    @Test
    fun `when disconnect fails, the error is shown`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayManager.disconnectError = exception

        sut.disconnect()
        runCurrent()

        assertEquals(exception.message, fakeSnackbarController.lastMessage)
    }

    // Events
    @Test
    fun `when the gateway connection is lost, show an alert`() = runTest {
        val sut = createSUT(backgroundScope)
        runCurrent() // allow the collector to initialize

        fakeGatewayManager.events.emit(GatewayEvent.ConnectionLost)
        runCurrent()

        assertEquals("Gateway connection lost.", fakeSnackbarController.lastMessage)
    }

}