package sound.frequencyBins

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FrequencyBinFactoryTests {

    private val config: Config = mockk()

    private val index = 2
    private val granularity = 5F
    private val magnitude = 7.0

    private val magnitudeMultiplier = 2F
    private val numberOfChannels = 2

    @BeforeEach
    fun setup() {
        every { config.magnitudeMultiplier } returns magnitudeMultiplier
        every { config.audioFormat.channels } returns numberOfChannels
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinFactory {
        return FrequencyBinFactory(
            config = config
        )
    }

    @Test
    fun `create a frequency bin`() {
        val sut = createSUT()
        val frequencyBin = sut.create(index, granularity, magnitude)
        val expected = FrequencyBin(20F, 14F)
        assertEquals(expected, frequencyBin)
    }

}