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
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBin

class FrequencyBinListFactoryTests {

    private var signalProcessor: SignalProcessorInterface = mockk()
    private var fftAlgorithm: FftAlgorithmInterface = mockk()
    private var magnitudeNormalizer: MagnitudeNormalizerInterface = mockk()
    private var frequencyBinFactory: FrequencyBinFactoryInterface = mockk()

    private val samples = nextDoubleArray(length = 4)
    private val sampleRate = 8F
    private val audioSignal = AudioSignal(samples, sampleRate)

    private val processedSamples = nextDoubleArray()
    private val magnitudes = nextDoubleArray()
    private val normalizedMagnitudes = doubleArrayOf(1.0, 2.0)
    private val frequencyBin = nextFrequencyBin()
    private val bin1: FrequencyBin = mockk()
    private val bin2: FrequencyBin = mockk()

    @BeforeEach
    fun setup() {
        every { signalProcessor.process(any(), any()) } returns processedSamples
        every { fftAlgorithm.calculateMagnitudes(any()) } returns magnitudes
        every { magnitudeNormalizer.normalize(any(), any()) } returns normalizedMagnitudes
        every { frequencyBinFactory.create(any(), any(), any()) } returns frequencyBin
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinListFactory {
        return FrequencyBinListFactory(
            signalProcessor = signalProcessor,
            fftAlgorithm = fftAlgorithm,
            magnitudeNormalizer = magnitudeNormalizer,
            frequencyBinFactory = frequencyBinFactory
        )
    }

    @Test
    fun `the audio signal is processed`() {
        val sut = createSUT()
        sut.create(audioSignal)
        verify { signalProcessor.process(audioSignal, any()) }
    }

    @Test
    fun `the lowest supported bin is 20hz`() {
        val sut = createSUT()
        sut.create(audioSignal)
        verify { signalProcessor.process(audioSignal, 20F) }
    }

    @Test
    fun `the processed signal is has its magnitudes calculated by FFT`() {
        val sut = createSUT()
        sut.create(audioSignal)
        verify { fftAlgorithm.calculateMagnitudes(processedSamples) }
    }

    @Test
    fun `the magnitudes are normalized`() {
        val sut = createSUT()
        sut.create(audioSignal)
        verify { magnitudeNormalizer.normalize(magnitudes, processedSamples.size) }
    }

    @Test
    fun `a frequency bin is created for each normalized magnitude`() {
        val sut = createSUT()
        every { frequencyBinFactory.create(0, 0.5F, 1.0) } returns bin1
        every { frequencyBinFactory.create(1, 0.5F, 2.0) } returns bin2
        val frequencyBins = sut.create(audioSignal)
        val expectedFrequencyBins = listOf(bin1, bin2)
        assertEquals(expectedFrequencyBins, frequencyBins)
    }

}