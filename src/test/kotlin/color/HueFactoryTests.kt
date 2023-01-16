package color

import config.ColorWheel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class HueFactoryTests {

    private val colorWheel = ColorWheel(40F, 120F, 0.25F)

    private fun createSUT(): HueFactory {
        return HueFactory(colorWheel)
    }

    @Test
    fun `return the frequency's relative position in the color wheel`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(60F, 0F)
        val hue = sut.create(frequencyBin)
        assertEquals(0.5F, hue)
    }

}