package sound.bins.frequency.dominant.frequency

import dsp.bins.FrequencyBin
import org.apache.commons.math3.complex.Complex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PeakFrequencyBinsFinderTests {

    private val bin0 = FrequencyBin(00F, Complex(4.0, 0.0))
    private val bin1 = FrequencyBin(10F, Complex(1.0, 0.0))
    private val bin2 = FrequencyBin(20F, Complex(2.0, 0.0))
    private val bin3 = FrequencyBin(30F, Complex(1.0, 0.0))
    private val bin4 = FrequencyBin(40F, Complex(2.0, 0.0))
    private val bin5 = FrequencyBin(50F, Complex(3.0, 0.0))
    private val frequencyBins = listOf(bin0, bin1, bin2, bin3, bin4, bin5)

    private fun createSUT(): PeakFrequencyBinsFinder {
        return PeakFrequencyBinsFinder()
    }

    @Test
    fun `find the peak frequency bins`() {
        val sut = createSUT()

        val frequencyBins = sut.find(frequencyBins)

        val expected = listOf(bin0, bin2, bin5)
        assertEquals(expected, frequencyBins)
    }

}
