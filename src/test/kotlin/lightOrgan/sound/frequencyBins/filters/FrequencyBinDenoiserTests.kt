package lightOrgan.sound.frequencyBins.filters

import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrequencyBinDenoiserTests {

    private val frequencyBin = FrequencyBin(40F, 0.001F)

    private fun createSUT(): FrequencyBinDenoiser {
        return FrequencyBinDenoiser()
    }

    @Test
    fun `magnitude less than 0_01 is set to 0`() {
        val sut = createSUT()
        val actual = sut.denoise(frequencyBin)
        val expected = FrequencyBin(40F, 0F)
        assertEquals(expected, actual)
    }

}