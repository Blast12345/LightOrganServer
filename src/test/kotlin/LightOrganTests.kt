import color.ColorFactory
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import sound.frequencyBins.FrequencyBinsFactory
import sound.input.Input
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextNormalizedAudioFrame
import kotlin.random.Random

class LightOrganTests {

    private lateinit var input: Input
    private lateinit var server: Server
    private lateinit var colorFactory: ColorFactory
    private lateinit var frequencyBinsFactory: FrequencyBinsFactory
    private val audioFrame = Random.nextNormalizedAudioFrame()

    @BeforeEach
    fun setup() {
        input = mockk()
        coEvery { input.listenForAudioSamples(any()) } returns Unit

        server = mockk()
        every { server.sendColor(any()) } returns Unit

        colorFactory = mockk()
        every { colorFactory.createFor(any()) } returns Random.nextColor()

        frequencyBinsFactory = mockk()
        every { frequencyBinsFactory.createFrom(any(), any()) } returns Random.nextFrequencyBins()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            input = input,
            server = server,
            colorFactory = colorFactory,
            frequencyBinsFactory = frequencyBinsFactory
        )
    }

    @Test
    suspend fun `start listening to the input for audio`() {
        val sut = createSUT()
        sut.start()
        coVerify { input.listenForAudioSamples(sut) }
    }

    @Test
    fun `send a color to the server when an audio frame is received`() {
        val sut = createSUT()
        sut.receiveAudioFrame(audioFrame)
        verify { server.sendColor(any()) }
    }

    @Test
    fun `the sent color is computed from frequency bins`() {
        val sut = createSUT()
        val color = Random.nextColor()
        every { colorFactory.createFor(any()) } returns color
        sut.receiveAudioFrame(audioFrame)
        verify { server.sendColor(color) }
    }

    @Test
    fun `the frequency bins are created from the audio frame`() {
        val sut = createSUT()
        val frequencyBins = Random.nextFrequencyBins()
        every { frequencyBinsFactory.createFrom(any(), any()) } returns frequencyBins
        sut.receiveAudioFrame(audioFrame)
        verify { colorFactory.createFor(frequencyBins) }
    }

    @Test
    fun `the lowest supported frequency is 20hz`() {
        // NOTE: This is for practical reasons.
        // Lower frequencies are uncommon in music and supporting them will add latency.
        val sut = createSUT()
        sut.receiveAudioFrame(audioFrame)
        verify { frequencyBinsFactory.createFrom(any(), 20F) }
    }

}