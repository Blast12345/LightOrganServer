package sound.bins.frequency.listCalculator

import com.github.psambit9791.jdsp.filter.Butterworth
import config.Config
import config.ConfigSingleton
import sound.signalProcessor.AudioTrimmer
import sound.signalProcessor.Downsampler
import sound.signalProcessor.OverlapAdd
import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer
import sound.signalProcessor.filters.window.HannFilter
import sound.signalProcessor.filters.window.WindowFilter
import sound.signalProcessor.interpolator.ZeroPaddingInterpolator
import wrappers.audioFormat.AudioFormatWrapper

class MagnitudeListCalculator(
    private val config: Config = ConfigSingleton,
    private val audioTrimmer: AudioTrimmer = AudioTrimmer(),
    private val downsampler: Downsampler = Downsampler(),
    private val overlapAdd: OverlapAdd = OverlapAdd(),
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = RelativeMagnitudeListCalculator(),
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = RelativeMagnitudeListNormalizer(),
    private val hannFilter: WindowFilter = HannFilter(),
    private val zeroPaddingInterpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator()
) {

    fun calculateNew(samples: DoubleArray, format: AudioFormatWrapper): DoubleArray {
        val trimmedSamples = audioTrimmer.trim(samples, config.sampleSize)
        val hannSamples = hannFilter.applyTo(trimmedSamples)
        val interpolatedSamples = zeroPaddingInterpolator.interpolate(hannSamples)
//        val bandpassedSamples = applyBandPassFilter(interpolatedSamples, format.sampleRate)
        val magnitudesForWindow = relativeMagnitudeListCalculator.calculate(interpolatedSamples)
        val magnitudes = relativeMagnitudeListNormalizer.normalize(magnitudesForWindow, magnitudesForWindow.size)

        return magnitudes
    }

    fun applyBandPassFilter(samples: DoubleArray, sampleRate: Float): DoubleArray {
        val order = 4 // Order of the filter
        val lowCutOff = 20.0 // Lower Cut-off Frequency
        val highCutOff = 120.0 // Higher Cut-off Frequency

        // Create the Butterworth filter
        val flt = Butterworth(sampleRate.toDouble())

        // Apply the bandpass filter
        return flt.bandPassFilter(samples, order, lowCutOff, highCutOff)
    }

}
