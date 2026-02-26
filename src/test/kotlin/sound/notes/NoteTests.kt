package sound.notes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NoteTests {

    @Test
    fun `get the frequency of an octave`() {
        val sut = Note(20F)

        val actual = sut.getFrequency(3)

        assertEquals(160F, actual)
    }

}