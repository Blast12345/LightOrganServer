package sound.frequencyBins.dominant

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.dominant.frequency.DominantFrequencyCalculator
import sound.frequencyBins.dominant.magnitude.DominantMagnitudeCalculator
import toolkit.monkeyTest.nextFrequencyBinList
import kotlin.random.Random

class DominantFrequencyBinCalculatorTests {

    private val frequencyBins = nextFrequencyBinList()
    private var dominantFrequencyCalculator: DominantFrequencyCalculator = mockk()
    private val dominantFrequency = Random.nextFloat()
    private var dominantMagnitudeCalculator: DominantMagnitudeCalculator = mockk()
    private val estimatedMagnitude = Random.nextFloat()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantFrequencyBinCalculator {
        return DominantFrequencyBinCalculator(
            dominantFrequencyCalculator = dominantFrequencyCalculator,
            dominantMagnitudeCalculator = dominantMagnitudeCalculator
        )
    }

    @Test
    fun `calculate the dominant frequency bin`() {
        val sut = createSUT()
        every { dominantFrequencyCalculator.calculate(frequencyBins) } returns dominantFrequency
        every { dominantMagnitudeCalculator.calculate(frequencyBins) } returns estimatedMagnitude

        val frequencyBin = sut.calculate(frequencyBins)

        val expected = FrequencyBin(dominantFrequency, estimatedMagnitude)
        assertEquals(expected, frequencyBin)
    }

    @Test
    fun `return null when there is no dominant frequency`() {
        val sut = createSUT()
        every { dominantFrequencyCalculator.calculate(any()) } returns null

        val frequencyBin = sut.calculate(frequencyBins)

        Assertions.assertNull(frequencyBin)
    }

    @Test
    fun `return null when there is no estimated magnitude`() {
        val sut = createSUT()
        every { dominantFrequencyCalculator.calculate(any()) } returns null

        val frequencyBin = sut.calculate(frequencyBins)

        Assertions.assertNull(frequencyBin)
    }

}