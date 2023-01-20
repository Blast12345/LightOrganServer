package sound.frequencyBins

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.fft.RelativeMagnitudesCalculatorInterface
import sound.frequencyBins.filters.FrequencyBinListDenoiserInterface
import sound.input.samples.AudioSignal
import sound.signalProcessing.SignalProcessorInterface
import toolkit.generators.SineWaveGenerator
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class FrequencyBinsServiceTests {

    private var config: Config = mockk()
    private var signalProcessor: SignalProcessorInterface = mockk()
    private var relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = mockk()
    private var granularityCalculator: GranularityCalculatorInterface = mockk()
    private var frequencyBinListFactory: FrequencyBinListFactoryInterface = mockk()
    private val frequencyBinListDenoiser: FrequencyBinListDenoiserInterface = mockk()

    private val audioSignal = nextAudioSignal()

    private val processedSamples = nextDoubleArray()
    private val magnitudes = nextDoubleArray()
    private val granularity = Random.nextFloat()
    private val frequencyBinList = nextFrequencyBins()
    private val denoisedFrequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { signalProcessor.process(any()) } returns processedSamples
        every { relativeMagnitudesCalculator.calculate(any()) } returns magnitudes
        every { granularityCalculator.calculate(any(), any()) } returns granularity
        every { frequencyBinListFactory.create(any(), any()) } returns frequencyBinList
        every { frequencyBinListDenoiser.denoise(any()) } returns denoisedFrequencyBins
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsService {
        return FrequencyBinsService(
            config = config,
            signalProcessor = signalProcessor,
            relativeMagnitudesCalculator = relativeMagnitudesCalculator,
            granularityCalculator = granularityCalculator,
            frequencyBinListFactory = frequencyBinListFactory,
            frequencyBinListDenoiser = frequencyBinListDenoiser
        )
    }

    @Test
    fun `the audio signal is processed`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { signalProcessor.process(audioSignal) }
    }

    @Test
    fun `the processed signal is has its magnitudes calculated`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { relativeMagnitudesCalculator.calculate(processedSamples) }
    }

    @Test
    fun `the granularity of the bins is calculated`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { granularityCalculator.calculate(magnitudes.size, audioSignal.sampleRate) }
    }

    @Test
    fun `frequency bins are created for the magnitudes`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioSignal)
        verify { frequencyBinListFactory.create(magnitudes, granularity) }
    }

    @Test
    fun `the supported bins are denoised`() {
        val sut = createSUT()
        val frequencyBins = sut.getFrequencyBins(audioSignal)
        assertEquals(denoisedFrequencyBins, frequencyBins)
        verify { frequencyBinListDenoiser.denoise(frequencyBinList) }
    }

    @Test
    fun `a 50hz signal produces an amplitude of 1 in a 50hz bin`() {
        // NOTE: This is an integration test
        every { config.interpolatedSampleSize } returns 48000
        every { config.sampleSize } returns 4096
        every { config.audioFormat.channels } returns 1
        every { config.magnitudeMultiplier } returns 1F
        val sut = FrequencyBinsService(config)

        val fiftyHertzSignal = createAudioSignal(50F)
        val frequencyBins = sut.getFrequencyBins(fiftyHertzSignal)

        val fiftyHertzBin = frequencyBins.first { it.frequency == 50F }
        assertEquals(1F, fiftyHertzBin.magnitude, 0.001F)
    }

    private fun createAudioSignal(frequency: Float): AudioSignal {
        val samples = SineWaveGenerator(48000F).generate(frequency, 48000)
        return AudioSignal(samples, 48000F)
    }

}