package sound.bins.frequency

import io.mockk.clearAllMocks
import org.apache.commons.math3.complex.Complex
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin

class GreatestMagnitudeFinderTests {

    private val lowestMagnitudeBin = nextFrequencyBin(value = Complex(0.1, 0.0))
    private val middleMagnitudeBin = nextFrequencyBin(value = Complex(0.3, 0.0))
    private val highestMagnitudeBin = nextFrequencyBin(value = Complex(0.5, 0.0))
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
