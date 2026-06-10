package lightOrgan.gateway.serial

import jsonrpc.JsonRpcRequest
import jsonrpc.RequestFailureException
import jsonrpc.sendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import serial.FakeSerialPort
import toolkit.extensions.collectInto
import toolkit.monkeyTest.TestObject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class SerialJsonRpcConnectionTests {

    private lateinit var fakePort: FakeSerialPort
    private val generatedId = "request1"

    @BeforeEach
    fun setupHappyPath() {
        fakePort = FakeSerialPort()
    }

    private fun createSUT(scope: CoroutineScope): SerialJsonRpcConnection {
        return SerialJsonRpcConnection(
            port = fakePort,
            generateId = { generatedId },
            scope = scope
        )
    }

    // Connection details
    @Test
    fun `get the name`() = runTest {
        val sut = createSUT(backgroundScope)

        assertEquals(fakePort.name, sut.name)
    }

    @Test
    fun `get the baud rate`() = runTest {
        val sut = createSUT(backgroundScope)

        assertEquals(fakePort.baudRate, sut.baudRate)
    }

    @Test
    fun `get the frame format`() = runTest {
        val sut = createSUT(backgroundScope)

        assertEquals(fakePort.frameFormat, sut.frameFormat)
    }

    // Connection state
    @Test
    fun `get the connection state`() = runTest {
        val sut = createSUT(backgroundScope)

        fakePort.isOpen.value = true
        assertEquals(true, sut.isConnected.value)

        fakePort.isOpen.value = false
        assertEquals(false, sut.isConnected.value)
    }

    // Lifecycle
    @Test
    fun `connect to the port`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.connect()

        assertEquals(true, sut.isConnected.value)
    }

    @Test
    fun `disconnect from the port`() = runTest {
        val sut = createSUT(backgroundScope)
        fakePort.isOpen.value = true

        sut.disconnect()

        assertEquals(false, sut.isConnected.value)
    }

    // Send Notification
    @Test
    fun `send a notification without parameters to the port`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.sendNotification(
            method = "notification-a",
            params = null
        )

        assertEquals(
            """{"method":"notification-a","jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )
    }

    @Test
    fun `send a notification with parameters to the port`() = runTest {
        val sut = createSUT(backgroundScope)

        sut.sendNotification(
            method = "notification-b",
            params = TestObject(value = 123)
        )

        assertEquals(
            """{"method":"notification-b","params":{"value":123},"jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )
    }

    @Test
    fun `sending a notification throws on timeout`() = runTest {
        val sut = createSUT(backgroundScope)
        fakePort.suspendWrites = true

        assertThrows<TimeoutCancellationException> {
            sut.sendNotification("test", null, timeout = 500.milliseconds)
        }
    }

    // Send Request
    @Test
    fun `send a request without parameters to the port`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Request
        val response = async {
            sut.sendRequest<TestObject>(
                method = "request-a",
                params = null
            )
        }
        runCurrent()

        assertEquals(
            """{"id":"request1","method":"request-a","jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )

        // Respond
        val expectedResult = TestObject(value = 42)
        fakePort.receiveLine("""{"id":"request1","result":{"value":42},"jsonrpc":"2.0"}""")
        runCurrent()

        assertEquals(expectedResult, response.await())
    }

    @Test
    fun `send a request with parameters to the port`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Request
        val response = async {
            sut.sendRequest<TestObject>(
                method = "request-b",
                params = TestObject(value = 321)
            )
        }
        runCurrent()

        assertEquals(
            """{"id":"request1","method":"request-b","params":{"value":321},"jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )

        // Respond
        val expectedResult = TestObject(value = 123)
        fakePort.receiveLine("""{"id":"request1","result":{"value":123},"jsonrpc":"2.0"}""")
        runCurrent()

        assertEquals(expectedResult, response.await())
    }

    @Test
    fun `sending a request throws on timeout`() = runTest {
        val sut = createSUT(backgroundScope)
        fakePort.suspendWrites = true

        assertThrows<TimeoutCancellationException> {
            sut.sendRequest("test", null, timeout = 500.milliseconds)
        }
    }

    @Test
    fun `sending a request throws when the port responds with a failure`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Request
        val response = backgroundScope.async {
            runCatching { sut.sendRequest<TestObject>("test", null) }
        }
        runCurrent()

        // Respond
        fakePort.receiveLine("""{"id":"request1","error":{"code":1,"message":"uh-oh","data":{"value":-123}},"jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val exception = response.await().exceptionOrNull() as RequestFailureException

        assertEquals(1, exception.code)
        assertEquals("uh-oh", exception.message)
        assertEquals(-123, exception.data?.get("value")?.asInt())
    }

    @Test
    fun `pending requests ignore responses with a non-matching id`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Request
        val response = async {
            sut.sendRequest<TestObject>("test", null)
        }
        runCurrent()

        // Response
        fakePort.receiveLine("""{"id":"wrong-id","result":{"value":999},"jsonrpc":"2.0"}""")
        runCurrent()

        fakePort.receiveLine("""{"id":"request1","result":{"value":42},"jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        assertEquals(TestObject(value = 42), response.await())
    }

    @Test
    fun `pending requests are canceled on disconnect`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Request
        val response = backgroundScope.async {
            runCatching { sut.sendRequest<TestObject>("test", null) }
        }
        runCurrent()

        // Disconnect
        sut.disconnect()
        runCurrent()

        // Assert
        val exception = response.await().exceptionOrNull()
        assertInstanceOf(CancellationException::class.java, exception)
    }

    // Receiving requests
    @Test
    fun `receive requests with parameters`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for requests
        val received = sut.incomingRequests.collectInto(this)

        // Port sends the request
        fakePort.receiveLine("""{"id":"req1","method":"doSomething","params":{"value":5},"jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("req1", request.id)
        assertEquals("doSomething", request.method)
        assertEquals(5, request.params?.get("value")?.asInt())
    }

    @Test
    fun `receive requests without parameters`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for requests
        val received = sut.incomingRequests.collectInto(this)

        // Port sends the request
        fakePort.receiveLine("""{"id":"req1","method":"doSomething","jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("req1", request.id)
        assertEquals("doSomething", request.method)
        assertEquals(null, request.params)
    }

    @Test
    fun `incoming requests ignores malformed json`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for notifications
        val received = sut.incomingRequests.collectInto(this)

        // Port sends invalid notification
        fakePort.receiveLine("not valid json")
        runCurrent()

        // Port sends valid notification
        fakePort.receiveLine("""{"id":"req1","method":"doSomething","jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("doSomething", request.method)
    }

    @Test
    fun `receive notifications with parameters`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for notifications
        val received = sut.incomingNotifications.collectInto(this)

        // Port sends the notification
        fakePort.receiveLine("""{"method":"doSomething","params":{"value":57},"jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("doSomething", request.method)
        assertEquals(57, request.params?.get("value")?.asInt())
    }

    @Test
    fun `receive notifications without parameters`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for notifications
        val received = sut.incomingNotifications.collectInto(this)

        // Port sends the notification
        fakePort.receiveLine("""{"method":"doSomething","jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("doSomething", request.method)
        assertEquals(null, request.params)
    }

    @Test
    fun `incoming notifications ignores malformed json`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Start watching for notifications
        val received = sut.incomingNotifications.collectInto(this)

        // Port sends invalid notification
        fakePort.receiveLine("not valid json")
        runCurrent()

        // Port sends valid notification
        fakePort.receiveLine("""{"method":"doSomething","jsonrpc":"2.0"}""")
        runCurrent()

        // Assert
        val request = received.first()
        assertEquals("doSomething", request.method)
    }

    // Respond
    @Test
    fun `given an incoming request, respond with success`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Pretend we received this request
        val request = JsonRpcRequest(id = "req1", method = "respond-to-me", params = null)

        // Respond to the request
        sut.respondWithSuccess(request.id, TestObject(value = 42))

        // Assert
        assertEquals(
            """{"id":"req1","result":{"value":42},"jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )
    }

    @Test
    fun `given an incoming request, respond with failure`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.connect()
        runCurrent()

        // Pretend we received this request
        val request = JsonRpcRequest(id = "req1", method = "respond-to-me", params = null)

        // Respond to the request
        sut.respondWithFailure(
            id = "req1",
            code = -1,
            message = "Invalid request",
            data = TestObject(value = 99)
        )

        // Assert
        assertEquals(
            """{"id":"req1","error":{"code":-1,"message":"Invalid request","data":{"value":99}},"jsonrpc":"2.0"}""",
            fakePort.writtenLines.first()
        )
    }

}