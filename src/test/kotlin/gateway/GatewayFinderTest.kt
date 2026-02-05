package gateway

import gateway.serial.wrappers.SerialPort
import gateway.serial.wrappers.SerialPortFinder
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GatewayFinderTest {

    val serialPortFinder: SerialPortFinder = mockk()

    val otherPort1: SerialPort = mockk()
    val otherPort2: SerialPort = mockk()
    val gatewayPort: SerialPort = mockk()

    val gateway: Gateway = mockk()

    private fun createSUT(): GatewayFinder {
        return GatewayFinder(
            serialPortFinder = serialPortFinder
        )
    }

    @BeforeEach
    fun setupHappyPath() {
        mockkObject(Gateway.Companion)

        coEvery { Gateway.connect(otherPort1, any()) } throws Exception("ASDF 1")
        coEvery { Gateway.connect(otherPort2, any()) } throws Exception("ASDF 2")
        coEvery { Gateway.connect(gatewayPort, any()) } returns gateway
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given a valid port is available, then return the gateway`() = runTest {
        val sut = createSUT()
        every { serialPortFinder.find() } returns listOf(otherPort1, otherPort2, gatewayPort)

        val actual = sut.find()

        assertEquals(gateway, actual)
    }

    @Test
    fun `given no valid port is available, then return null`() = runTest {
        val sut = createSUT()
        every { serialPortFinder.find() } returns listOf(otherPort1, otherPort2)

        val actual = sut.find()

        assertNull(actual)
    }

}