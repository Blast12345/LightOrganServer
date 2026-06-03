package lightOrgan.gateway.serial.client

import io.mockk.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.serial.SerialConnection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import serial.rpc.DefaultSerialRpcClient
import serial.rpc.request
import toolkit.monkeyTest.nextString
import kotlin.time.Duration.Companion.milliseconds

class DefaultSerialJsonRpcPeerTests {

    private val connection: SerialConnection = mockk()
    private val connectionState = MutableStateFlow(false)
    private val portPath = nextString("port-path")
    private val capturedLine = slot<String>()
    private val incomingLines = MutableSharedFlow<String>()

    private val generatedId = "test-id"

    data class TestObject(val myKey: String)

    @BeforeEach
    fun setupHappyPath() {
        every { connection.isConnected } returns connectionState
        every { connection.portPath } returns portPath
        every { connection.connect() } returns Unit
        every { connection.disconnect() } returns Unit
        every { connection.write(capture(capturedLine)) } returns Unit
        every { connection.incomingLines } returns incomingLines
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): DefaultSerialRpcClient {
        return DefaultSerialRpcClient(
            connection = connection,
            generateId = { generatedId }
        )
    }

    // Lifecycle
    @Test
    fun `connect to the serial port`() {
        val sut = createSUT()

        sut.connect()

        verify { connection.connect() }
    }

    @Test
    fun `disconnect from the serial port`() {
        val sut = createSUT()

        sut.disconnect()

        verify { connection.disconnect() }
    }

    @Test
    fun `get the connection state`() {
        val sut = createSUT()

        connectionState.value = true
        assertEquals(true, sut.isConnected.value)

        connectionState.value = false
        assertEquals(false, sut.isConnected.value)
    }

    // Details
    @Test
    fun `get the port path`() {
        val sut = createSUT()

        assertEquals(portPath, sut.name)
    }

    // Send
    @Test
    fun `send an object to the serial port`() {
        val sut = createSUT()

        sut.send("test", TestObject("my-value"))

        val expected = """{"id":"test-id","command":"test","data":{"my-key":"my-value"}}"""
        assertEquals(expected, capturedLine.captured)
    }

    // Request
    @Test
    fun `when a request is sent to the serial port, then the response is received`() = runTest {
        val sut = createSUT()
        val requestBody = TestObject("my-request-data")
        val responseBody = TestObject("my-response-data")

        launch {
            delay(100.milliseconds)
            incomingLines.emit("""{"id":"test-id","command":"test-response","data":{"my-key":"my-response-data"}}""")
        }

        val response: TestObject = sut.request("test-request", requestBody)

        val expected = """{"id":"test-id","command":"test-request","data":{"my-key":"my-request-data"}}"""
        assertEquals(expected, capturedLine.captured)
        assertEquals(responseBody, response)
    }

    @Test
    fun `request ignores responses with non-matching id`() = runTest {
        val sut = createSUT()

        launch {
            delay(100.milliseconds)
            incomingLines.emit("""{"id":"wrong-id","command":"test-response","data":{"my-key":"wrong"}}""")
            incomingLines.emit("""{"id":"test-id","command":"test-response","data":{"my-key":"correct"}}""")
        }

        val response: TestObject = sut.request("test-request")

        assertEquals(TestObject("correct"), response)
    }


    @Test
    fun `request throws on timeout when no response arrives`() = runTest {
        val sut = createSUT()

        assertThrows<TimeoutCancellationException> {
            sut.request<TestObject>("test-request", timeout = 500.milliseconds)
        }
    }

}