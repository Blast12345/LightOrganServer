import color.ColorFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import sound.frequencyBins.FrequencyBinsFactory
import sound.input.Input
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins

class LightOrganTests {

    private var input: Input = mockk()
    private var server: Server = mockk()
    private var colorFactory: ColorFactory = mockk()
    private var frequencyBinsFactory: FrequencyBinsFactory = mockk()
    private val audioFrame = nextAudioFrame()

    @BeforeEach
    fun setup() {
        every { input.listenForAudioSamples(any()) } returns Unit
        every { server.sendColor(any()) } returns Unit
        every { colorFactory.create(any()) } returns nextColor()
        every { frequencyBinsFactory.create(any(), any()) } returns nextFrequencyBins()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
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
    fun `start listening to the input for audio`() {
        val sut = createSUT()
        sut.start()
        verify { input.listenForAudioSamples(sut) }
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
        val color = nextColor()
        every { colorFactory.create(any()) } returns color
        sut.receiveAudioFrame(audioFrame)
        verify { server.sendColor(color) }
    }

    @Test
    fun `the frequency bins are created from the audio frame`() {
        val sut = createSUT()
        val frequencyBins = nextFrequencyBins()
        every { frequencyBinsFactory.create(any(), any()) } returns frequencyBins
        sut.receiveAudioFrame(audioFrame)
        verify { colorFactory.create(frequencyBins) }
    }

    @Test
    fun `the lowest supported frequency is 20hz`() {
        // NOTE: This is for practical reasons.
        // Lower frequencies are uncommon in music and supporting them will add latency.
        val sut = createSUT()
        sut.receiveAudioFrame(audioFrame)
        verify { frequencyBinsFactory.create(any(), 20F) }
    }

}