package sound.frequencyBins

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.fft.RelativeMagnitudesCalculator
import sound.frequencyBins.filters.FrequencyBinListDenoiser
import sound.signalProcessing.SignalProcessor
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class FrequencyBinsServiceTests {

    private val signalProcessor: SignalProcessor = mockk()
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculator = mockk()
    private val granularityCalculator: GranularityCalculator = mockk()
    private val frequencyBinListFactory: FrequencyBinListFactory = mockk()
    private val frequencyBinListDenoiser: FrequencyBinListDenoiser = mockk()

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

}