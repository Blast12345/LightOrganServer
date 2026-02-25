package lightOrgan.spectrum

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.generators.generateMonoAudioFrame
import toolkit.generators.generateSineWave
import toolkit.generators.generateStereoAudioFrame

// The processing chain is so long and specific that unit tests seemed like a mirror of implementation
// rather than checks for meaningful behavior. As such, integration tests seemed like the right tool.
class SpectrumManagerIntegrationTests {

    private val sut = SpectrumManager()

    private val frequency = 60f
    private val sampleRate = 48000f
    private val samplesPerSecond = sampleRate.toInt()
    private val sixtyHzTone = generateSineWave(frequency, 1f, samplesPerSecond, sampleRate)
    private val silence = FloatArray(samplesPerSecond)

    @Test
    fun `60 Hz sine wave produces peak at 60 Hz`() {
        val audio = generateMonoAudioFrame(sixtyHzTone, sampleRate)

        val bins = sut.calculate(audio)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(60f, peakBin.frequency, 0.1f)
    }

    @Test
    fun `amplitude correction produces expected magnitude for full volume sine wave`() {
        val audio = generateMonoAudioFrame(sixtyHzTone, sampleRate)

        val bins = sut.calculate(audio)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(1f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `silence produces near-zero magnitudes`() {
        val audio = generateMonoAudioFrame(silence, sampleRate)

        val bins = sut.calculate(audio)

        bins.forEach { assertTrue(it.magnitude < 0.1f) }
    }

    @Test
    fun `stereo input is mixed to mono before processing`() {
        val audio = generateStereoAudioFrame(sixtyHzTone, silence, sampleRate)

        val bins = sut.calculate(audio)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(60f, peakBin.frequency, 1f)
        assertEquals(0.5f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `frequency bins are emitted to state flow`() {
        val audio = generateMonoAudioFrame(sixtyHzTone, sampleRate)

        val bins = sut.calculate(audio)

        assertEquals(bins, sut.frequencyBins.value)
    }

}