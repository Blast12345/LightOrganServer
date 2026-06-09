package lightOrgan

import io.mockk.clearAllMocks
import io.mockk.every
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import lightOrgan.color.ColorManagerFixture
import lightOrgan.gateway.FakeGatewayManager
import lightOrgan.input.AudioInputManagerFixture
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextStandardRgbColor
import utilities.coroutines.Sequenced

@OptIn(ExperimentalCoroutinesApi::class)
class LightOrganTests {

    private lateinit var inputManager: AudioInputManagerFixture
    private lateinit var spectrumManager: SpectrumManagerFixture
    private lateinit var colorManager: ColorManagerFixture
    private lateinit var fakeGatewayManager: FakeGatewayManager

    private val audioFrame = nextAudioFrame()
    private val frequencyBins = nextFrequencyBins()
    private val newColor = nextStandardRgbColor()

    @BeforeEach
    fun setupHappyPath() {
        inputManager = AudioInputManagerFixture.create()
        spectrumManager = SpectrumManagerFixture.create()
        colorManager = ColorManagerFixture.create()
        fakeGatewayManager = FakeGatewayManager()

        every { spectrumManager.mock.calculate(audioFrame) } returns frequencyBins
        every { colorManager.mock.calculate(frequencyBins) } returns newColor
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
            gatewayManager = fakeGatewayManager,
            scope = scope,
        )
    }

    @Test
    fun `when an audio frame is received, then a color is derived and broadcast`() = runTest {
        val sut = createSUT(backgroundScope)
        fakeGatewayManager.connect()
        sut.start()
        runCurrent()

        val sequencedFrame = Sequenced("audio frame", 0L, audioFrame)
        inputManager.audioStream.emit(sequencedFrame)
        runCurrent()

        assertEquals(newColor, fakeGatewayManager.gateway.lastColor)
    }

}
