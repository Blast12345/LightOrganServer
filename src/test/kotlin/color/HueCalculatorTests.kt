package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.octave.OctaveWeightedAverageCalculator
import sound.octave.PeakOctaveBinsFinder
import toolkit.monkeyTest.nextFrequencyBinList
import toolkit.monkeyTest.nextOctaveBinList
import kotlin.random.Random

class HueCalculatorTests {

    private val frequencyBins = nextFrequencyBinList()

    private val peakOctaveBinsFinder: PeakOctaveBinsFinder = mockk()
    private val peakOctaveBins = nextOctaveBinList()
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = mockk()
    private val octaveWeightedAverage = Random.nextFloat()

    private fun createSUT(): HueCalculator {
        return HueCalculator(
            peakOctaveBinsFinder = peakOctaveBinsFinder,
            octaveWeightedAverageCalculator = octaveWeightedAverageCalculator
        )
    }

    @AfterEach
    fun teardown() {
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