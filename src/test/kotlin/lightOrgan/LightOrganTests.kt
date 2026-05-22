package lightOrgan

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lightOrgan.color.ColorManagerFixture
import lightOrgan.input.AudioInputManagerFixture
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import toolkit.monkeyTest.nextAudioStreamFrame
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextStandardRgbColor

@OptIn(ExperimentalCoroutinesApi::class)
class LightOrganTests {

    private lateinit var inputManager: AudioInputManagerFixture
    private lateinit var spectrumManager: SpectrumManagerFixture
    private lateinit var colorManager: ColorManagerFixture
    private val server: Server = mockk()

    private val streamFrame = nextAudioStreamFrame()
    private val frequencyBins = nextFrequencyBins()
    private val newColor = nextStandardRgbColor()

    @BeforeEach
    fun setupHappyPath() {
        inputManager = AudioInputManagerFixture.create()
        spectrumManager = SpectrumManagerFixture.create()
        colorManager = ColorManagerFixture.create()

        every { spectrumManager.mock.calculate(streamFrame.audio) } returns frequencyBins
        every { colorManager.mock.calculate(frequencyBins) } returns newColor
        every { server.new(newColor) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(scope: CoroutineScope): LightOrgan {
        return LightOrgan(
            inputManager = inputManager.mock,
            spectrumManager = spectrumManager.mock,
            colorManager = colorManager.mock,
            server = server,
            scope = scope,
        )
    }

    @Test
    fun `when an audio frame is received, then a color is derived and broadcast`() = runTest {
        val sut = createSUT(backgroundScope)
        sut.start()
        runCurrent()

        inputManager.audioStream.emit(streamFrame)
        runCurrent()

        verify { server.new(newColor) }
    }

}
