package lightOrgan.sound.frequencyBins.filters

import config.Config
import config.children.HighPassFilter
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BassFrequencyBinsFilterTests {

    private var config: Config = mockk()

    private val bin1 = FrequencyBin(100F, 1F)
    private val bin2 = FrequencyBin(120F, 1F)
    private val bin3 = FrequencyBin(130F, 1F)
    private val bin4 = FrequencyBin(140F, 1F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4)

    private val highPassFilter: HighPassFilter = mockk()
    private val highPassFrequency: Float = 120F
    private val highPassRollOffRange: Float = 20F

    private val expectedBin1 = FrequencyBin(100F, 1F)
    private val expectedBin2 = FrequencyBin(120F, 1F)
    private val expectedBin3 = FrequencyBin(130F, 0.5F)
    private val expectedBin4 = FrequencyBin(140F, 0F)

    @BeforeEach
    fun setup() {
        every { config.highPassFilter } returns highPassFilter
        every { highPassFilter.frequency } returns highPassFrequency
        every { highPassFilter.rollOffRange } returns highPassRollOffRange
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): BassFrequencyBinsFilter {
        return BassFrequencyBinsFilter(
            config = config
        )
    }

    @Test
    fun `return frequency bins that roll off from the highpass frequency`() {
        val sut = createSUT()
        val actual = sut.filter(frequencyBins)
        val expected = listOf(expectedBin1, expectedBin2, expectedBin3, expectedBin4)
        assertEquals(expected, actual)
    }

}