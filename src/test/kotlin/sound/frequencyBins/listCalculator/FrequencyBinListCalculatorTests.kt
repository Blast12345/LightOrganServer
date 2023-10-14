package sound.frequencyBins.listCalculator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinListFactory
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBinList
import kotlin.random.Random

class FrequencyBinListCalculatorTests {

    private val audioFrame = nextAudioFrame()
    private val magnitudeListCalculator: MagnitudeListCalculator = mockk()
    private val magnitudeList = nextDoubleArray()
    private val granularityCalculator: GranularityCalculator = mockk()
    private val granularity = Random.nextFloat()
    private val frequencyBinListFactory: FrequencyBinListFactory = mockk()
    private val frequencyBinList = nextFrequencyBinList()

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
        every { magnitudeListCalculator.calculate(audioFrame.samples) } returns magnitudeList
        every { granularityCalculator.calculate(magnitudeList.size, audioFrame.format) } returns granularity
        every { frequencyBinListFactory.create(magnitudeList, granularity) } returns frequencyBinList

        val actual = sut.calculate(audioFrame)

        assertEquals(frequencyBinList, actual)
    }

}