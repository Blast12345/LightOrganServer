package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SampleSizeCalculatorTests {

    private fun createSUT(): SampleSizeCalculator {
        return SampleSizeCalculator()
    }

    @Test
    fun `calculate the number of samples required given a frequency`() {
        val sut = createSUT()
        val samplesRequired = sut.calculate(12000F, 48000F)
        assertEquals(4, samplesRequired)
    }

    @Test
    fun `the number of samples must always be a power of 2`() {
        // NOTE: It seems like this isn't strictly necessary, but does yield a significant performance advantage.
        // https://dsp.stackexchange.com/questions/10043/how-important-is-it-to-use-power-of-2-when-using-fft
        val sut = createSUT()
        val samplesRequired = sut.calculate(20F, 48000F)
        assertEquals(4096, samplesRequired)
    }

}