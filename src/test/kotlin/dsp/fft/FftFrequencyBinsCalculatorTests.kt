package dsp.fft

import bins.frequency.FrequencyBin
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextFloatArray

// TODO: Standardize all tests around every being variable specific
class FftFrequencyBinsCalculatorTests {

    private val fftCalculator: FftCalculator = mockk()

    private val frame = nextFloatArray()
    private val format = nextAudioFormat(sampleRate = 6f)
    private val magnitudes = floatArrayOf(1f, 2f, 3f)

    private val bin1 = FrequencyBin(0f, 1f)
    private val bin2 = FrequencyBin(1f, 2f)
    private val bin3 = FrequencyBin(2f, 3f)
    private val bins = listOf(bin1, bin2, bin3)

    @BeforeEach
    fun setupHappyPath() {
        every { fftCalculator.calculateMagnitudes(any()) } returns magnitudes
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): FftFrequencyBinsCalculator {
        return FftFrequencyBinsCalculator(
            fftCalculator = fftCalculator
        )
    }

    @Test
    fun `calculate the frequency bins for a given audio frame`() {
        val sut = createSUT()

        val actual = sut.calculate(frame, format)

        assertEquals(bins, actual)
        verify { fftCalculator.calculateMagnitudes(frame) }
    }

}
