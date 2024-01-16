package sound.bins.frequency.listCalculator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBinsFactory
import toolkit.monkeyTest.nextAudioFormatWrapper
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

class FrequencyBinsCalculatorTests {

    private val samples = nextDoubleArray()
    private val nyquistFrequency = 100F
    private val audioFormat = nextAudioFormatWrapper(nyquistFrequency = nyquistFrequency)

    private val magnitudeListCalculator: MagnitudeListCalculator = mockk()
    private val magnitudeList = nextDoubleArray()
    private val granularityCalculator: GranularityCalculator = mockk()
    private val granularity = Random.nextFloat()
    private val frequencyBinsFactory: FrequencyBinsFactory = mockk()
    private val bin90 = nextFrequencyBin(90F)
    private val bin100 = nextFrequencyBin(100F)
    private val bin101 = nextFrequencyBin(101F)
    private val frequencyBins = listOf(bin90, bin100, bin101)

    @BeforeEach
    fun setupHappyPath() {
        every { magnitudeListCalculator.calculate(samples) } returns magnitudeList
        every { granularityCalculator.calculate(magnitudeList.size, audioFormat) } returns granularity
        every { frequencyBinsFactory.create(magnitudeList, granularity) } returns frequencyBins
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsCalculator {
        return FrequencyBinsCalculator(
            magnitudeListCalculator = magnitudeListCalculator,
            granularityCalculator = granularityCalculator,
            frequencyBinsFactory = frequencyBinsFactory
        )
    }

    @Test
    fun `calculate the frequency bins for a given audio frame`() {
        val sut = createSUT()

        val actual = sut.calculate(samples, audioFormat)

        val binsWithinNyquistFrequency = listOf(bin90, bin100)
        assertEquals(binsWithinNyquistFrequency, actual)
    }

}
