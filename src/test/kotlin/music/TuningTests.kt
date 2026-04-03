package music

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TuningSystemTests {

    private val tuningSystem = object : TuningSystem() {
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
        assertEquals(1.0f, tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 0), 0.1f)
        assertEquals(2.0f, tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 1), 0.1f)
        assertEquals(1.4f, tuningSystem.getFrequency(tuningSystem.halfwayPitch, octave = 0), 0.1f)
        assertEquals(2.8f, tuningSystem.getFrequency(tuningSystem.halfwayPitch, octave = 1), 0.1f)
    }

    // Helical angle
    @Test
    fun `given a frequency is at the start of the first octave, the helical angle is zero turns`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 0)

        val angle = tuningSystem.getHelicalAngle(frequency)

        assertEquals(0.0, angle.turns, 0.001)
    }

    @Test
    fun `given a frequency is halfway through the first octave, the helical angle is half a turn`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.halfwayPitch, octave = 0)

        val angle = tuningSystem.getHelicalAngle(frequency)

        assertEquals(0.5, angle.turns, 0.001)
    }

    @Test
    fun `helical angle increases by one turn per octave`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 1)

        val angle = tuningSystem.getHelicalAngle(frequency)

        assertEquals(1.0, angle.turns, 0.001)
    }

    // Octave
    @Test
    fun `given a frequency at the start of an octave, returns that octave`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 2)

        val octave = tuningSystem.getOctave(frequency)

        assertEquals(2, octave)
    }

    @Test
    fun `given a frequency partway through an octave, returns that octave`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.halfwayPitch, octave = 1)

        val octave = tuningSystem.getOctave(frequency)

        assertEquals(1, octave)
    }

    // Position in octave
    @Test
    fun `given a frequency is at the start of an octave, the position in the octave is 0 degrees`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.firstPitch, octave = 1)

        val result = tuningSystem.getPositionInOctave(frequency)

        assertEquals(0.0, result.degrees, 0.001)
    }

    @Test
    fun `given a frequency is halfway through an octave, the position in the octave is 180 degrees`() {
        val frequency = tuningSystem.getFrequency(tuningSystem.halfwayPitch, octave = 1)

        val result = tuningSystem.getPositionInOctave(frequency)

        assertEquals(180.0, result.degrees, 0.001)
    }

}