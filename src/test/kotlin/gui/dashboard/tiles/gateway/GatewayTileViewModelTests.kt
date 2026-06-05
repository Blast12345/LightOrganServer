package gui.dashboard.tiles.gateway

import gui.snackbar.FakeSnackbarController
import gui.tiles.gateway.GatewayTileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.FakeGatewayManager
import lightOrgan.gateway.GatewayManager
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

        assert(fakeGatewayManager.state.value is GatewayManager.State.Connected)
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
        fakeGatewayManager.state.value = GatewayManager.State.Connected(fakeGatewayManager.gateway)

        sut.disconnect()
        runCurrent()

        assert(fakeGatewayManager.state.value is GatewayManager.State.NoGateway)
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
    fun `when the gateway unexpectedly disconnects, show an alert`() = runTest {
        val sut = createSUT(backgroundScope)
        runCurrent() // allow the collector to initialize

        fakeGatewayManager.events.emit(GatewayManager.Event.UnexpectedDisconnect)
        runCurrent()

        assertEquals("Gateway unexpectedly disconnected.", fakeSnackbarController.lastMessage)
    }

}