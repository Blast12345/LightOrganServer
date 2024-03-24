package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import organize.HardBandpassFilter
import organize.SampleTrimmer
import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator
import sound.bins.octave.OctaveWeightedAverageCalculator
import sound.notes.Notes
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextOctaveBins
import kotlin.random.Random

class HueCalculatorTests {

    private val audioFrame = nextAudioFrame()
    private val sampleSize = 8820 // TODO:
    private val lowCrossover = Crossover(
        stopFrequency = Notes.C.getFrequency(0),
        cornerFrequency = Notes.C.getFrequency(1)
    )
    private val highCrossover = Crossover(
        cornerFrequency = Notes.C.getFrequency(2),
        stopFrequency = Notes.C.getFrequency(3)
    )

    private val sampleTrimmer: SampleTrimmer = mockk()
    private val trimmedSamples = nextDoubleArray()
    private val frequencyBinsCalculator: FrequencyBinsCalculator = mockk()
    private val frequencyBins = nextFrequencyBins()
    private val hardBandpassFilter: HardBandpassFilter = mockk()
    private val filteredFrequencyBins = nextFrequencyBins()
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = mockk()
    private val peakFrequencyBins = nextFrequencyBins()
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = mockk()
    private val peakOctaveBins = nextOctaveBins()
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = mockk()
    private val octaveWeightedAverage = Random.nextFloat()

    private fun createSUT(): HueCalculator {
        return HueCalculator(
            sampleTrimmer = sampleTrimmer,
            frequencyBinsCalculator = frequencyBinsCalculator,
            hardBandpassFilter = hardBandpassFilter,
            peakFrequencyBinsFinder = peakFrequencyBinsFinder,
            frequencyBinsToOctaveBinsConverter = frequencyBinsToOctaveBinsConverter,
            octaveWeightedAverageCalculator = octaveWeightedAverageCalculator,
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `calculate the hue`() {
        val sut = createSUT()
        every { sampleTrimmer.trim(audioFrame.samples, sampleSize) } returns trimmedSamples
        every { frequencyBinsCalculator.calculate(trimmedSamples, audioFrame.format) } returns frequencyBins
        every { hardBandpassFilter.filter(frequencyBins, lowCrossover, highCrossover) } returns filteredFrequencyBins
        every { peakFrequencyBinsFinder.find(filteredFrequencyBins) } returns peakFrequencyBins
        every { frequencyBinsToOctaveBinsConverter.convert(peakFrequencyBins) } returns peakOctaveBins
        every { octaveWeightedAverageCalculator.calculate(peakOctaveBins) } returns octaveWeightedAverage

        val actual = sut.calculate(audioFrame)

        assertEquals(octaveWeightedAverage, actual)
    }

}
