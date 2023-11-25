package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.notes.Notes

class HueCalculatorTests {

    // NOTE: These tests assume a root note of C
    private fun createSUT(): HueCalculator {
        return HueCalculator()
    }

    @Test
    fun `C notes are red`() {
        val sut = createSUT()

        for (octave in 0 until 8) {
            val frequency = Notes.C.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0F, actual, 0.01F)
        }
    }

    @Test
    fun `F# notes are teal`() {
        val sut = createSUT()

        for (octave in 0 until 8) {
            val frequency = Notes.F_SHARP.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0.5F, actual, 0.01F)
        }
    }

    @Test
    fun `A notes are purple`() {
        val sut = createSUT()

        for (octave in 0 until 8) {
            val frequency = Notes.A.getFrequency(octave)
            val actual = sut.calculate(frequency)
            assertEquals(0.75F, actual, 0.01F)
        }
    }

}