package sound.frequencyBins

import sound.fft.AmplitudeFactory
import sound.fft.AmplitudeFactoryInterface
import sound.input.samples.NormalizedAudioFrame

interface FrequencyBinsFactoryInterface {
    fun createFrom(audioFrame: NormalizedAudioFrame, lowestSupportedFrequency: Float): FrequencyBins
}

class FrequencyBinsFactory(private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory()) :
    FrequencyBinsFactoryInterface {

    override fun createFrom(
        audioFrame: NormalizedAudioFrame,
        lowestSupportedFrequency: Float
    ): FrequencyBins {
        // TODO: Only pass in necessary samples
        val amplitudes = getAmplitudesFor(audioFrame.samples)
        val sampleSize = audioFrame.samples.count() // TODO: This will need to change
        val sampleRate = audioFrame.sampleRate
        return amplitudes.mapIndexed { index, amplitude ->
            getFrequencyBinFor(index, amplitude, sampleSize, sampleRate)
        }
    }

    private fun getFrequencyBinFor(index: Int, amplitude: Double, sampleSize: Int, sampleRate: Float): FrequencyBin {
        return FrequencyBin(
            frequency = getFrequencyFor(index, sampleSize, sampleRate),
            amplitude = amplitude
        )
    }

    private fun getAmplitudesFor(samples: DoubleArray): DoubleArray {
        return amplitudeFactory.createFrom(samples)
    }

    private fun getFrequencyFor(index: Int, sampleSize: Int, sampleRate: Float): Float {
        return index * sampleRate / sampleSize
    }

//    private fun getLatestSamplesForSupportedFrequency(
//        lowestSupportedFrequency: Float,
//        normalizedAudioFrame: NormalizedAudioFrame
//    ): DoubleArray {
//        val samplesNeeded = samplesNeededFor(lowestSupportedFrequency, normalizedAudioFrame.sampleRate)
//        val latestSamples = normalizedAudioFrame.samples.takeLast(samplesNeeded)
//        return latestSamples.toDoubleArray()
//    }
//
//    private fun samplesNeededFor(lowestSupportedFrequency: Float, sampleRate: Float): Int {
//        // TODO: This is a bit lazy. We need to ensure that it is a power of something.
//        val secondsOfAudioRequired = 1 / lowestSupportedFrequency
//        return (secondsOfAudioRequired * sampleRate).toInt()
//    }

}