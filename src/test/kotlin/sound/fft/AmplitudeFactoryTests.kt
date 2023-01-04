package sound.fft

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray

class AmplitudeFactoryTests {

    private var fftAlgorithm: FftAlgorithmInterface = mockk()
    private var hannWindowFilter: HannWindowFilterInterface = mockk()
    private val samples = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { fftAlgorithm.calculateRelativeAmplitudes(any()) } returns nextDoubleArray()
        every { hannWindowFilter.filter(any()) } returns nextDoubleArray()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): AmplitudeFactory {
        return AmplitudeFactory(
            fftAlgorithm = fftAlgorithm,
            hannWindowFilter = hannWindowFilter
        )
    }

    @Test
    fun `the samples are processed by FFT to receive amplitudes`() {
        val sut = createSUT()
        val expectedAmplitudes = nextDoubleArray()
        every { fftAlgorithm.calculateRelativeAmplitudes(any()) } returns expectedAmplitudes

        val amplitudes = sut.create(samples)

        assertEquals(expectedAmplitudes, amplitudes)
    }

    @Test
    fun `the samples go through a hann window filter to reduce spectral leakage`() {
        val sut = createSUT()
        val filteredSamples = nextDoubleArray()
        every { hannWindowFilter.filter(any()) } returns filteredSamples

        sut.create(samples)

        verify { hannWindowFilter.filter(samples) }
        verify { fftAlgorithm.calculateRelativeAmplitudes(filteredSamples) }
    }

}