import colorService.FakeColorService
import org.junit.*
import org.junit.Assert.*
import server.FakeServer

class LightOrganTests {

    private lateinit var server: FakeServer
    private lateinit var colorService: FakeColorService
    private val color = "1"

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
        assertEquals(color, server.message)
    }

    @Test
    fun `limit the number of messages to 60 times per second`() {
        val sut = createSUT()
        sut.start()

        server.millisecondsSinceLastSentMessage = 0

        colorService.lambda?.invoke(color)
        assertNotEquals(color, server.message)

        server.millisecondsSinceLastSentMessage = minimumColorDurationInMilliseconds(60)

        colorService.lambda?.invoke(color)
        assertEquals(color, server.message)
    }

    private fun minimumColorDurationInMilliseconds(colorsPerSecond: Int): Long {
        val minimumColorDurationInMilliseconds = 1 / colorsPerSecond.toFloat() * 1000
        return minimumColorDurationInMilliseconds.toLong()
    }

}