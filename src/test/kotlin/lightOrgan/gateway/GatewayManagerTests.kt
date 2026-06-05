package lightOrgan.gateway

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.GatewayManager.Event
import lightOrgan.gateway.GatewayManager.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextException
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class GatewayManagerTests {

    private lateinit var fakeGatewayFinder: FakeGatewayFinder

    @BeforeEach
    fun setupHappyPath() {
        fakeGatewayFinder = FakeGatewayFinder()
    }

    private fun createSUT(scope: CoroutineScope): RealGatewayManager {
        return RealGatewayManager(
            gatewayFinder = fakeGatewayFinder,
            scope = scope
        )
    }

    // Connect
    @Test
    fun `connect to a gateway`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayFinder.deferFind = CompletableDeferred()

        // Begin a connection attempt
        launch { sut.connect() }
        runCurrent()

        assertIs<State.Connecting>(sut.state.first())

        // Finish connecting
        fakeGatewayFinder.deferFind?.complete(Unit)
        runCurrent()

        assertEquals(
            State.Connected(fakeGatewayFinder.gateway),
            sut.state.first()
        )
    }

    @Test
    fun `given we are already attempting to connect, then calling connect again throws an error`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayFinder.deferFind = CompletableDeferred()
        launch { sut.connect() }
        runCurrent()

        assertThrows<Exception> { sut.connect() }

        // Allow the original connect attempt to finish
        fakeGatewayFinder.deferFind?.complete(Unit)
        runCurrent()

        assertIs<State.Connected>(sut.state.first())
    }

    @Test
    fun `given we are already connected, then calling connect again throws an error`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()

        assertThrows<Exception> { sut.connect() }
        assertIs<State.Connected>(sut.state.first())
    }

    @Test
    fun `when a gateway finder throws, then throw an error`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayFinder.error = nextException()

        assertThrows<Exception> { sut.connect() }
        assertIs<State.NoGateway>(sut.state.first())
    }

    @Test
    fun `when the gateway finder returns a disconnected gateway, then throw an error`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayFinder.gateway.isConnected.value = false

        assertThrows<Exception> { sut.connect() }
    }

    // Disconnect
    @Test
    fun `disconnect from a gateway`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()

        sut.disconnect()

        assertIs<State.NoGateway>(sut.state.first())
    }

    @Test
    fun `given we are attempting to connect, then calling disconnect throws an error`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayFinder.deferFind = CompletableDeferred()
        launch { sut.connect() }
        runCurrent()

        assertThrows<Exception> { sut.disconnect() }

        // Allow the original connect attempt to finish
        fakeGatewayFinder.deferFind?.complete(Unit)
        runCurrent()

        assertIs<State.Connected>(sut.state.first())
    }

    @Test
    fun `when the gateway unexpectedly disconnects, then emit an event`() = runTest {
        val sut = createSUT(backgroundScope)
        val events = mutableListOf<Event>()
        backgroundScope.launch { sut.events.toList(events) }
        sut.connect()
        runCurrent()

        fakeGatewayFinder.gateway.isConnected.value = false
        runCurrent()

        assertIs<State.NoGateway>(sut.state.first())
        assertIs<Event.UnexpectedDisconnect>(events.first())
    }

}