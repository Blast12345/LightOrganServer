package sound.frequencyBins.bass

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.filters.BandPassFilter
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFrequencyBinList

class BassFrequencyBinListCalculatorTests {

    private val audioFrame = nextAudioFrame()
    private val frequencyBinListCalculator: FrequencyBinListCalculator = mockk()
    private val frequencyBinList = nextFrequencyBinList()
    private val bandPassFilter: BandPassFilter = mockk()
    private val filteredFrequencyBinList = nextFrequencyBinList()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): BassFrequencyBinListCalculator {
        return BassFrequencyBinListCalculator(
            frequencyBinListCalculator = frequencyBinListCalculator,
            bandPassFilter = bandPassFilter
        )
    }

    @Test
    fun `get the bass frequency bins for an audio frame`() {
        val sut = createSUT()
        every { frequencyBinListCalculator.calculate(audioFrame) } returns frequencyBinList
        every { bandPassFilter.filter(frequencyBinList) } returns filteredFrequencyBinList

        val actual = sut.calculate(audioFrame)

        assertEquals(filteredFrequencyBinList, actual)
    }
}