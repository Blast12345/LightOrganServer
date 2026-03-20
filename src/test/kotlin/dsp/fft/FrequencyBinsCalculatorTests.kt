package dsp.fft

import audio.samples.AudioFrame
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextFloatArray

class FrequencyBinsCalculatorTests {

    private val fftCalculator: FftCalculator = mockk()

    private val audioFrame = AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat(sampleRate = 6f)
    )
    private val magnitudes = floatArrayOf(1f, 2f, 3f)

    // nyquist frequency = (sample rate / 2) = (6 / 2) = 3
    // bin spacing = (nyquist / magnitudes.size) = (3 / 3) = 1
    private val offsetBin = FrequencyBin(0f, 1f)
    private val bin2 = FrequencyBin(1f, 2f)
    private val bin3 = FrequencyBin(2f, 3f)

    @BeforeEach
    fun setupHappyPath() {
        every { fftCalculator.calculateMagnitudes(audioFrame.samples) } returns magnitudes
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsCalculator {
        return FrequencyBinsCalculator(
            fftCalculator = fftCalculator
        )
    }

    @Test
    fun `calculate the frequency bins for a given audio frame`() {
        val sut = createSUT()

        val actual = sut.calculate(audioFrame)

        assertEquals(listOf(bin2, bin3), actual)
    }

    @Test
    fun `exclude the DC offset bin`() {
        val sut = createSUT()

        val actual = sut.calculate(audioFrame)

        assertFalse(actual.contains(offsetBin))
    }

}
