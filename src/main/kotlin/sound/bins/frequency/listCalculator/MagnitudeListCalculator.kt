package sound.bins.frequency.listCalculator

import LowPassFilter
import config.Config
import config.ConfigSingleton
import sound.signalProcessor.*
import wrappers.audioFormat.AudioFormatWrapper

class MagnitudeListCalculator(
    private val config: Config = ConfigSingleton,
    private val audioTrimmer: AudioTrimmer = AudioTrimmer(),
    private val lowPassFilter: LowPassFilter = LowPassFilter(),
    private val downsampler: Downsampler = Downsampler(),
    private val overlapAdd: OverlapAdd = OverlapAdd(),
    private val windowsPreparer: WindowsPreparer = WindowsPreparer(),
    private val windowsProcessor: WindowsProcessor = WindowsProcessor()
) {
    // val decimatedNyquistFrequency = format.sampleRate / config.decimationFactor

    // NOTE: Omitting the downsampling and overlap-add works.
    // I think the overlap-add is the problem.
    fun calculateNew(samples: DoubleArray, format: AudioFormatWrapper): DoubleArray {
        val trimmedSamples = audioTrimmer.trim(samples, config.sampleSize)
        val downsampledSamples = downsampler.decimate(trimmedSamples, config.decimationFactor)
//        val overlappedWindows = overlapAdd.process(trimmedSamples, config.overlaps, config.overlapPercent)
//        val overlappedWindows = overlapAdd.overlapAdd2(trimmedSamples, config.overlaps, config.overlapPercent)
        val preparedWindows = windowsPreparer.prepare(listOf(downsampledSamples))
        val magnitudes = windowsProcessor.process(preparedWindows)

        return magnitudes
    }

}
