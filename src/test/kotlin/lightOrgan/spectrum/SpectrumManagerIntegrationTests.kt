package lightOrgan.spectrum

import bins.nearestTo
import dsp.Downsampler
import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily
import dsp.filtering.config.FilterOrder
import extensions.inSeconds
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.generators.combineWaves
import toolkit.generators.generateSilence
import toolkit.generators.generateSineWave
import toolkit.monkeyTest.nextAudioFrame
import kotlin.time.Duration.Companion.milliseconds

// Calculating bins from audio is such a complex process that unit tests don't feel like they deliver the "big picture"
// So, these tests represent the ultimate goal of the spectrum manager.
@OptIn(ExperimentalCoroutinesApi::class)
class SpectrumManagerIntegrationTests {

    private val config = SpectrumConfig(
        frameDuration = 50.milliseconds, // 20 Hz spacing
        approximateBinSpacing = 1f,
        rolloffThreshold = -48f,
        highPassFilter = null,
        lowPassFilter = null,
    )

    private val sampleRate = 48000f
    private val frequency1 = 60f
    private val frequency2 = 120f
    private val middleFrequency = (frequency1 + frequency2) / 2f
    private val wave1 = generateSineWave(frequency1, sampleRate, amplitude = 1f)
    private val wave2 = generateSineWave(frequency2, sampleRate, amplitude = 1f)
    private val combinedWaves = combineWaves(wave1, wave2)
    private val silence = generateSilence(sampleRate)

    private val wave1Frame = nextAudioFrame(wave1)
    private val combinedWavesFrame = nextAudioFrame(combinedWaves)
    private val silenceFrame = nextAudioFrame(silence)

    val highPassConfig = FilterConfig.HighPass(
        family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
        frequency = middleFrequency
    )

    val lowPassConfig = FilterConfig.LowPass(
        family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
        frequency = middleFrequency
    )

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    // Frequency Bins
    @Test
    fun `given a tone, the peak bin is at the frequency of the wave`() {
        val sut = SpectrumManager(config)

        val bins = sut.calculate(wave1Frame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(frequency1, peakBin.frequency, config.approximateBinSpacing)
        assertEquals(1f, peakBin.magnitude, 0.1f)
    }

    @Test
    fun `silence produces near-zero magnitudes`() {
        val sut = SpectrumManager(config)

        val bins = sut.calculate(silenceFrame)

        bins.forEach { assertTrue(it.magnitude < 0.1f) }
    }

    @Test
    fun `given multiple tones, the peak bins are at the respective frequency of the waves`() {
        val sut = SpectrumManager(config)

        val bins = sut.calculate(combinedWavesFrame)

        val peak1 = bins.nearestTo(frequency1)
        assertEquals(frequency1, peak1.frequency, config.approximateBinSpacing)
        assertEquals(1f, peak1.magnitude, 0.1f)

        val peak2 = bins.nearestTo(frequency2)
        assertEquals(frequency2, peak2.frequency, config.approximateBinSpacing)
        assertEquals(1f, peak2.magnitude, 0.1f)
    }

    @Test
    fun `bin spacing approximately matches configured spacing`() {
        val sut = SpectrumManager(config)

        val bins = sut.calculate(wave1Frame)

        val maxSpacing = bins.zipWithNext { a, b -> b.frequency - a.frequency }.max()

        assertTrue(
            maxSpacing <= config.approximateBinSpacing,
            "Max bin spacing $maxSpacing exceeds configured spacing ${config.approximateBinSpacing}"
        )
    }

    @Test
    fun `bins below the frequency resolution are not included`() {
        val sut = SpectrumManager(
            config.copy(highPassFilter = null) // High pass may lead to false positives
        )

        val bins = sut.calculate(wave1Frame)

        val frequencyResolution = 1 / config.frameDuration.inSeconds
        val lowestBin = bins.minBy { it.frequency }
        assertTrue(lowestBin.frequency >= frequencyResolution)
    }

    // Multichannel
    @Test
    fun `stereo input produces correct results`() {
        val sut = SpectrumManager(config)
        val stereoFrame = nextAudioFrame(wave1, silence)

        val bins = sut.calculate(stereoFrame)

        val peakBin = bins.maxBy { it.magnitude }
        assertEquals(frequency1, peakBin.frequency, config.approximateBinSpacing)
        assertEquals(0.5f, peakBin.magnitude, 0.1f) // Half-amplitude because only one channel (i.e., half) has the tone
    }

    // DSP Filters
    @Test
    fun `high pass filter attenuates frequencies below cutoff`() {
        val sut = SpectrumManager(config.copy(highPassFilter = highPassConfig))

        val bins = sut.calculate(combinedWavesFrame)

        val peak1 = bins.nearestTo(frequency1)
        val peak2 = bins.nearestTo(frequency2)

        assertTrue(peak1.magnitude < 0.1f, "Expected $frequency1 Hz to be attenuated")
        assertEquals(1f, peak2.magnitude, 0.1f)
    }

    @Test
    fun `bins below high pass threshold are not included`() {
        val sut = SpectrumManager(config.copy(highPassFilter = highPassConfig))

        val bins = sut.calculate(wave1Frame)

        val threshold = highPassConfig.frequencyAtMagnitude(config.rolloffThreshold)
        val lowestBin = bins.minBy { it.frequency }
        assertTrue(
            lowestBin.frequency >= threshold,
            "Expected no bins below high pass threshold of $threshold Hz"
        )
    }

    @Test
    fun `low pass filter attenuates frequencies above cutoff`() {
        val sut = SpectrumManager(config.copy(lowPassFilter = lowPassConfig))

        val bins = sut.calculate(combinedWavesFrame)

        val peak1 = bins.nearestTo(frequency1)
        val peak2 = bins.nearestTo(frequency2)

        assertEquals(1f, peak1.magnitude, 0.1f)
        assertTrue(peak2.magnitude < 0.1f, "Expected $frequency2 Hz to be attenuated")
    }

    @Test
    fun `bins above low pass threshold are not included`() {
        val sut = SpectrumManager(config.copy(lowPassFilter = lowPassConfig))

        val bins = sut.calculate(wave1Frame)

        val threshold = lowPassConfig.frequencyAtMagnitude(config.rolloffThreshold)
        val highestBin = bins.maxBy { it.frequency }
        assertTrue(
            highestBin.frequency <= threshold,
            "Expected no bins above low pass threshold of $threshold Hz"
        )
    }

    // Optimization
    @Test
    fun `given a low pass filter is used, performance is optimized by decimating`() {
        val downsampler = spyk(Downsampler())
        val sut = SpectrumManager(
            config = config.copy(lowPassFilter = lowPassConfig),
            downsampler = downsampler
        )

        sut.calculate(wave1Frame)

        verify { downsampler.decimate(any(), any(), any(), any()) }
    }

}