package lightOrgan

import audio.samples.AccumulatingAudioBuffer
import color.ColorFactory
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.input.AudioInputManagerFixture
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins

@OptIn(ExperimentalCoroutinesApi::class)
class LightOrganTests {

    private lateinit var audioInputManager: AudioInputManagerFixture
    private lateinit var spectrumManager: SpectrumManagerFixture
    private val colorFactory: ColorFactory = mockk()
    private val audioBuffer: AccumulatingAudioBuffer = mockk()
    private val server: Server = mockk()
    private val sutScope = TestScope()

    private val frequencyBins = nextFrequencyBins()

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private val subscribers = mutableSetOf(subscriber1, subscriber2)

    private val newAudio = nextAudioFrame()
    private val bufferedAudio = nextAudioFrame()
    private val newColor = nextColor()

    @BeforeEach
    fun setupHappyPath() {
        audioInputManager = AudioInputManagerFixture.create()
        spectrumManager = SpectrumManagerFixture.create()

        every { audioBuffer.append(newAudio) } returns Unit
        coEvery { audioBuffer.drain() } returns bufferedAudio coAndThen { awaitCancellation() }
        every { spectrumManager.mock.calculate(any()) } returns frequencyBins
        every { colorFactory.create(frequencyBins) } returns newColor
        every { subscriber1.new(newColor) } returns Unit
        every { subscriber2.new(newColor) } returns Unit
        every { server.new(newColor) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            audioInputManager = audioInputManager.mock,
            spectrumManager = spectrumManager.mock,
            colorFactory = colorFactory,
            server = server,
            audioBuffer = audioBuffer,
            subscribers = subscribers,
            scope = sutScope
        )
    }

    @Test
    fun `when new audio is received, broadcast the color`() = runTest {
        val sut = createSUT()
        sutScope.advanceUntilIdle()

        audioInputManager.audioStream.emit(newAudio)
        sutScope.advanceUntilIdle()

        verify { spectrumManager.mock.calculate(bufferedAudio) }
        verify { colorFactory.create(frequencyBins) }
        verify { subscriber1.new(newColor) }
        verify { subscriber2.new(newColor) }
        verify { server.new(newColor) }
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        val newSubscriber: LightOrganSubscriber = mockk()

        sut.addSubscriber(newSubscriber)

        assertTrue(subscribers.contains(newSubscriber))
    }

}
