package music

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WesternTuningSystemTests {

    @Test
    fun `the system has 12 pitch classes`() {
        val tuning = WesternTuningSystem()

        assertEquals(12, tuning.pitchClasses.size)
    }

    // Tuning
    // Reference: https://en.wikipedia.org/wiki/A440_(pitch_standard)
    @Test
    fun `the tuning is A4 440 Hz by default`() {
        val tuning = WesternTuningSystem()

        val a4Frequency = tuning.getFrequency(tuning.A, octave = 4)

        assertEquals(440f, a4Frequency, 0.01f)
    }

    @Test
    fun `custom tunings are supported`() {
        val baroquePitch = 415f
        val tuning = WesternTuningSystem(a4Frequency = baroquePitch)

        val a4Frequency = tuning.getFrequency(tuning.A, octave = 4)

        assertEquals(baroquePitch, a4Frequency, 0.01f)
    }

    // Characterization tests
    // Reference: https://muted.io/note-frequencies/
    @Test
    fun `C4 is 261_63 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.C, octave = 4)

        assertEquals(261.63f, frequency, 0.01f)
    }

    @Test
    fun `C#4 is 277_18 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.C_SHARP, octave = 4)

        assertEquals(277.18f, frequency, 0.01f)
    }

    @Test
    fun `D4 is 293_66 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.D, octave = 4)

        assertEquals(293.66f, frequency, 0.01f)
    }

    @Test
    fun `D#4 is 311_13 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.D_SHARP, octave = 4)

        assertEquals(311.13f, frequency, 0.01f)
    }

    @Test
    fun `E4 is 329_63 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.E, octave = 4)

        assertEquals(329.63f, frequency, 0.01f)
    }

    @Test
    fun `F4 is 349_23 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.F, octave = 4)

        assertEquals(349.23f, frequency, 0.01f)
    }

    @Test
    fun `F#4 is 369_99 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.F_SHARP, octave = 4)

        assertEquals(369.99f, frequency, 0.01f)
    }

    @Test
    fun `G4 is 392_00 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.G, octave = 4)

        assertEquals(392.00f, frequency, 0.01f)
    }

    @Test
    fun `G#4 is 415_30 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.G_SHARP, octave = 4)

        assertEquals(415.30f, frequency, 0.01f)
    }

    @Test
    fun `A4 is 440_00 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.A, octave = 4)

        assertEquals(440.00f, frequency, 0.01f)
    }

    @Test
    fun `A#4 is 466_16 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.A_SHARP, octave = 4)

        assertEquals(466.16f, frequency, 0.01f)
    }

    @Test
    fun `B4 is 493_88 Hz`() {
        val tuning = WesternTuningSystem()

        val frequency = tuning.getFrequency(tuning.B, octave = 4)

        assertEquals(493.88f, frequency, 0.01f)
    }

}