import colorListener.FakeColorFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import server.FakeServer
import sound.input.FakeInput

class LightOrganTests {

    private lateinit var input: FakeInput
    private lateinit var colorFactory: FakeColorFactory
    private lateinit var server: FakeServer

    @Before
    fun setup() {
        input = FakeInput()
        colorFactory = FakeColorFactory()
        server = FakeServer()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(input, colorFactory, server)
    }

    @Test
    fun `send colors to the server when the input has new data`() {
        val sut = createSUT()
        sut.start()

        val sample = doubleArrayOf(1.1, 2.2)
        input.listener?.invoke(sample)

        assertEquals(colorFactory.color, server.color)
        assertEquals(sample, colorFactory.sample)
    }

}