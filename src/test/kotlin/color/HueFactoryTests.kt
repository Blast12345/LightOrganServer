package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.notes.Notes

class HueFactoryTests {

    private fun createSUT(): HueFactory {
        return HueFactory()
    }

    @Test
    fun `C notes are red`() {
        val sut = createSUT()

        for (octave in 0 until 8) {
            val frequency = Notes.C.getFrequency(octave)
            val actual = sut.create(frequency)
            assertEquals(0F, actual, 0.01F)
        }
    }

    @Test
    fun `F# notes are teal`() {
        val sut = createSUT()

        for (octave in 0 until 8) {
            val frequency = Notes.F_SHARP.getFrequency(octave)
            val actual = sut.create(frequency)
            assertEquals(0.5F, actual, 0.01F)
        }
    }

}