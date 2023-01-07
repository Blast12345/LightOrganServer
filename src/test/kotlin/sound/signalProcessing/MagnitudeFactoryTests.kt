package sound.signalProcessing

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray

class MagnitudeFactoryTests {

    private var fftAlgorithm: FftAlgorithmInterface = mockk()
    private var hannWindowFilter: HannWindowFilterInterface = mockk()
    private var denoiser: DenoiserInterface = mockk()
    private val samples = nextDoubleArray()
    private val fftOutput = nextDoubleArray()
    private val hannWindowOutput = nextDoubleArray()
    private val denoiserOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { fftAlgorithm.calculateMagnitudes(any()) } returns fftOutput
        every { hannWindowFilter.filter(any()) } returns hannWindowOutput
        every { denoiser.denoise(any()) } returns denoiserOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): MagnitudeFactory {
        return MagnitudeFactory(
            fftAlgorithm = fftAlgorithm,
            hannWindowFilter = hannWindowFilter,
            denoiser = denoiser
        )
    }

    @Test
    fun `the samples are processed to produce magnitudes`() {
        val sut = createSUT()
        val magnitudes = sut.create(samples)
        assertEquals(denoiserOutput, magnitudes)
    }

    @Test
    fun `the samples are processed by FFT to identify the relative magnitude of particular frequencies`() {
        val sut = createSUT()
        sut.create(samples)
        verify { fftAlgorithm.calculateMagnitudes(hannWindowOutput) }
    }

    @Test
    fun `the samples go through a hann window filter to reduce spectral leakage`() {
        val sut = createSUT()
        sut.create(samples)
        verify { hannWindowFilter.filter(samples) }
    }

    @Test
    fun `the magnitudes are de-noised to account for interference`() {
        val sut = createSUT()
        sut.create(samples)
        every { denoiser.denoise(fftOutput) }
    }

}