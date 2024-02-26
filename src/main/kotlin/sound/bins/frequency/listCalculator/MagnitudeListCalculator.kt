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
        var startTime = System.currentTimeMillis()

        val effectiveSampleRate = format.sampleRate.toDouble() * format.numberOfChannels
        // Option 1
        val trimmedSamples = audioTrimmer.trim(samples, config.sampleSize)
        val hannSamples = hannFilter.applyTo(trimmedSamples)
        var magnitudes = calculateMagnitudes(hannSamples, effectiveSampleRate, 0.0, 150.0, ConfigSingleton.step)

        // Option 2
//        val octaves = arrayOf(0, 1, 2)
//        val lowestFrequency = Notes.C.getFrequency(octaves.min()).toInt()
//        val highestFrequency = Notes.C.getFrequency(octaves.max() + 1).toInt()
//        val cycles = 4F
//        var magnitudes = DoubleArray(lowestFrequency) { 0.0 }
//        var magnitudeSegments = mutableListOf<DoubleArray>()
//
//        for (octave in octaves) {
//            val octaveStartFrequency = Notes.C.getFrequency(octave)
//            val startFrequencyDuration = 1 / octaveStartFrequency
//            val startFrequencySamples = startFrequencyDuration * effectiveSampleRate
//            val samplesToCollect = (startFrequencySamples * cycles).toInt()
//
//            val trimmedSamples = audioTrimmer.trim(samples, samplesToCollect)
//            val hannSamples = hannFilter.applyTo(trimmedSamples)
//            val octaveMagnitudes = calculateMagnitudes(hannSamples, effectiveSampleRate, lowestFrequency.toDouble(), highestFrequency.toDouble(), 1.0)
//            magnitudeSegments.add(octaveMagnitudes)
//        }
//
//        val combinedMagnitudes = magnitudeSegments[0].clone()
//
//        // Iterate over the rest of the segments
//        for (i in 0 until magnitudeSegments.size) {
//            // For each index, select the minimum value between the current combined magnitude and the magnitude in the current segment
//            for (j in combinedMagnitudes.indices) {
////                combinedMagnitudes[j] = max(combinedMagnitudes[j], magnitudeSegments[i][j])
//                combinedMagnitudes[j] = (combinedMagnitudes[j] + magnitudeSegments[i][j]) / 2.0
//            }
//        }
//
//        magnitudes += combinedMagnitudes

        // Option 3 - megaboi
//        val magnitudes = MutableList(120) { 0.0 }
//
//        val jobs = List(120) { f ->
//            GlobalScope.launch {
//                if (f < 20) {
//                    magnitudes[f] = 0.0
//                } else {
//                    val startFrequencyDuration = 1 / f.toFloat()
//
//                    val startFrequencySamples = startFrequencyDuration * (format.sampleRate * 2)
//
//                    val samplesToCollect = (startFrequencySamples * 4).toInt()
//                    val realFrequency = 1 / samplesToCollect.toDouble() * 4.0 * (format.sampleRate * 2)
//
//                    val trimmedSamples = audioTrimmer.trim(samples, samplesToCollect)
//                    val hannSamples = hannFilter.applyTo(trimmedSamples)
//                    val magnitude = goertzel(hannSamples, format.sampleRate.toDouble() * format.numberOfChannels, realFrequency)
//                    magnitudes[f] = (magnitude / hannSamples.size)
//                }
//            }
//        }
//
//        runBlocking {
//            jobs.forEach { it.join() }
//        }

        var endTime = System.currentTimeMillis()
        println("Time to calculate magnitudes: ${endTime - startTime}ms")
        return magnitudes//.toDoubleArray()
    }

    fun calculateMagnitudes(samples: DoubleArray, sampleRate: Double, minFrequency: Double, maxFrequency: Double, stepSize: Double): DoubleArray {
        val frequencies = generateSequence(minFrequency) { it + stepSize }.takeWhile { it <= maxFrequency }
        return frequencies.map { goertzel(samples, sampleRate, it) / samples.size }.toList().toDoubleArray()
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
