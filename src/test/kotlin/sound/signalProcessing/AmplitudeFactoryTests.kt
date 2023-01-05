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

class AmplitudeFactoryTests {

    private var fftAlgorithm: FftAlgorithmInterface = mockk()
    private var hannWindowFilter: HannWindowFilterInterface = mockk()
    private var denoiser: DenoiserInterface = mockk()
    private val samples = nextDoubleArray()
    private val fftOutput = nextDoubleArray()
    private val hannWindowOutput = nextDoubleArray()
    private val denoiserOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { fftAlgorithm.calculateRelativeAmplitudes(any()) } returns fftOutput
        every { hannWindowFilter.filter(any()) } returns hannWindowOutput
        every { denoiser.denoise(any()) } returns denoiserOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): AmplitudeFactory {
        return AmplitudeFactory(
            fftAlgorithm = fftAlgorithm,
            hannWindowFilter = hannWindowFilter,
            denoiser = denoiser
        )
    }

    @Test
    fun `the samples are processed to produce amplitudes`() {
        val sut = createSUT()
        val amplitudes = sut.create(samples)
        assertEquals(denoiserOutput, amplitudes)
    }

    @Test
    fun `the samples are processed by FFT to identify the relative amplitude of particular frequencies`() {
        val sut = createSUT()
        sut.create(samples)
        verify { fftAlgorithm.calculateRelativeAmplitudes(hannWindowOutput) }
    }

    @Test
    fun `the samples go through a hann window filter to reduce spectral leakage`() {
        val sut = createSUT()
        sut.create(samples)
        verify { hannWindowFilter.filter(samples) }
    }

    @Test
    fun `the amplitudes are de-noised to account for interference`() {
        val sut = createSUT()
        sut.create(samples)
        every { denoiser.denoise(fftOutput) }
    }

}