package music

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TuningTest {

    private val tuning = object : Tuning() {
        val firstPitch = PitchClass("Low", 0f)
        val halfwayPitch = PitchClass("High", 0.5f)

        override val pitchClasses = listOf(firstPitch, halfwayPitch)
        override val octaveRatio = 2f
        override val referencePitchClass = firstPitch
        override val referenceOctave = 0
        override val referenceFrequency = 1f
    }

    // Frequency
    @Test
    fun `given a pitch class and octave, returns the frequency`() {
        assertEquals(1.0f, tuning.getFrequency(tuning.firstPitch, octave = 0), 0.1f)
        assertEquals(2.0f, tuning.getFrequency(tuning.firstPitch, octave = 1), 0.1f)
        assertEquals(1.4f, tuning.getFrequency(tuning.halfwayPitch, octave = 0), 0.1f)
        assertEquals(2.8f, tuning.getFrequency(tuning.halfwayPitch, octave = 1), 0.1f)
    }

    // Helical angle
    @Test
    fun `given a frequency is at the start of the first octave, the helical angle is zero turns`() {
        val frequency = tuning.getFrequency(tuning.firstPitch, octave = 0)

        val angle = tuning.getHelicalAngle(frequency)

        assertEquals(0.0, angle.turns, 0.001)
    }

    @Test
    fun `given a frequency is halfway through the first octave, the helical angle is half a turn`() {
        val frequency = tuning.getFrequency(tuning.halfwayPitch, octave = 0)

        val angle = tuning.getHelicalAngle(frequency)

        assertEquals(0.5, angle.turns, 0.001)
    }

    @Test
    fun `helical angle increases by one turn per octave`() {
        val frequency = tuning.getFrequency(tuning.firstPitch, octave = 1)

        val angle = tuning.getHelicalAngle(frequency)

        assertEquals(1.0, angle.turns, 0.001)
    }

    // Octave
    @Test
    fun `given a frequency at the start of an octave, returns that octave`() {
        val frequency = tuning.getFrequency(tuning.firstPitch, octave = 2)

        val octave = tuning.getOctave(frequency)

        assertEquals(2, octave)
    }

    @Test
    fun `given a frequency partway through an octave, returns that octave`() {
        val frequency = tuning.getFrequency(tuning.halfwayPitch, octave = 1)

        val octave = tuning.getOctave(frequency)

        assertEquals(1, octave)
    }

    // Position in octave
    @Test
    fun `given a frequency is at the start of an octave, the position in the octave is 0 degrees`() {
        val frequency = tuning.getFrequency(tuning.firstPitch, octave = 1)

        val result = tuning.getPositionInOctave(frequency)

        assertEquals(0.0, result.degrees, 0.001)
    }

    @Test
    fun `given a frequency is halfway through an octave, the position in the octave is 180 degrees`() {
        val frequency = tuning.getFrequency(tuning.halfwayPitch, octave = 1)

        val result = tuning.getPositionInOctave(frequency)

        assertEquals(180.0, result.degrees, 0.001)
    }

}