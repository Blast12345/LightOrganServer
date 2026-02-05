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

    @Test
    fun `open connection to the port`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.connect()

        assertTrue(serialPort.isOpen)
        assertEquals(serialPort.baudRate, baudRate)
        assertEquals(serialPort.format, serialFormat)
    }

    @Test
    fun `close connection to the port`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        assertTrue(serialPort.isOpen)

        sut.disconnect()

        assertFalse(serialPort.isOpen)
        assertTrue(backgroundScope.coroutineContext.job.isCancelled)
    }

    @Test
    fun `when a request is sent and a matching response is received, then the response is returned`() = runTest {
        val sut = createSUT(backgroundScope)
        serialPort.responseMap[requestString] = listOf(randomObjectString, responseString)
        sut.connect()

        val actual = sut.send(request, FakeSerialResponse::class.java)

        assertEquals(response, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when a request is sent and no response is received, then timeout`() = runTest {
        val sut = createSUT(backgroundScope)
        serialPort.responseMap[requestString] = listOf(randomObjectString)
        sut.connect()

        val startTime = currentTime.milliseconds

        assertThrows<TimeoutCancellationException> {
            sut.send(request, FakeSerialResponse::class.java, requestTimeout)
        }

        val elapsedTime = currentTime.milliseconds - startTime
        assertEquals(requestTimeout, elapsedTime)
    }

}