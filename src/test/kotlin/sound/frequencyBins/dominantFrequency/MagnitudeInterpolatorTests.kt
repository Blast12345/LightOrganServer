package sound.frequencyBins.dominantFrequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class MagnitudeInterpolatorTests {

    private val binA = FrequencyBin(0F, 1F)
    private val binB = FrequencyBin(10F, 2F)

    private fun createSUT(): MagnitudeInterpolator {
        return MagnitudeInterpolator()
    }

    @Test
    fun `interpolate the magnitude of a frequency between two points`() {
        val sut = createSUT()
        val actual = sut.interpolate(2F, binA, binB)
        assertEquals(1.2F, actual)
    }

    @Test
    fun `interpolate the magnitude of a frequency between two points when they are flipped`() {
        val sut = createSUT()
        val actual = sut.interpolate(3F, binB, binA)
        assertEquals(1.3F, actual)
    }

}