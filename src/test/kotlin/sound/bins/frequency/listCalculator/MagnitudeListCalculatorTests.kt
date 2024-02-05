package sound.bins.frequency.listCalculator

import LowPassFilter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import sound.signalProcessor.*
import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer
import toolkit.monkeyTest.nextAudioFormatWrapper
import toolkit.monkeyTest.nextConfig
import toolkit.monkeyTest.nextDoubleArray

// TODO: Is this the signal processor?
class MagnitudeListCalculatorTests {

    private val samples = nextDoubleArray()
    private val format = nextAudioFormatWrapper()
    private val config = nextConfig()
    private val decimatedNyquistFrequency = format.sampleRate / config.decimationFactor

    private val signalProcessor: SignalProcessor = mockk()
    private val processedSamples = nextDoubleArray()
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = mockk()
    private val relativeMagnitudeList = nextDoubleArray()
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = mockk()
    private val normalizedMagnitudes = nextDoubleArray()

    private val audioTrimmer: AudioTrimmer = mockk()
    private val trimmedSamples = nextDoubleArray()
    private val lowPassFilter: LowPassFilter = mockk()
    private val lowPassedSamples = nextDoubleArray()
    private val downsampler: Downsampler = mockk()
    private val downsampledSamples = nextDoubleArray()
    private val overlapAdd: OverlapAdd = mockk()
    private val overlappedWindows = listOf(nextDoubleArray(), nextDoubleArray()) // TODO: nextWindows()
    private val windowsPreparer: WindowsPreparer = mockk()
    private val preparedWindows = listOf(nextDoubleArray(), nextDoubleArray()) // TODO: nextWindows()
    private val windowsProcessor: WindowsProcessor = mockk()
    private val magnitudes = nextDoubleArray()

    private fun createSUT(): MagnitudeListCalculator {
        return MagnitudeListCalculator(
            signalProcessor = signalProcessor,
            relativeMagnitudeListCalculator = relativeMagnitudeListCalculator,
            relativeMagnitudeListNormalizer = relativeMagnitudeListNormalizer,
            config = config
        )
    }

//    @Test
//    fun `calculate the magnitudes for given samples`() {
//        val sut = createSUT()
//        every { signalProcessor.process(samples) } returns processedSamples
//        every { relativeMagnitudeListCalculator.calculate(processedSamples) } returns relativeMagnitudeList
//        every { relativeMagnitudeListNormalizer.normalize(relativeMagnitudeList, processedSamples.size) } returns normalizedMagnitudes
//
//        val actual = sut.calculate(samples)
//
//        assertArrayEquals(normalizedMagnitudes, actual)
//    }

    @Test
    fun `new --- calculate the magnitudes for given samples`() {
        val sut = createSUT()
        every { audioTrimmer.trim(samples, config.sampleSize) } returns trimmedSamples
        every { lowPassFilter.filter(trimmedSamples, decimatedNyquistFrequency) } returns lowPassedSamples
        every { downsampler.decimate(lowPassedSamples, config.decimationFactor) } returns downsampledSamples
        every { overlapAdd.process(downsampledSamples, config.overlaps, config.overlapPercent) } returns overlappedWindows
        every { windowsPreparer.prepare(overlappedWindows) } returns preparedWindows
        every { windowsProcessor.process(preparedWindows) } returns magnitudes

        val actual = sut.calculateNew(samples, format)

        assertArrayEquals(magnitudes, actual)
    }
}
