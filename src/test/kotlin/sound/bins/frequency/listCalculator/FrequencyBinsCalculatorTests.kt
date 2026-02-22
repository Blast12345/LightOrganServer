package sound.bins.frequency.listCalculator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.FrequencyBinsCalculator
import sound.bins.frequency.FrequencyBinFactory
import sound.signalProcessor.SignalProcessor
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

class FrequencyBinsCalculatorTests {

    private val signalProcessor: SignalProcessor = mockk()
    private val magnitudeListCalculator: MagnitudeListCalculator = mockk()
    private val granularityCalculator: GranularityCalculator = mockk()
    private val frequencyBinFactory: FrequencyBinFactory = mockk()

    private val audioFrame = nextAudioFrame(200)
    private val samplesAsDoubles = audioFrame.samples.map { it.toDouble() }.toDoubleArray()
    private val processedSamples = nextDoubleArray()
    private val magnitudeList = nextDoubleArray(3)
    private val granularity = Random.nextFloat()

    private val bin90 = nextFrequencyBin(90F)
    private val bin100 = nextFrequencyBin(100F)
    private val bin101 = nextFrequencyBin(101F)

    @BeforeEach
    fun setupHappyPath() {
        every { signalProcessor.process(samplesAsDoubles) } returns processedSamples
        every { magnitudeListCalculator.calculate(processedSamples) } returns magnitudeList
        every { granularityCalculator.calculate(magnitudeList.size, audioFrame.format) } returns granularity

        every { frequencyBinFactory.create(0, granularity, magnitudeList[0]) } returns bin90
        every { frequencyBinFactory.create(1, granularity, magnitudeList[1]) } returns bin100
        every { frequencyBinFactory.create(2, granularity, magnitudeList[2]) } returns bin101
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsCalculator {
        return FrequencyBinsCalculator(
            signalProcessor = signalProcessor,
            magnitudeListCalculator = magnitudeListCalculator,
            granularityCalculator = granularityCalculator,
            frequencyBinFactory = frequencyBinFactory
        )
    }

    // TODO: Redo tests with FrequencyBinsCalculator refactor
    @Test
    fun `calculate the frequency bins for a given audio frame`() {
        val sut = createSUT()

        val actual = sut.calculate(audioFrame)

        val binsWithinNyquistFrequency = listOf(bin90, bin100)
        assertEquals(binsWithinNyquistFrequency, actual)
    }

}
