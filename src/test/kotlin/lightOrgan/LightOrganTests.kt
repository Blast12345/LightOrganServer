package lightOrgan

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.color.ColorManagerFixture
import lightOrgan.input.AudioInputManagerFixture
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
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
    private lateinit var colorManager: ColorManagerFixture
    private val server: Server = mockk()
    private val sutScope = TestScope()

    private val frequencyBins = nextFrequencyBins()

    private val newAudio = nextAudioFrame()
    private val newColor = nextColor()

    @BeforeEach
    fun setupHappyPath() {
        audioInputManager = AudioInputManagerFixture.create()
        spectrumManager = SpectrumManagerFixture.create()
        colorManager = ColorManagerFixture.create()

        every { spectrumManager.mock.calculate(any()) } returns frequencyBins
        every { colorManager.mock.calculate(any()) } returns newColor
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
            colorManager = colorManager.mock,
            server = server,
            scope = sutScope
        )
    }

    @Test
    fun `when new audio is received, broadcast the color`() = runTest {
        val sut = createSUT()
        sutScope.advanceUntilIdle()

        audioInputManager.bufferedAudio.emit(newAudio)
        sutScope.advanceUntilIdle()

        verify { spectrumManager.mock.calculate(newAudio) }
        verify { colorManager.mock.calculate(frequencyBins) }
        verify { server.new(newColor) }
    }

}
