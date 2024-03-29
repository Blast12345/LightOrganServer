package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.octave.OctaveWeightedAverageCalculator
import sound.bins.octave.PeakOctaveBinsFinder
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextOctaveBins
import kotlin.random.Random

class HueCalculatorTests {

    private val frequencyBins = nextFrequencyBins()

    private val peakOctaveBinsFinder: PeakOctaveBinsFinder = mockk()
    private val peakOctaveBins = nextOctaveBins()
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = mockk()
    private val octaveWeightedAverage = Random.nextFloat()

    private fun createSUT(): HueCalculator {
        return HueCalculator(
            peakOctaveBinsFinder = peakOctaveBinsFinder,
            octaveWeightedAverageCalculator = octaveWeightedAverageCalculator
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `calculate the hue`() {
        val sut = createSUT()
        every { peakOctaveBinsFinder.find(frequencyBins) } returns peakOctaveBins
        every { octaveWeightedAverageCalculator.calculate(peakOctaveBins) } returns octaveWeightedAverage

        val actual = sut.calculate(frequencyBins)

        assertEquals(octaveWeightedAverage, actual)
    }

}
