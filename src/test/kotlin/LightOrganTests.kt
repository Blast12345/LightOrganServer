import colorService.FakeColorService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import server.FakeServer
import java.awt.Color

class LightOrganTests {

    private lateinit var server: FakeServer
    private lateinit var colorService: FakeColorService
    private val color = Color.blue

    @Before
    fun setup() {
        server = FakeServer()
        colorService = FakeColorService()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(server, colorService)
    }

    @Test
    fun `know when the light organ has been started`() {
        val sut = createSUT()
        assertFalse(sut.isRunning)
        sut.start()
        assertTrue(sut.isRunning)
    }

    @Test
    fun `send colors to the server when they become available`() {
        val sut = createSUT()
        sut.start()

        colorService.lambda?.invoke(color)
        assertEquals(color, server.color)
    }

    @Test
    fun `limit the number of colors to 60 times per second`() {
        val sut = createSUT()
        sut.start()

        server.millisecondsSinceLastSentColor = 0

        colorService.lambda?.invoke(color)
        assertNotEquals(color, server.color)

        server.millisecondsSinceLastSentColor = minimumColorDurationInMilliseconds(60)

        colorService.lambda?.invoke(color)
        assertEquals(color, server.color)
    }

    private fun minimumColorDurationInMilliseconds(colorsPerSecond: Int): Long {
        val minimumColorDurationInMilliseconds = 1 / colorsPerSecond.toFloat() * 1000
        return minimumColorDurationInMilliseconds.toLong()
    }

}