package color

import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import music.PitchClass
import music.WesternTuning
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HueCalculatorIntegrationTests {

    private val tuning = WesternTuning()

    @Test
    fun `C notes are red`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(tuning.C)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0F, actual!!, 0.01F)
    }

    @Test
    fun `E notes are teal`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(tuning.E)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.33F, actual!!, 0.01F)
    }

    @Test
    fun `F# notes are teal`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(tuning.F_SHARP)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `G# notes are blue`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(tuning.G_SHARP)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.66F, actual!!, 0.01F)
    }

    @Test
    fun `A notes are purple`() {
        val sut = HueCalculator()
        val frequencyBins = createFrequencyBinsFor(tuning.A)

        val actual = sut.calculate(frequencyBins)

        assertEquals(0.75F, actual!!, 0.01F)
    }

    @Test
    fun `D# and A at the same time is teal`() {
        val sut = HueCalculator()

        val actual = sut.calculate(
            listOf(
                FrequencyBin(tuning.getFrequency(tuning.D_SHARP, 0), 1F),
                FrequencyBin(tuning.getFrequency(tuning.A, 0), 1F)
            )
        )

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `F and G at the same time is teal`() {
        val sut = HueCalculator()

        val actual = sut.calculate(
            listOf(
                FrequencyBin(tuning.getFrequency(tuning.F, 0), 1F),
                FrequencyBin(tuning.getFrequency(tuning.G, 0), 1F)
            )
        )

        assertEquals(0.5F, actual!!, 0.01F)
    }

    @Test
    fun `D# and F at the same time is green`() {
        val sut = HueCalculator()

        val actual = sut.calculate(
            listOf(
                FrequencyBin(tuning.getFrequency(tuning.D_SHARP, 0), 1F),
                FrequencyBin(tuning.getFrequency(tuning.F, 0), 1F)
            )
        )

        assertEquals(0.33F, actual!!, 0.01F)
    }

    @Test
    fun `playing two notes with equal magnitude results in the average hue`() {
        val sut = HueCalculator()

        val actual = sut.calculate(
            listOf(
                FrequencyBin(tuning.getFrequency(tuning.C, octave = 0), 1F),
                FrequencyBin(tuning.getFrequency(tuning.F_SHARP, octave = 0), 1F)
            )
        )

        // C = 0/12 = 0.0, F# = 6/12 = 0.5, average = 0.25
        assertEquals(0.25F, actual!!, 0.01F)
    }

    private fun createFrequencyBinsFor(pitchClass: PitchClass): FrequencyBins {
        val bins = mutableListOf<FrequencyBin>()

        for (octave in 0 until 8) {
            bins.add(
                FrequencyBin(
                    frequency = tuning.getFrequency(pitchClass, octave),
                    magnitude = 1F
                )
            )
        }

        return bins
    }

}
