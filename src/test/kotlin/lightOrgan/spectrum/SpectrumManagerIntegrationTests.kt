package lightOrgan.spectrum

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.generators.generateSineWave
import toolkit.monkeyTest.nextAudioFrame

// The processing chain is so long and specific that unit tests seemed like a mirror of implementation
// rather than checks for meaningful behavior. As such, integration tests seemed like the right tool.
class SpectrumManagerIntegrationTests {

    private val frequency = 60f
    private val sampleRate = 48000f
    private val samplesPerSecond = sampleRate.toInt()
    private val sixtyHzTone = generateSineWave(frequency, 1f, samplesPerSecond, sampleRate)
    private val silence = FloatArray(samplesPerSecond)

    private val sixtyHzFrame = nextAudioFrame(listOf(sixtyHzTone), sampleRate)
    private val silentFrame = nextAudioFrame(listOf(silence), sampleRate)

    private fun createSUT(magnitudeMultiplier: Float = 1f): SpectrumManager {
        return SpectrumManager(
            magnitudeMultiplier = magnitudeMultiplier
        )
    }

    @Test
    fun `60 Hz sine wave produces peak at 60 Hz`() {
        val sut = createSUT()

        val bins = sut.calculate(sixtyHzFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(60f, peakBin.frequency, 0.1f)
    }

    @Test
    fun `window correction produces expected magnitude for full volume sine wave`() {
        val sut = createSUT()

        val bins = sut.calculate(sixtyHzFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(1f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `magnitude multiplier scales bin magnitudes`() {
        val baselinSut = createSUT(magnitudeMultiplier = 1f)
        val baselineBins = baselinSut.calculate(sixtyHzFrame)

        val scaledSut = createSUT(magnitudeMultiplier = 2f)
        val scaledBins = scaledSut.calculate(sixtyHzFrame)

        val baselinePeak = baselineBins.maxBy { it.magnitude }
        val scaledPeak = scaledBins.maxBy { it.magnitude }
        assertEquals(baselinePeak.magnitude * 2f, scaledPeak.magnitude, 0.01f)
    }

    @Test
    fun `silence produces near-zero magnitudes`() {
        val sut = createSUT()

        val bins = sut.calculate(silentFrame)

        bins.forEach { assertTrue(it.magnitude < 0.1f) }
    }

    @Test
    fun `stereo input is mixed to mono before processing`() {
        val sut = createSUT()

        val stereoFrame = nextAudioFrame(listOf(sixtyHzTone, silence), sampleRate)
        val bins = sut.calculate(stereoFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(60f, peakBin.frequency, 1f)
        assertEquals(0.5f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `frequency bins are emitted to state flow`() {
        val sut = createSUT()

        val bins = sut.calculate(sixtyHzFrame)

        assertEquals(bins, sut.frequencyBins.value)
    }

}