package lightOrgan.spectrum

import audio.samples.AudioFrame
import audio.samples.RollingAudioBuffer
import dsp.MonoMixer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FrequencyBinsCalculator
import dsp.filtering.OrderedFilter
import dsp.filtering.config.FilterBuilder
import dsp.filtering.config.FilterConfig
import dsp.windowing.WindowFunction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.*

// Some behavior is important but hard to validate via integration tests.
// This test file will encapsulate that.
class SpectrumManagerUnitTests {

    private val config: SpectrumConfig = mockk()
    private val monoMixer: MonoMixer = mockk()
    private val filterBuilder: FilterBuilder = mockk()
    private val audioBuffer: RollingAudioBuffer = mockk()
    private val windowFunction: WindowFunction = mockk()
    private val interpolator: ZeroPaddingInterpolator = mockk()
    private val frequencyBinsCalculator: FrequencyBinsCalculator = mockk()

    private val format1 = nextAudioFormat()
    private val format2 = nextAudioFormat()
    private val format1Frame1 = nextAudioFrame(sampleRate = format1.sampleRate)
    private val format1Frame2 = nextAudioFrame(sampleRate = format1.sampleRate)
    private val format2Frame1 = nextAudioFrame(sampleRate = format2.sampleRate)

    private val interpolatedSampleSize = nextPositiveInt()
    private val lowPassFilterConfig: FilterConfig = mockk()
    private val highPassFilterConfig: FilterConfig = mockk()
    private val lowPassFilter1: OrderedFilter = mockk()
    private val highPassFilter1: OrderedFilter = mockk()
    private val lowPassFilter2: OrderedFilter = mockk()
    private val highPassFilter2: OrderedFilter = mockk()
    private val monoFrame: AudioFrame = nextAudioFrame()
    private val lowPassedSamples = nextFloatArray()
    private val highPassedSamples = nextFloatArray()
    private val bufferedFrame = nextAudioFrame()
    private val windowedSamples = nextFloatArray()
    private val interpolatedSamples = nextFloatArray()
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setupHappyPath() {
        // Don't worry about much input matching unless necessary - integration tests will verify the data flow.
        every { config.interpolatedSampleSize } returns interpolatedSampleSize
        every { config.lowPassFilter } returns lowPassFilterConfig
        every { config.highPassFilter } returns highPassFilterConfig

        every { filterBuilder.build(lowPassFilterConfig, format1.sampleRate) } returns lowPassFilter1
        every { filterBuilder.build(highPassFilterConfig, format1.sampleRate) } returns highPassFilter1
        every { filterBuilder.build(lowPassFilterConfig, format2.sampleRate) } returns lowPassFilter2
        every { filterBuilder.build(highPassFilterConfig, format2.sampleRate) } returns highPassFilter2

        every { monoMixer.mix(any()) } returns monoFrame
        every { lowPassFilter1.filter(any()) } returns lowPassedSamples
        every { lowPassFilter1.supportedSampleRate } returns format1.sampleRate
        every { highPassFilter1.filter(any()) } returns highPassedSamples
        every { highPassFilter1.supportedSampleRate } returns format1.sampleRate
        every { lowPassFilter2.filter(any()) } returns lowPassedSamples
        every { lowPassFilter2.supportedSampleRate } returns format2.sampleRate
        every { highPassFilter2.filter(any()) } returns highPassedSamples
        every { highPassFilter2.supportedSampleRate } returns format2.sampleRate

        every { audioBuffer.append(any()) } returns bufferedFrame
        every { windowFunction.appliedTo(any()) } returns windowedSamples
        every { windowFunction.amplitudeCorrectionFactor } returns 1f
        every { interpolator.interpolate(any(), any()) } returns interpolatedSamples
        every { frequencyBinsCalculator.calculate(any()) } returns frequencyBins
    }

    private fun createSUT(): SpectrumManager {
        return SpectrumManager(
            config = config,
            monoMixer = monoMixer,
            filterBuilder = filterBuilder,
            audioBuffer = audioBuffer,
            windowFunction = windowFunction,
            interpolator = interpolator,
            frequencyBinsCalculator = frequencyBinsCalculator
        )
    }

    // Filter building
    @Test
    fun `given then the config has no filters, then do not build filters`() {
        val sut = createSUT()
        every { config.lowPassFilter } returns null
        every { config.highPassFilter } returns null

        sut.calculate(format1Frame1)

        verify(exactly = 0) { filterBuilder.build(any(), any()) }
    }

    @Test
    fun `given then the config has filters, build filters on first audio frame`() {
        val sut = createSUT()

        sut.calculate(format1Frame1)

        verify { filterBuilder.build(lowPassFilterConfig, format1.sampleRate) }
        verify { filterBuilder.build(highPassFilterConfig, format1.sampleRate) }
    }

    @Test
    fun `filters are reused if the sample rate does not change`() {
        val sut = createSUT()

        sut.calculate(format1Frame1)
        sut.calculate(format1Frame2)

        // Filters work best if reused because the first results of a filter are inaccurate.
        verify(exactly = 1) { filterBuilder.build(lowPassFilterConfig, format1.sampleRate) }
        verify(exactly = 1) { filterBuilder.build(highPassFilterConfig, format1.sampleRate) }
    }

    @Test
    fun `rebuild filters when sample rate changes`() {
        val sut = createSUT()

        sut.calculate(format1Frame1)
        sut.calculate(format2Frame1)

        // Filter stages are dependent upon the sample rate; changing rates could lead to inaccurate results
        verify(exactly = 1) { filterBuilder.build(lowPassFilterConfig, format1.sampleRate) }
        verify(exactly = 1) { filterBuilder.build(highPassFilterConfig, format1.sampleRate) }
        verify(exactly = 1) { filterBuilder.build(lowPassFilterConfig, format2.sampleRate) }
        verify(exactly = 1) { filterBuilder.build(highPassFilterConfig, format2.sampleRate) }
    }

}