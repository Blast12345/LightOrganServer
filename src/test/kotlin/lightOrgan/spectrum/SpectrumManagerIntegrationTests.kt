package lightOrgan.spectrum

import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily
import dsp.windowing.WindowType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.generators.generateSineWave
import toolkit.monkeyTest.nextAudioFrame
import kotlin.time.Duration.Companion.milliseconds


// The processing chain is so long and specific that unit tests seemed like a mirror of implementation
// rather than checks for meaningful behavior. As such, integration tests seemed like the right tool.
class SpectrumManagerIntegrationTests {

    private val sampleRate = 48000f
    private val config: SpectrumConfig = mockk()

    private val frequency = 60f
    private val tone = generateSineWave(frequency, sampleRate, amplitude = 1f)
    private val silence = generateSineWave(frequency, sampleRate, amplitude = 0f)

    private val toneFrame = nextAudioFrame(tone)
    private val silenceFrame = nextAudioFrame(silence)
    private val interleavedFrame = nextAudioFrame(tone, silence)

    @BeforeEach
    fun setupHappyPath() {
        every { config.frameDuration } returns 100.milliseconds
        every { config.approximateBinSpacing } returns 1f
        every { config.highPassFilter } returns null
        every { config.lowPassFilter } returns null
        every { config.window } returns WindowType.Hann
    }

    private fun createSUT(): SpectrumManager {
        return SpectrumManager(config)
    }

    // Frequency data
    @Test
    fun `given a tone, the peak bin is at the frequency of the wave`() {
        val sut = createSUT()

        val bins = sut.calculate(toneFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(frequency, peakBin.frequency, 0.1f)
    }

    @Test
    fun `window correction produces expected magnitude for full volume sine wave`() {
        val sut = createSUT()

        val bins = sut.calculate(toneFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(1f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `silence produces near-zero magnitudes`() {
        val sut = createSUT()

        val bins = sut.calculate(silenceFrame)

        bins.forEach { assertTrue(it.magnitude < 0.1f) }
    }

    // Multichannel
    @Test
    fun `stereo input is mixed to mono before processing`() {
        val sut = createSUT()

        val bins = sut.calculate(interleavedFrame)

        // Half-amplitude because only one channel (i.e., half) has the tone
        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(60f, peakBin.frequency, 1f)
        assertEquals(0.5f, peakBin.magnitude, 0.1f)
    }

    // Filtering
    @Test
    fun `given a frequency is below the high pass cutoff, it is filtered`() {
        val sut = createSUT()
        every { config.highPassFilter } returns FilterConfig.highPassFromSlope(
            FilterFamily.BUTTERWORTH,
            frequency * 2f,
            6
        )

        val bins = sut.calculate(toneFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(frequency, peakBin.frequency, 0.1f)
        assertTrue(peakBin.magnitude <= 0.5f)
    }

    @Test
    fun `given a frequency is above the low pass cutoff, it is filtered`() {
        val sut = createSUT()
        every { config.lowPassFilter } returns FilterConfig.lowPassFromSlope(
            FilterFamily.BUTTERWORTH,
            frequency / 2f,
            6
        )

        val bins = sut.calculate(toneFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(frequency, peakBin.frequency, 0.1f)
        assertTrue(peakBin.magnitude <= 0.5f)
    }

    // Emission
    @Test
    fun `frequency bins are emitted to state flow`() {
        val sut = createSUT()

        val bins = sut.calculate(toneFrame)

        assertEquals(bins, sut.frequencyBins.value)
    }

}