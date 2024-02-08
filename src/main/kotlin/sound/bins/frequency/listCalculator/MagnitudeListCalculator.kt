package sound.bins.frequency.listCalculator

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
        val magnitudes = calculateMagnitudes(hannSamples, format.sampleRate.toDouble() * format.numberOfChannels, 0.0, 150.0, 1.0)

        return magnitudes
    }

    fun calculateMagnitudes(samples: DoubleArray, sampleRate: Double, minFrequency: Double, maxFrequency: Double, stepSize: Double): DoubleArray {
        val frequencies = generateSequence(minFrequency) { it + stepSize }.takeWhile { it <= maxFrequency }
        return frequencies.map { goertzel(samples, sampleRate, it) / 2205 }.toList().toDoubleArray()
    }

    fun goertzel(samples: DoubleArray, sampleRate: Double, targetFrequency: Double): Double {
        val coeff = 2.0 * kotlin.math.cos(2.0 * kotlin.math.PI * targetFrequency / sampleRate)
        var sPrev = 0.0
        var sPrev2 = 0.0

        for (sample in samples) {
            val s = sample + coeff * sPrev - sPrev2
            sPrev2 = sPrev
            sPrev = s
        }

        val power = sPrev2 * sPrev2 + sPrev * sPrev - coeff * sPrev * sPrev2
        return kotlin.math.sqrt(power)
    }

}
