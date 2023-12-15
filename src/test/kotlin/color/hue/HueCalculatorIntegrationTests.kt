package color.hue

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.notes.Note
import sound.notes.Notes

class HueCalculatorIntegrationTests {

    @Test
    fun `C notes are red`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(Notes.C)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0F, actual!!, 0.01F)
    }

    @Test
    fun `E notes are teal`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(Notes.E)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.33F, actual!!, 0.01F)
    }

    @Test
    fun `F# notes are teal`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(Notes.F_SHARP)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `G# notes are blue`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(Notes.G_SHARP)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.66F, actual!!, 0.01F)
    }

    @Test
    fun `A notes are purple`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(Notes.A)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.75F, actual!!, 0.01F)
    }

    @Test
    fun `D# and A at the same time is teal`() {
        val sut = HueCalculator()
        val binDSharp = FrequencyBin(Notes.D_SHARP.getFrequency(0), 1F)
        val binA = FrequencyBin(Notes.A.getFrequency(0), 1F)

        val actual = sut.calculate(listOf(binDSharp, binA))

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `F and G at the same time is teal`() {
        val sut = HueCalculator()
        val binF = FrequencyBin(Notes.F.getFrequency(0), 1F)
        val binG = FrequencyBin(Notes.G.getFrequency(0), 1F)

        val actual = sut.calculate(listOf(binF, binG))

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `D# and F at the same time is green`() {
        val sut = HueCalculator()
        val binF = FrequencyBin(Notes.D_SHARP.getFrequency(0), 1F)
        val binG = FrequencyBin(Notes.F.getFrequency(0), 1F)

        val actual = sut.calculate(listOf(binF, binG))

        assertEquals(0.33F, actual!!, 0.01F)
    }

    @Test
    fun `playing two different notes at the same time with equal magnitude results in the average of the two`() {
        val sut = HueCalculator()
        val notes = listOf(
            Notes.C,
            Notes.C_SHARP,
            Notes.D,
            Notes.D_SHARP,
            Notes.E,
            Notes.F,
            Notes.F_SHARP,
            Notes.G,
            Notes.G_SHARP,
            Notes.A,
            Notes.A_SHARP,
            Notes.B,
        )

        notes.forEachIndexed { index1, note1 ->
            notes.forEachIndexed { index2, note2 ->
                val bin1 = FrequencyBin(note1.getFrequency(0), 1F)
                val bin2 = FrequencyBin(note2.getFrequency(0), 1F)

                val actual = sut.calculate(listOf(bin1, bin2))

                val hue = ((index1 / 12F) + (index2 / 12F)) / 2F
                assertEquals(hue, actual!!, 0.01F)
            }
        }
    }

    private fun createFrequencyBinsFor(note: Note): FrequencyBinList {
        val bins = mutableListOf<FrequencyBin>()

        for (octave in 0 until 8) {
            bins.add(
                FrequencyBin(
                    frequency = note.getFrequency(octave),
                    magnitude = 1F
                )
            )
        }

        return bins
    }

}