package sound.frequencyBins

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.AudioSignal
import sound.signalProcessing.FftAlgorithmInterface
import sound.signalProcessing.MagnitudeNormalizerInterface
import sound.signalProcessing.SignalProcessorInterface
import toolkit.generators.SineWaveGenerator
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class FrequencyBinsServiceTests {

    private var signalProcessor: SignalProcessorInterface = mockk()
    private var fftAlgorithm: FftAlgorithmInterface = mockk()
    private var magnitudeNormalizer: MagnitudeNormalizerInterface = mockk()
    private var granularityCalculator: GranularityCalculatorInterface = mockk()
    private var frequencyBinListFactory: FrequencyBinListFactoryInterface = mockk()
    private var supportedFrequencyBinsFilter: SupportedFrequencyBinsFilterInterface = mockk()

    private val audioSignal = nextAudioSignal()

    private val processedSamples = nextDoubleArray()
    private val magnitudes = nextDoubleArray()
    private val normalizedMagnitudes = nextDoubleArray()
    private val granularity = Random.nextFloat()
    private val allBins = nextFrequencyBins()
    private val supportedBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { signalProcessor.process(any(), any()) } returns processedSamples
        every { fftAlgorithm.calculateMagnitudes(any()) } returns magnitudes
        every { magnitudeNormalizer.normalize(any(), any()) } returns normalizedMagnitudes
        every { granularityCalculator.calculate(any(), any()) } returns granularity
        every { frequencyBinListFactory.create(any(), any()) } returns allBins
        every { supportedFrequencyBinsFilter.filter(any(), any()) } returns supportedBins
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsService {
        return FrequencyBinsService(
            signalProcessor = signalProcessor,
            fftAlgorithm = fftAlgorithm,
            magnitudeNormalizer = magnitudeNormalizer,
            granularityCalculator = granularityCalculator,
            frequencyBinListFactory = frequencyBinListFactory,
            supportedFrequencyBinsFilter = supportedFrequencyBinsFilter
        )
    }

    @Test
    fun `the audio signal is processed`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { signalProcessor.process(audioSignal, any()) }
    }

    @Test
    fun `the processed signal is has its magnitudes calculated by FFT`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { fftAlgorithm.calculateMagnitudes(processedSamples) }
    }

    @Test
    fun `the magnitudes are normalized`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { magnitudeNormalizer.normalize(magnitudes, processedSamples.size) }
    }

    @Test
    fun `the granularity of the bins is calculated`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { granularityCalculator.calculate(normalizedMagnitudes.size, audioSignal.sampleRate) }
    }

    @Test
    fun `frequency bins are created for the normalized magnitudes`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { frequencyBinListFactory.create(normalizedMagnitudes, granularity) }
    }

    @Test
    fun `the lowest supported bin is 20hz`() {
        val sut = createSUT()
        val frequencyBins = sut.getFrequencyBins(audioSignal)
        assertEquals(supportedBins, frequencyBins)
        verify { signalProcessor.process(audioSignal, 20F) }
        verify { supportedFrequencyBinsFilter.filter(allBins, 20F) }
    }

    @Test
    fun `some integration test`() {
        val sut = FrequencyBinsService()
        val samples = SineWaveGenerator(48000.0).generate(50, 48000)
        val audioSignal = AudioSignal(samples, 48000F)
        val frequencyBins = sut.getFrequencyBins(audioSignal)
        println("ASDF")
    }

}