package gateway

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextString

class GatewayTests {

    private val device: UsbDevice = mockk()
    private val colorMessageFactory: ColorMessageFactory = mockk()

    private val nextColorMessage = nextString()

    @BeforeEach
    fun setupHappyPath() {
        every { device.send(any()) } returns Unit
        every { colorMessageFactory.create(any()) } returns nextColorMessage
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): Gateway {
        return Gateway(
            device = device,
            colorMessageFactory = colorMessageFactory
        )
    }

    @Test
    fun `when a new color is received then the gateway broadcasts a string representation of that color`() {
        val sut = createSUT()
        val color = nextColor()

        sut.new(color)

        verify { colorMessageFactory.create(color) }
        verify { device.send(nextColorMessage) }
    }

}
