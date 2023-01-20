package sound.frequencyBins.dominantFrequency.frequency

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.finders.FrequencyBinFinder
import toolkit.monkeyTest.nextFrequencyBins

class DominantFrequencyCalculatorTests {

    private val frequencyBinFinder: FrequencyBinFinder = mockk()

    private val frequencyBins = nextFrequencyBins()

    private val peakBin1 = FrequencyBin(10F, 2F)
    private val peakBin2 = FrequencyBin(20F, 8F)
    private val peakFrequencyBins = listOf(peakBin1, peakBin2)

    @BeforeEach
    fun setup() {
        every { frequencyBinFinder.findPeaks(any()) } returns peakFrequencyBins
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantFrequencyCalculator {
        return DominantFrequencyCalculator(
            frequencyBinFinder = frequencyBinFinder
        )
    }

    @Test
    fun `return the weighted average of the peak frequencies`() {
        val sut = createSUT()
        val actual = sut.calculate(frequencyBins)
        assertEquals(18F, actual)
        verify { frequencyBinFinder.findPeaks(frequencyBins) }
    }

    @Test
    fun `return null the total magnitude is zero`() {
        val sut = createSUT()
        every { frequencyBinFinder.findPeaks(any()) } returns peakFrequencyBins
        val quietBin = FrequencyBin(10F, 0F)
        val frequencyBins = listOf(quietBin)
        val actual = sut.calculate(frequencyBins)
        assertNull(actual)
    }

}