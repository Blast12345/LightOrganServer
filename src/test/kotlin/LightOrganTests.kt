import color.FakeColorFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.FakeServer
import sound.input.FakeInput
import sound.input.samples.NormalizedAudioFrame

class LightOrganTests {

    private lateinit var input: FakeInput
    private lateinit var colorFactory: FakeColorFactory
    private lateinit var server: FakeServer

    @BeforeEach
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

        val samples = doubleArrayOf(1.1)
        val audioFrame = NormalizedAudioFrame(samples)
        input.listener?.invoke(audioFrame)

        assertEquals(colorFactory.color, server.color)
        assertEquals(audioFrame, colorFactory.audioFrame)
    }

}