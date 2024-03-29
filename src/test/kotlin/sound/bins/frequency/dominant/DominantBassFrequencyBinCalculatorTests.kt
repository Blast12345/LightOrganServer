package sound.bins.frequency.dominant

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.dominant.frequency.DominantFrequencyCalculator
import sound.bins.frequency.dominant.magnitude.DominantMagnitudeCalculator
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.PassBandRegionFilter
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class DominantBassFrequencyBinCalculatorTests {

    private val frequencyBins = nextFrequencyBins()
    private val passBandRegionFilter: PassBandRegionFilter = mockk()
    private val passBandRegionBins = nextFrequencyBins()
    private val bandPassFilter: BandPassFilter = mockk()
    private val bandPassedBins = nextFrequencyBins()
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = mockk()
    private val dominantFrequency = Random.nextFloat()
    private val dominantMagnitudeCalculator: DominantMagnitudeCalculator = mockk()
    private val estimatedMagnitude = Random.nextFloat()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()

    @BeforeEach
    fun setupHappyPath() {
        every { passBandRegionFilter.filter(frequencyBins, lowCrossover.stopFrequency, highCrossover.stopFrequency) } returns passBandRegionBins
        every { bandPassFilter.filter(passBandRegionBins, lowCrossover, highCrossover) } returns bandPassedBins
        every { dominantFrequencyCalculator.calculate(passBandRegionBins) } returns dominantFrequency
        every { dominantMagnitudeCalculator.calculate(bandPassedBins) } returns estimatedMagnitude
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantBassFrequencyBinCalculator {
        return DominantBassFrequencyBinCalculator(
            passBandRegionFilter = passBandRegionFilter,
            bandPassFilter = bandPassFilter,
            dominantFrequencyCalculator = dominantFrequencyCalculator,
            dominantMagnitudeCalculator = dominantMagnitudeCalculator,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
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
