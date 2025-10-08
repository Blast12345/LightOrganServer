package gateway

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextColor
import kotlinx.coroutines.test.*
import toolkit.monkeyTest.nextPositiveLong

class GatewayTests {

    private val device1: UsbDevice = mockk()
    private val device2: UsbDevice = mockk()
    private val healthCheckMS = nextPositiveLong()

    @BeforeEach
    fun setupHappyPath() {
        every { device1.connect() } returns Unit
        every { device1.disconnect() } returns Unit
        every { device1.isConnected() } returns false
        every { device1.send(any()) } returns Unit

        every { device2.connect() } returns Unit
        every { device1.disconnect() } returns Unit
        every { device2.isConnected() } returns false
        every { device2.send(any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when a usb device is assigned then connect to that device`() {
        val sut = Gateway()

        sut.setUsbDevice(device1)

        verify { device1.connect() }
    }

    @Test
    fun `given a device is already assigned when a new usb device is assigned then connect to the new device`() {
        val sut = Gateway()
        sut.setUsbDevice(device1)

        sut.setUsbDevice(device2)

        verify { device2.connect() }
    }

    @Test
    fun `given a device is already assigned when a new usb device is assigned then disconnect from the previous device`() {
        val sut = Gateway()
        sut.setUsbDevice(device1)
        every { device1.isConnected() } returns true

        sut.setUsbDevice(device2)

        verify { device1.disconnect() }
    }

    @Test
    fun `when a new color is received then the string representation of that color is sent to the connected device`() {
        val sut = Gateway()
        sut.setUsbDevice(device1)

        val color = nextColor()
        sut.new(color)

        val expectedMessage = "${color.red},${color.green},${color.blue}"
        verify { device1.send(expectedMessage) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `reconnect to a device when the device loses connection`() = runTest {
        val sut = Gateway(scope = backgroundScope, healthCheckMS = healthCheckMS)

        // Initial connection (fails)
        sut.setUsbDevice(device1)
        advanceTimeBy(1)
        verify(exactly = 1) { device1.connect() }

        every { device1.isConnected() } returns false

        // Retry (succeeds)
        advanceTimeBy(healthCheckMS)
        verify(exactly = 2) { device1.connect() }

        every { device1.isConnected() } returns true

        // No additional connection attempts going forward
        advanceTimeBy(healthCheckMS * 10)
        verify(exactly = 2) { device1.connect() }
    }

}
