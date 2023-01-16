package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class BrightnessFactoryTests {

    private fun createSUT(): BrightnessFactory {
        return BrightnessFactory()
    }

    @Test
    fun `return the magnitude of the frequency bin`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(0F, 0.5F)
        val brightness = sut.create(frequencyBin)
        assertEquals(0.5F, brightness)
    }

    @Test
    fun `return 1 when the magnitude is greater than 1`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(0F, 1.5F)
        val brightness = sut.create(frequencyBin)
        assertEquals(1F, brightness)
    }

}