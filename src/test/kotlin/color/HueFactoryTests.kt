package color

import config.children.ColorWheel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class HueFactoryTests {

    private val colorWheel = ColorWheel(
        startingFrequency = 40F,
        endingFrequency = 120F,
        offset = 0.25F
    )

    private fun createSUT(): HueFactory {
        return HueFactory(
            colorWheel = colorWheel
        )
    }

    @Test
    fun `calculate the hue using the frequency's relative position in the color wheel`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(60F, 0F)

        val hue = sut.create(frequencyBin)

        assertEquals(0.5F, hue)
    }

}