package sound.bins.listCalculator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequencyBins.FrequencyBinListFactory
import sound.bins.frequencyBins.listCalculator.FrequencyBinListCalculator
import sound.bins.frequencyBins.listCalculator.GranularityCalculator
import sound.bins.frequencyBins.listCalculator.MagnitudeListCalculator
import toolkit.monkeyTest.nextAudioFormatWrapper
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBinList
import kotlin.random.Random

class FrequencyBinListCalculatorTests {

    private val samples = nextDoubleArray()
    private val nyquistFrequency = 100F
    private val audioFormat = nextAudioFormatWrapper(nyquistFrequency = nyquistFrequency)

    private val magnitudeListCalculator: MagnitudeListCalculator = mockk()
    private val magnitudeList = nextDoubleArray()
    private val granularityCalculator: GranularityCalculator = mockk()
    private val granularity = Random.nextFloat()
    private val frequencyBinListFactory: FrequencyBinListFactory = mockk()
    private val bin90 = nextFrequencyBin(90F)
    private val bin100 = nextFrequencyBin(100F)
    private val bin101 = nextFrequencyBin(101F)
    private val frequencyBinList = listOf(bin90, bin100, bin101)

    @BeforeEach
    fun setup() {
        every { magnitudeListCalculator.calculate(any()) } returns nextDoubleArray()
        every { granularityCalculator.calculate(any(), any()) } returns Random.nextFloat()
        every { frequencyBinListFactory.create(any(), any()) } returns nextFrequencyBinList()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinListCalculator {
        return FrequencyBinListCalculator(
            magnitudeListCalculator = magnitudeListCalculator,
            granularityCalculator = granularityCalculator,
            frequencyBinListFactory = frequencyBinListFactory
        )
    }

    @Test
    fun `calculate the frequency bins for a given audio frame`() {
        val sut = createSUT()
        every { magnitudeListCalculator.calculate(samples) } returns magnitudeList
        every { granularityCalculator.calculate(magnitudeList.size, audioFormat) } returns granularity
        every { frequencyBinListFactory.create(magnitudeList, granularity) } returns frequencyBinList

        val actual = sut.calculate(samples, audioFormat)

        val binsWithinNyquistFrequency = listOf(bin90, bin100)
        assertEquals(binsWithinNyquistFrequency, actual)
    }

}