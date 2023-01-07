package sound.signalProcessing

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextDoubleArray
import kotlin.random.Random

class SignalProcessorTests {

    private val sampleExtractor: SampleExtractorInterface = mockk()
    private val interpolator: InterpolatorInterface = mockk()
    private var hannFilter: HannFilterInterface = mockk()
    private val hannFilterCorrector: HannFilterCorrectorInterface = mockk()
    private var fftAlgorithm: FftAlgorithmInterface = mockk()

    private val audioSignal = nextAudioSignal()
    private val lowestFrequency = Random.nextFloat()

    private val extractedSamples = nextDoubleArray()
    private val interpolatedSamples = nextDoubleArray()
    private val filteredSamples = nextDoubleArray()
    private val correctedSamples = nextDoubleArray()
    private val magnitudes = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { sampleExtractor.extract(any(), any()) } returns extractedSamples
        every { interpolator.interpolate(any(), any()) } returns interpolatedSamples
        every { hannFilter.filter(any()) } returns filteredSamples
        every { hannFilterCorrector.correct(any()) } returns correctedSamples
        every { fftAlgorithm.calculateMagnitudes(any()) } returns magnitudes
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SignalProcessor {
        return SignalProcessor(
            sampleExtractor = sampleExtractor,
            interpolator = interpolator,
            hannFilter = hannFilter,
            hannFilterCorrector = hannFilterCorrector,
            fftAlgorithm = fftAlgorithm
        )
    }

    @Test
    fun `the sample size is reduced to increase responsiveness`() {
        val sut = createSUT()
        sut.process(audioSignal, lowestFrequency)
        verify { sampleExtractor.extract(audioSignal, lowestFrequency) }
    }

    @Test
    fun `the extracted samples are interpolated to increase frequency resolution`() {
        val sut = createSUT()
        sut.process(audioSignal, lowestFrequency)
        verify { interpolator.interpolate(extractedSamples, audioSignal.sampleRate.toInt()) }
    }

    @Test
    fun `the interpolated samples go through a hann filter to prevent smearing`() {
        val sut = createSUT()
        sut.process(audioSignal, lowestFrequency)
        verify { hannFilter.filter(interpolatedSamples) }
    }

    @Test
    fun `the filtered samples have their amplitudes corrected`() {
        val sut = createSUT()
        sut.process(audioSignal, lowestFrequency)
        verify { hannFilterCorrector.correct(filteredSamples) }
    }

    @Test
    fun `the corrected samples are processed by FFT to identify the magnitudes of frequencies present`() {
        val sut = createSUT()
        sut.process(audioSignal, lowestFrequency)
        verify { fftAlgorithm.calculateMagnitudes(correctedSamples) }
    }

}