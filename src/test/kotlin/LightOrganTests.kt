import color.ColorFactoryInterface
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.ServerInterface
import sound.frequencyBins.FrequencyBinsFactoryInterface
import sound.input.InputInterface
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins
import wrappers.SystemTimeInterface
import kotlin.random.Random

class LightOrganTests {

    private var input: InputInterface = mockk()
    private var server: ServerInterface = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()
    private var systemTime: SystemTimeInterface = mockk()
    private val audioFrame = nextAudioFrame()
    private val oneSixtiethSecond = 1.0 / 60.0
    private val twoSixtiethsSecond = oneSixtiethSecond * 2.0

    @BeforeEach
    fun setup() {
        every { input.listenForAudioSamples(any()) } returns Unit
        every { server.sendColor(any()) } returns Unit
        every { colorFactory.create(any()) } returns nextColor()
        every { systemTime.currentTimeInSeconds() } returns Random.nextDouble()
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
            systemTime = systemTime
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
    fun `the sent color is calculated from the audio frame`() {
        val sut = createSUT()
        val color = nextColor()
        every { colorFactory.create(any()) } returns color
        sut.receiveAudioFrame(audioFrame)
        verify { server.sendColor(color) }
    }

    @Test
    fun `limit the number of colors per second to 60 to prevent saturation`() {
        val sut = createSUT()

        every { systemTime.currentTimeInSeconds() } returns oneSixtiethSecond
        sut.receiveAudioFrame(audioFrame)
        verify(exactly = 1) { server.sendColor(any()) }

        sut.receiveAudioFrame(audioFrame)
        verify(exactly = 1) { server.sendColor(any()) }

        every { systemTime.currentTimeInSeconds() } returns twoSixtiethsSecond
        sut.receiveAudioFrame(audioFrame)
        verify(exactly = 2) { server.sendColor(any()) }
    }

}