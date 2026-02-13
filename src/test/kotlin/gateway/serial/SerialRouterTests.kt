package gateway.serial

import JsonMapper
import gateway.serial.wrappers.FakeSerialPort
import gateway.serial.wrappers.SerialFormat
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.job
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextPositiveInt
import kotlin.time.Duration.Companion.milliseconds

class SerialRouterTests {

    lateinit var serialPort: FakeSerialPort
    val baudRate: Int = nextPositiveInt()
    val serialFormat: SerialFormat = mockk()

    val requestTimeout = nextPositiveInt().milliseconds
    val request = FakeSerialRequest("1", "some request")
    val requestString = JsonMapper.writeValueAsString(request)!!
    val response = FakeSerialResponse("1", "some response")
    val responseString = JsonMapper.writeValueAsString(response)!!
    val randomObject = FakeSerialObject("random object")
    val randomObjectString = JsonMapper.writeValueAsString(randomObject)!!

    @BeforeEach
    fun setUp() {
        serialPort = FakeSerialPort()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    fun createSUT(testScope: CoroutineScope): SerialRouter {
        return SerialRouter(
            serialPort,
            baudRate,
            serialFormat,
            testScope
        )
    }

    // Connection State
    @Test
    fun `given the port is open, when initialized, then is connected`() = runTest {
        serialPort.isOpen = true
        val sut = createSUT(backgroundScope)

        assertTrue(sut.isConnected.value)
    }

    @Test
    fun `given the port is closed, when initialized, then is disconnected`() = runTest {
        serialPort.isOpen = false
        val sut = createSUT(backgroundScope)

        assertFalse(sut.isConnected.value)
    }

    // Connect
    @Test
    fun `connect to the port`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.connect()

        assertTrue(serialPort.isOpen)
        assertEquals(baudRate, serialPort.baudRate)
        assertEquals(serialFormat, serialPort.format)
    }

    @Test
    fun `when connecting to the port succeeds, then is connected`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.connect()

        assertTrue(sut.isConnected.value)
    }

    @Test
    fun `when connecting to the port fails, then is disconnected`() = runTest {
        val sut = createSUT(backgroundScope)
        serialPort.openException = Exception()

        assertThrows<Exception> { sut.connect() }

        assertFalse(sut.isConnected.value)
    }

    // Disconnect
    @Test
    fun `disconnect from the port`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()

        sut.disconnect()

        assertFalse(serialPort.isOpen)
        assertTrue(backgroundScope.coroutineContext.job.isCancelled) // Cancel any jobs
    }

    @Test
    fun `when disconnecting to the port succeeds, then is disconnected`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()

        sut.disconnect()

        assertFalse(sut.isConnected.value)
    }

    @Test
    fun `when disconnecting to the port fails, then is connected`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        serialPort.closeException = Exception()

        assertThrows<Exception> { sut.disconnect() }

        assertTrue(sut.isConnected.value)
    }

    // Request
    @Test
    fun `when a request is sent and a matching response is received, then the response is returned`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        serialPort.responseMap[requestString] = listOf(randomObjectString, responseString)

        val actual = sut.send(request, FakeSerialResponse::class.java)

        assertEquals(requestString, serialPort.writtenLines.firstOrNull())
        assertEquals(response, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when a request is sent and no response is received, then timeout`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        serialPort.responseMap[requestString] = listOf(randomObjectString)

        val startTime = currentTime.milliseconds

        assertThrows<TimeoutCancellationException> {
            sut.send(request, FakeSerialResponse::class.java, requestTimeout)
        }

        val elapsedTime = currentTime.milliseconds - startTime
        assertEquals(requestTimeout, elapsedTime)
    }

    @Test
    fun `given the port has become disconnected, when a request fails, then is disconnected`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        serialPort.isOpen = false

        assertThrows<Exception> { sut.send(request, FakeSerialResponse::class.java) }

        assertFalse(sut.isConnected.value)
    }

    // Send object
    @Test
    fun `when an object is sent, then the JSON string is written to the port`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()

        sut.send(randomObject)

        assertEquals(randomObjectString, serialPort.writtenLines.firstOrNull())
    }

    @Test
    fun `given the port has become disconnected, when sending an object fails, then is disconnected`() =
        runTest {
            val sut = createSUT(backgroundScope)
            sut.connect()
            serialPort.isOpen = false

            assertThrows<Exception> { sut.send(randomObject) }

            assertFalse(sut.isConnected.value)
        }

}