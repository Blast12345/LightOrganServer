package lightOrgan.color.calculator.hue

import bins.FrequencyBin
import bins.FrequencyBins
import music.Note
import music.Notes
import music.Tuning
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import sound.bins.frequency.filters.Crossover
import kotlin.math.abs
import kotlin.math.min

class OctaveHueCalculatorIntegrationTests {

    @Suppress("ClassName")
    @Nested
    inner class `given Western tuning` {

        private val c3Crossover = Crossover(
            Notes.C.getFrequency(3),
            Notes.C.getFrequency(3)
        )

        private fun createSUT(highCrossover: Crossover? = null) = OctaveHueCalculator(
            highCrossover = highCrossover,
            tuning = Tuning.western()
        )

        // Single notes
        @Test
        fun `C notes are red`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.C)

            val actual = sut.calculate(bins)

            assertCircularEquals(0F, actual!!, 0.01F)
        }

        @Test
        fun `F# notes are teal`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.F_SHARP)

            val actual = sut.calculate(bins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `A notes are purple`() {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.A)

            val actual = sut.calculate(bins)

            assertCircularEquals(0.75F, actual!!, 0.01F)
        }

        // Multiple notes
        @Test
        fun `D# and A at the same time is teal`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP)
            val aBins = createFrequencyBinsFor(Notes.A)

            val actual = sut.calculate(dSharpBins + aBins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `F and G at the same time is teal`() {
            val sut = createSUT()
            val fBins = createFrequencyBinsFor(Notes.F)
            val gBins = createFrequencyBinsFor(Notes.G)

            val actual = sut.calculate(fBins + gBins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `D# and F at the same time is green`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP)
            val fBins = createFrequencyBinsFor(Notes.F)

            val actual = sut.calculate(dSharpBins + fBins)

            assertCircularEquals(0.33F, actual!!, 0.01F)
        }

        @Test
        fun `C slightly sharp and C slightly flat average to red`() {
            val sut = createSUT()
            val cSlightlyFlat = createFrequencyBinsFor(Notes.C.getFrequency(3) - 1)
            val cSlightlySharp = createFrequencyBinsFor(Notes.C.getFrequency(4) + 1)

            val actual = sut.calculate(cSlightlyFlat + cSlightlySharp)

            assertCircularEquals(0.0F, actual!!, 0.01F)
        }

        // Octaves
        @ParameterizedTest
        @ValueSource(ints = [-1, 0, 1, 10])
        fun `given a single note, the hue is consistent across octaves`(octave: Int) {
            val sut = createSUT()
            val bins = createFrequencyBinsFor(Notes.F_SHARP, octave)

            val actual = sut.calculate(bins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `given a multiple notes, the hue is consistent across octaves`() {
            val sut = createSUT()
            val dSharpBins = createFrequencyBinsFor(Notes.D_SHARP, 1)
            val aBins = createFrequencyBinsFor(Notes.A, 4)

            val actual = sut.calculate(dSharpBins + aBins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        // High crossover
        @Test
        fun `given the peaks are below the crossover, return the hue`() {
            val sut = createSUT(c3Crossover)
            val bins = createFrequencyBinsFor(Notes.F_SHARP, 2)

            val actual = sut.calculate(bins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        @Test
        fun `given the peaks are above the crossover, return null`() {
            val sut = createSUT(c3Crossover)
            val bins = createFrequencyBinsFor(Notes.F_SHARP, 4)

            val actual = sut.calculate(bins)

            assertNull(actual)
        }

        @Test
        fun `given one peak is above the crossover and another is below, return the hue of the lower note`() {
            val sut = createSUT(c3Crossover)
            val fSharpBins = createFrequencyBinsFor(Notes.F_SHARP, 2)
            val cBins = createFrequencyBinsFor(Notes.C, 4)

            val actual = sut.calculate(fSharpBins + cBins)

            assertCircularEquals(0.5F, actual!!, 0.01F)
        }

        // Helpers
        private fun createFrequencyBinsFor(
            note: Note,
            octave: Int = 0
        ): FrequencyBins {
            return createFrequencyBinsFor(note.getFrequency(octave))
        }

        private fun createFrequencyBinsFor(
            frequency: Float
        ): FrequencyBins {
            return listOf(
                FrequencyBin(frequency - 1, 0F),
                FrequencyBin(frequency, 1F),
                FrequencyBin(frequency + 1, 0F),
            )
        }

        fun assertCircularEquals(expected: Float, actual: Float, tolerance: Float) {
            val delta = abs(expected - actual)
            val circularDelta = min(delta, 1F - delta)

            // Values of 0.001 and 0.999 are effectively the same hue because values wrap around at 1.
            assertTrue(
                circularDelta <= tolerance,
                "Expected $expected ± $tolerance but was $actual (circular distance: $circularDelta)"
            )
        }
    }


}