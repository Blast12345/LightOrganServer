package lightOrgan.color.calculator.hue

import bins.frequency.FrequencyBin
import bins.frequency.FrequencyBins
import bins.octave.OctaveBinFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import sound.bins.frequency.filters.Crossover
import sound.notes.Note
import sound.notes.Notes

// NOTE: In theory, this implementation should work for any scale, hence the nested test classes.
class OctaveHueCalculatorIntegrationTests {

    @Suppress("ClassName")
    @Nested
    inner class `given C is the start of the octave` {

        private val c3Crossover = Crossover(
            Notes.C.getFrequency(3),
            Notes.C.getFrequency(3)
        )

        private fun createSUT(highCrossover: Crossover? = null) = OctaveHueCalculator(
            highCrossover = highCrossover,
            octaveBinFactory = OctaveBinFactory(rootFrequency = Notes.C.getFrequency(0))
        )

        // Single notes
        @Test
        fun `C notes are red`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.C)

            val actual = sut.calculate(bins)

            Assertions.assertEquals(0F, actual!!, 0.01F)
        }

        @Test
        fun `F# notes are teal`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.F_SHARP)

            val actual = sut.calculate(bins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `A notes are purple`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.A)

            val actual = sut.calculate(bins)

            Assertions.assertEquals(0.75F, actual!!, 0.01F)
        }

        // Multiple notes
        @Test
        fun `D# and A at the same time is teal`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP)
            val aBins = createFrequencyBinsFor(Notes.A)

            val actual = sut.calculate(dSharpBins + aBins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `F and G at the same time is teal`() {
            val sut = createSUT()
            val fBins = createFrequencyBinsFor(Notes.F)
            val gBins = createFrequencyBinsFor(Notes.G)

            val actual = sut.calculate(fBins + gBins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `D# and F at the same time is green`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP)
            val fBins = createFrequencyBinsFor(Notes.F)

            val actual = sut.calculate(dSharpBins + fBins)

            Assertions.assertEquals(0.33F, actual!!, 0.01F)
        }

        // Octaves
        @ParameterizedTest
        @ValueSource(ints = [-1, 0, 1, 10])
        fun `given a single note, the hue is consistent across octaves`(octave: Int) {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.F_SHARP, octave)

            val actual = sut.calculate(bins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `given a multiple notes, the hue is consistent across octaves`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP, 1)
            val aBins = createFrequencyBinsFor(Notes.A, 4)

            val actual = sut.calculate(dSharpBins + aBins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        // High crossover
        @Test
        fun `given the peaks are below the crossover, return the hue`() {
            val sut = createSUT(c3Crossover)
            val bins = createFrequencyBinsFor(Notes.F_SHARP, 2)

            val actual = sut.calculate(bins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `given the peaks are above the crossover, return null`() {
            val sut = createSUT(c3Crossover)
            val bins = createFrequencyBinsFor(Notes.F_SHARP, 4)

            val actual = sut.calculate(bins)

            Assertions.assertNull(actual)
        }

        @Test
        fun `given one peak is above the crossover and another is below, return the hue of the lower note`() {
            val sut = createSUT(c3Crossover)
            val fSharpBins = createFrequencyBinsFor(Notes.F_SHARP, 2)
            val cBins = createFrequencyBinsFor(Notes.C, 4)

            val actual = sut.calculate(fSharpBins + cBins)

            Assertions.assertEquals(0.5F, actual!!, 0.01F)
        }

        // Helpers
        private fun createFrequencyBinsFor(
            note: Note,
            octave: Int = 0
        ): FrequencyBins {
            return listOf(
                FrequencyBin(note.getFrequency(octave) - 1, 0F),
                FrequencyBin(note.getFrequency(octave), 1F),
                FrequencyBin(note.getFrequency(octave) + 1, 0F),
            )
        }

    }


}