package sound.frequencyBins.filters

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin

class PassBandRegionFilterTests {

    private val frequencyBin49 = nextFrequencyBin(frequency = 49F)
    private val frequencyBin50 = nextFrequencyBin(frequency = 50F)
    private val frequencyBin75 = nextFrequencyBin(frequency = 75F)
    private val frequencyBin100 = nextFrequencyBin(frequency = 100F)
    private val frequencyBin101 = nextFrequencyBin(frequency = 101F)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): PassBandRegionFilter {
        return PassBandRegionFilter()
    }

    @Test
    fun `get the frequency bins in the pass-band region`() {
        val sut = createSUT()

        val actual = sut.filter(
            frequencyBinList = listOf(frequencyBin49, frequencyBin50, frequencyBin75, frequencyBin100, frequencyBin101),
            lowStopFrequency = 50F,
            highStopFrequency = 100F
        )

        val expectedBins = listOf(frequencyBin50, frequencyBin75, frequencyBin100)
        assertEquals(expectedBins, actual)
    }
}