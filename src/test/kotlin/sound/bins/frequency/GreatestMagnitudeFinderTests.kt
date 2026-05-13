package sound.bins.frequency

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin

class GreatestMagnitudeFinderTests {

    private val lowestMagnitudeBin = nextFrequencyBin(magnitude = 0.1)
    private val middleMagnitudeBin = nextFrequencyBin(magnitude = 0.3)
    private val highestMagnitudeBin = nextFrequencyBin(magnitude = 0.5)
    private val frequencyBins = listOf(lowestMagnitudeBin, middleMagnitudeBin, highestMagnitudeBin)

    private fun createSUT(): GreatestMagnitudeFinder {
        return GreatestMagnitudeFinder()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `get the greatest magnitude from a list of frequency bins`() {
        val sut = createSUT()

        val actual = sut.find(frequencyBins)

        assertEquals(highestMagnitudeBin.magnitude, actual)
    }


}
