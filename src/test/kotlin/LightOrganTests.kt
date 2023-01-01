import color.FakeColorFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import server.FakeServer
import sound.input.FakeInput
import sound.input.sample.AudioFrame
import javax.sound.sampled.AudioFormat

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
    fun `send colors to the server when the input provides new samples`() {
        val sut = createSUT()
        sut.start()

        val samples = byteArrayOf(1)
        val format = AudioFormat(44100F, 8, 1, true, true)
        val audioFrame = AudioFrame(samples, format)
        input.listener?.invoke(audioFrame)

        assertEquals(colorFactory.color, server.color)
        assertEquals(audioFrame, colorFactory.audioFrame)
    }

}