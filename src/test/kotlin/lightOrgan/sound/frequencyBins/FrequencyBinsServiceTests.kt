package sound.frequencyBins

import config.ConfigSingleton
import config.TestConfig
import input.audioFrame.AudioFrame
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lightOrgan.sound.fft.RelativeMagnitudesCalculatorInterface
import lightOrgan.sound.frequencyBins.FrequencyBinListFactoryInterface
import lightOrgan.sound.frequencyBins.GranularityCalculatorInterface
import lightOrgan.sound.frequencyBins.filters.FrequencyBinListDenoiserInterface
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.signalProcessing.SignalProcessorInterface
import toolkit.generators.SineWaveGenerator
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBins
import wrappers.audioFormat.AudioFormatWrapper
import kotlin.random.Random

class FrequencyBinsServiceTests {

    private var signalProcessor: SignalProcessorInterface = mockk()
    private var relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = mockk()
    private var granularityCalculator: GranularityCalculatorInterface = mockk()
    private var frequencyBinListFactory: FrequencyBinListFactoryInterface = mockk()
    private val frequencyBinListDenoiser: FrequencyBinListDenoiserInterface = mockk()

    private val audioFrame = nextAudioFrame()

    private val processedSamples = nextDoubleArray()
    private val magnitudes = nextDoubleArray()
    private val granularity = Random.nextFloat()
    private val frequencyBinList = nextFrequencyBins()
    private val denoisedFrequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { signalProcessor.process(any()) } returns processedSamples
        every { relativeMagnitudesCalculator.calculate(any()) } returns magnitudes
        every { granularityCalculator.calculate(any(), any(), any()) } returns granularity
        every { frequencyBinListFactory.create(any(), any()) } returns frequencyBinList
        every { frequencyBinListDenoiser.denoise(any()) } returns denoisedFrequencyBins
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsService {
        return FrequencyBinsService(
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
        sut.getFrequencyBins(audioFrame)
        verify { signalProcessor.process(audioFrame) }
    }

    @Test
    fun `the processed signal is has its magnitudes calculated`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioFrame)
        verify { relativeMagnitudesCalculator.calculate(processedSamples) }
    }

    @Test
    fun `the granularity of the bins is calculated`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioFrame)
        verify {
            granularityCalculator.calculate(
                magnitudes.size,
                audioFrame.format.sampleRate,
                audioFrame.format.numberOfChannels
            )
        }
    }

    @Test
    fun `frequency bins are created for the magnitudes`() {
        val sut = createSUT()
        sut.getFrequencyBins(audioFrame)
        verify { frequencyBinListFactory.create(magnitudes, granularity) }
    }

    @Test
    fun `the supported bins are denoised`() {
        val sut = createSUT()
        val frequencyBins = sut.getFrequencyBins(audioFrame)
        assertEquals(denoisedFrequencyBins, frequencyBins)
        verify { frequencyBinListDenoiser.denoise(frequencyBinList) }
    }

    @Test
    // NOTE: This is an integration test
    fun `a 50hz signal produces an amplitude of 1 in a 50hz bin`() {
        // The singleton feels a smelly, but passing the config through every class is burdensome.
        // TODO: Maybe use dependency injection?
        ConfigSingleton = TestConfig

        val sut = FrequencyBinsService()

        val fiftyHertzSignal = createAudioFrame(50F)
        val frequencyBins = sut.getFrequencyBins(fiftyHertzSignal)

        val fiftyHertzBin = frequencyBins.first { it.frequency == 50F }
        assertEquals(1F, fiftyHertzBin.magnitude, 0.001F)
    }

    private fun createAudioFrame(frequency: Float): AudioFrame {
        val audioFormat = AudioFormatWrapper(48000F, 1)
        val samples = SineWaveGenerator(48000F).generate(frequency, 48000)
        return AudioFrame(samples, audioFormat)
    }

}