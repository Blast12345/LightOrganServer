package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.notes.Notes

class HueCalculatorIntegrationTests {

    @Test
    fun `C notes are red`() {
        val sut = HueCalculator()

        for (octave in 0 until 8) {
            val frequency = Notes.C.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0F, actual, 0.01F)
        }
    }

    @Test
    fun `F# notes are teal`() {
        val sut = HueCalculator()

        for (octave in 0 until 8) {
            val frequency = Notes.F_SHARP.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0.5F, actual, 0.01F)
        }
    }

    @Test
    fun `A notes are purple`() {
        val sut = HueCalculator()

        for (octave in 0 until 8) {
            val frequency = Notes.A.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0.75F, actual, 0.01F)
        }
    }

}