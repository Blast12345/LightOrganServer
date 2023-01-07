package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SampleSizeCalculatorTests {

    private val frequency = 12000F
    private val sampleRate = 48000F

    private fun createSUT(): SampleSizeCalculator {
        return SampleSizeCalculator()
    }

    @Test
    fun `calculate the number of samples required given a frequency`() {
        val sut = createSUT()
        val samplesRequired = sut.calculate(frequency, sampleRate)
        assertEquals(4, samplesRequired)
    }

}