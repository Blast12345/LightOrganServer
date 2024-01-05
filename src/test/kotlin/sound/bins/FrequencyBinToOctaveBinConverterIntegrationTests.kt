package sound.bins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin
import sound.bins.octave.OctaveBin
import sound.notes.Notes

class FrequencyBinToOctaveBinConverterIntegrationTests {

    private val rootNote = Notes.C

    private fun createSUT(): FrequencyBinToOctaveBinConverter {
        return FrequencyBinToOctaveBinConverter(
            rootNote = rootNote
        )
    }

    @Test
    fun `given a frequency of C1, create an octave bin`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(
            frequency = Notes.C.getFrequency(1),
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
            frequency = Notes.C.getFrequency(4),
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
            frequency = Notes.F_SHARP.getFrequency(3),
            magnitude = 0.75F
        )

        val actual = sut.convert(frequencyBin)

        val expected = OctaveBin(0.5F, 0.75F)
        assertEquals(expected, actual)
    }


}