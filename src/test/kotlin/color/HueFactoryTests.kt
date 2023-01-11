package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class HueFactoryTests {

    private val lowestFrequencyBin = FrequencyBin(50F, 0F)
    private val dominantFrequencyBin = FrequencyBin(75F, 0F)
    private val highestFrequencyBin = FrequencyBin(100F, 0F)

    private fun createSUT(): HueFactory {
        return HueFactory()
    }

    @Test
    fun `the hue is corresponds to the position of the dominant frequency within the provided range`() {
        val sut = createSUT()
        val hue = sut.create(dominantFrequencyBin, lowestFrequencyBin, highestFrequencyBin)
        assertEquals(0.5F, hue)
    }

}