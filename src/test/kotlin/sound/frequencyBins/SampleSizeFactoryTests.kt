package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SampleSizeFactoryTests {

    private fun createSUT(): SampleSizeFactory {
        return SampleSizeFactory()
    }

    @Test
    fun `calculate the number of samples required given a frequency`() {
        val sut = createSUT()
        val samplesRequired = sut.createFor(12000F, 48000F)
        assertEquals(4, samplesRequired)
    }

    @Test
    fun `the number of samples must always be a power of 2`() {
        val sut = createSUT()
        val samplesRequired = sut.createFor(20F, 48000F)
        assertEquals(4096, samplesRequired)
    }

}