package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrequencyBinFactoryTests {

    private val index = 2
    private val granularity = 5F
    private val magnitude = 7.0

    private fun createSUT(): FrequencyBinFactory {
        return FrequencyBinFactory()
    }

    @Test
    fun `create a frequency bin`() {
        val sut = createSUT()
        val frequencyBin = sut.create(index, granularity, magnitude)
        val expected = FrequencyBin(10F, 7F)
        assertEquals(expected, frequencyBin)
    }

}