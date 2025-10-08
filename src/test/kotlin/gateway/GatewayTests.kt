package gateway

import androidx.compose.runtime.key
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

    private val keyword: String = nextString()
    private val device: UsbDevice = mockk()

    @BeforeEach
    fun setupHappyPath() {
        every { device.send(any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): Gateway {
        return Gateway(
            keyword = keyword
        )
    }

    // todo: check for device

    @Test
    fun `when a new color is received then the gateway broadcasts a string representation of that color`() {
        val sut = createSUT()
        val color = nextColor()

        sut.new(color)

        val expectedMessage = "${color.red},${color.green},${color.blue}"
        verify { device.send(expectedMessage) }
    }

}
