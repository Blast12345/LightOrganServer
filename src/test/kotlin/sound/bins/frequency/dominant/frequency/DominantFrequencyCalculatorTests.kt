package sound.bins.frequency.dominant.frequency

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBinList
import kotlin.random.Random

class DominantFrequencyCalculatorTests {

    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = mockk()
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculator = mockk()
    private val totalMagnitudeCalculator: TotalMagnitudeCalculator = mockk()

    private val frequencyBins = nextFrequencyBinList()

    private val peakFrequencyBins = nextFrequencyBinList()
    private val weightedMagnitude = Random.nextFloat()
    private val totalMagnitude = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { peakFrequencyBinsFinder.find(any()) } returns peakFrequencyBins
        every { weightedMagnitudeCalculator.calculate(any()) } returns weightedMagnitude
        every { totalMagnitudeCalculator.calculate(any()) } returns totalMagnitude
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantFrequencyCalculator {
        return DominantFrequencyCalculator(
            peakFrequencyBinsFinder = peakFrequencyBinsFinder,
            weightedMagnitudeCalculator = weightedMagnitudeCalculator,
            totalMagnitudeCalculator = totalMagnitudeCalculator
        )
    }

    @Test
    fun `return the weighted average of the peak frequencies`() {
        val sut = createSUT()
        every { weightedMagnitudeCalculator.calculate(peakFrequencyBins) } returns 20F
        every { totalMagnitudeCalculator.calculate(peakFrequencyBins) } returns 5F
        val actual = sut.calculate(frequencyBins)
        assertEquals(4F, actual)
    }

    @Test
    fun `return null the total magnitude is zero`() {
        val sut = createSUT()
        every { weightedMagnitudeCalculator.calculate(peakFrequencyBins) } returns 20F
        every { totalMagnitudeCalculator.calculate(peakFrequencyBins) } returns 0F
        val actual = sut.calculate(frequencyBins)
        assertNull(actual)
    }

}