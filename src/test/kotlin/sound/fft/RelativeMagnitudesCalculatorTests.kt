package sound.fft

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray

class RelativeMagnitudesCalculatorTests {

    private var fftAlgorithm: FftAlgorithm = mockk()
    private var magnitudeNormalizer: MagnitudeNormalizer = mockk()

    private val signal = nextDoubleArray()

    private val fftOutput = nextDoubleArray()
    private val normalizerOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { fftAlgorithm.calculateRelativeMagnitudes(any()) } returns fftOutput
        every { magnitudeNormalizer.normalize(any(), any()) } returns normalizerOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): RelativeMagnitudesCalculator {
        return RelativeMagnitudesCalculator(
            fftAlgorithm = fftAlgorithm,
            magnitudeNormalizer = magnitudeNormalizer
        )
    }

    @Test
    fun `calculate the relative magnitudes of a signal`() {
        val sut = createSUT()
        val magnitudes = sut.calculate(signal)
        assertArrayEquals(normalizerOutput, magnitudes, 0.001)
        verify { fftAlgorithm.calculateRelativeMagnitudes(signal) }
        verify { magnitudeNormalizer.normalize(fftOutput, signal.size) }

    }

}