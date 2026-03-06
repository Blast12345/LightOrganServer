package music

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NoteTests {

    @Test
    fun `get the frequency of an octave`() {
        val sut = Note(20F)

        val actual = sut.getFrequency(3)

        Assertions.assertEquals(160F, actual)
    }

}