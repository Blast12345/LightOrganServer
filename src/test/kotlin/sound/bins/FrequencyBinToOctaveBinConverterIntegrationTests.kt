package sound.bins

import dsp.bins.FrequencyBin
import music.WesternTuning
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.octave.OctaveBin

class FrequencyBinToOctaveBinConverterIntegrationTests {

    private val tuning = WesternTuning()

    private fun createSUT(): FrequencyBinToOctaveBinConverter {
        return FrequencyBinToOctaveBinConverter(tuning)
    }

    @Test
    fun `given a frequency of C1, create an octave bin`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(
            frequency = tuning.getFrequency(tuning.C, 1),
            magnitude = 0.25F
        )

        val actual = sut.convert(frequencyBin)

        val expected = OctaveBin(0F, 0.25F)
        assertEquals(expected, actual)
    }

    @Test
    fun `given a frequency of C4, create an octave bin`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(
            frequency = tuning.getFrequency(tuning.C, 4),
            magnitude = 0.5F
        )

        val actual = sut.convert(frequencyBin)

        val expected = OctaveBin(0F, 0.5F)
        assertEquals(expected, actual)
    }


    @Test
    fun `given a frequency of F#3, create an octave bin`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(
            frequency = tuning.getFrequency(tuning.F_SHARP, 3),
            magnitude = 0.75F
        )

        val actual = sut.convert(frequencyBin)

        val expected = OctaveBin(0.5F, 0.75F)
        assertEquals(expected, actual)
    }

}
