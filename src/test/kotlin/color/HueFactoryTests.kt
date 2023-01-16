package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class HueFactoryTests {

    private val colorWheel = ColorWheel(40F, 120F, 0.25F)

    private fun createSUT(): HueFactory {
        return HueFactory(colorWheel)
    }

    @Test
    fun `return the dominant frequency's position in the color wheel`() {
        val sut = createSUT()
        val dominantFrequencyBin = FrequencyBin(60F, 0F)
        val hue = sut.create(dominantFrequencyBin)
        assertEquals(0.5F, hue)
    }

}