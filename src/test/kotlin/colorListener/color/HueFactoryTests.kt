package colorListener.color

import colorListener.sound.frequencyBins.FrequencyBin
import org.junit.Assert.assertEquals
import org.junit.Test

class HueFactoryTests {

    private fun createSUT(): HueFactory {
        return HueFactory()
    }

    @Test
    fun `the hue corresponds to the average frequency`() {
        val sut = createSUT()

        val frequencyBins = listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(50.0, 1.0),
            FrequencyBin(100.0, 1.0)
        )
        val actualHue = sut.hueFrom(frequencyBins)

        assertEquals(0.75f, actualHue, 0.001f)
    }

}