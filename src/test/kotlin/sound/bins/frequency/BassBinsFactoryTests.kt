package sound.bins.frequency

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.filters.PassBandRegionFilter
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBins

class BassBinsFactoryTests {

    private val audioFrame = nextAudioFrame()

    private val frequencyBinsCalculator: FrequencyBinsCalculator = mockk()
    private val frequencyBins = nextFrequencyBins()
    private val passBandRegionFilter: PassBandRegionFilter = mockk()
    private val bassBins = nextFrequencyBins()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()


    @BeforeEach
    fun setupHappyPath() {
        every { frequencyBinsCalculator.calculate(audioFrame.samples, audioFrame.format) } returns frequencyBins
        every { passBandRegionFilter.filter(frequencyBins, lowCrossover.stopFrequency, highCrossover.stopFrequency) } returns bassBins
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): BassBinsFactory {
        return BassBinsFactory(
            frequencyBinsCalculator = frequencyBinsCalculator,
            passBandRegionFilter = passBandRegionFilter,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

    @Test
    fun `get the bass bins for an audio frame`() {
        val sut = createSUT()

        val actual = sut.create(audioFrame)

        assertEquals(bassBins, actual)
    }

}
