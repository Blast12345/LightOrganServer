package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin

class BrightnessFactoryTests {

    private val frequencyBin = nextFrequencyBin()

    private fun createSUT(): BrightnessFactory {
        return BrightnessFactory()
    }

    @Test
    fun `the brightness is determined by magnitude of a frequency bin`() {
        val sut = createSUT()
        val brightness = sut.create(frequencyBin)
        assertEquals(frequencyBin.magnitude, brightness)
    }

}