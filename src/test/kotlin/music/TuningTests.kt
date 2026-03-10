package music

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveInt

class TuningTests {

    @Suppress("ClassName")
    @Nested
    inner class `given Western tuning` {

        @Test
        fun `get the octave of a note`() {
            val sut = Tuning.western()

            for (note in Keys.all) {
                for (octave in 0..8) {
                    val frequency = note.getFrequency(octave)
                    val actual = sut.getOctave(frequency)
                    assertEquals(octave, actual, "Failed for $note at octave $octave")
                }
            }
        }

        @Test
        fun `C is at position 0 in an octave`() {
            val sut = Tuning.western()
            val frequency = Keys.C.getFrequency(nextPositiveInt(max = 8))

            val actual = sut.getPositionInOctave(frequency)

            assertEquals(0f, actual.turns.toFloat(), 0.001f)
        }

        @Test
        fun `C sharp is at position 0_08 in an octave`() {
            val sut = Tuning.western()
            val frequency = Keys.C_SHARP.getFrequency(nextPositiveInt(max = 8))

            val actual = sut.getPositionInOctave(frequency)

            assertEquals(0.083f, actual.turns.toFloat(), 0.001f)
        }

        @Test
        fun `A is at position 0_75 in an octave`() {
            val sut = Tuning.western()
            val frequency = Keys.A.getFrequency(nextPositiveInt(max = 8))

            val actual = sut.getPositionInOctave(frequency)

            assertEquals(0.75f, actual.turns.toFloat(), 0.001f)
        }

    }


}