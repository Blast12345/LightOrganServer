package sound.bins.frequency.dominant

import dsp.fft.FrequencyBin
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.dominant.frequency.DominantFrequencyCalculator
import sound.bins.frequency.dominant.magnitude.DominantMagnitudeCalculator
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class DominantBassFrequencyBinCalculatorTests {

    private val frequencyBins = nextFrequencyBins()
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = mockk()
    private val dominantFrequency = Random.nextFloat()
    private val dominantMagnitudeCalculator: DominantMagnitudeCalculator = mockk()
    private val estimatedMagnitude = Random.nextFloat()

    @BeforeEach
    fun setupHappyPath() {
        every { dominantFrequencyCalculator.calculate(frequencyBins) } returns dominantFrequency
        every { dominantMagnitudeCalculator.calculate(frequencyBins) } returns estimatedMagnitude
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantBassFrequencyBinCalculator {
        return DominantBassFrequencyBinCalculator(
            dominantFrequencyCalculator = dominantFrequencyCalculator,
            dominantMagnitudeCalculator = dominantMagnitudeCalculator,
        )
    }

    @Test
    fun `calculate the dominant frequency bin`() {
        val sut = createSUT()

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
