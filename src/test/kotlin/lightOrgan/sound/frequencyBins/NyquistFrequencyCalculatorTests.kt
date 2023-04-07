package lightOrgan.sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NyquistFrequencyCalculatorTests {

    private fun createSUT(): NyquistFrequencyCalculator {
        return NyquistFrequencyCalculator()
    }

    @Test
    fun `the Nyquist Frequency is half the sample rate`() {
        val sut = createSUT()
        val nyquistFrequency = sut.calculate(4F)
        val expected = 2F
        assertEquals(expected, nyquistFrequency, 0.001F)
    }

}