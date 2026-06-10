package lightOrgan.gateway

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.serial.SerialGatewayConnector
import lightOrgan.gateway.serial.SerialGatewayFinder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import serial.FakeSerialPort
import serial.SerialPortFinder
import toolkit.monkeyTest.nextException
import toolkit.utilities.triggerTimeout
import kotlin.coroutines.cancellation.CancellationException

class SerialGatewayFinderTests {

    private val portFinder: SerialPortFinder = mockk()
    private val connector: SerialGatewayConnector = mockk()

    private val port1 = FakeSerialPort()
    private val port2 = FakeSerialPort()
    private val gatewayPort = FakeSerialPort()

    private val fakeGateway = FakeGateway()

    @BeforeEach
    fun setupHappyPath() {
        every { portFinder.find() } returns listOf(port1, port2, gatewayPort)

        coEvery { connector.connect(port1) } throws nextException()
        coEvery { connector.connect(port2) } throws nextException()
        coEvery { connector.connect(gatewayPort) } returns fakeGateway
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): SerialGatewayFinder {
        return SerialGatewayFinder(
            portFinder = portFinder,
            connector = connector
        )
    }

    @Test
    fun `given a port connects successfully, then return the gateway`() = runTest {
        val sut = createSUT()

        val actual = sut.find()

        assertEquals(fakeGateway, actual)
    }

    @Test
    fun `given no port connects successfully, then throw`() = runTest {
        val sut = createSUT()
        every { portFinder.find() } returns listOf(port1, port2)

        assertThrows<Exception> { sut.find() }
    }

    @Test
    fun `given a connection attempt times out, then continue to next port`() = runTest {
        val sut = createSUT()
        coEvery { connector.connect(port1) } coAnswers { triggerTimeout() }
        val actual = sut.find()

        assertEquals(fakeGateway, actual)
    }

    @Test
    fun `given a cancellation exception is thrown, then it is propagated`() = runTest {
        val sut = createSUT()
        coEvery { connector.connect(port1) } throws CancellationException()

        assertThrows<CancellationException> { sut.find() }
    }

}