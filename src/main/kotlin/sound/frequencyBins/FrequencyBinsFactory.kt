package sound.frequencyBins

import sound.fft.AmplitudeFactory
import sound.fft.AmplitudeFactoryInterface
import sound.input.samples.NormalizedAudioFrame

interface FrequencyBinsFactoryInterface {
    fun createFrom(normalizedAudioFrame: NormalizedAudioFrame, lowestSupportedFrequency: Float): FrequencyBins
}

class FrequencyBinsFactory(private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory()) :
    FrequencyBinsFactoryInterface {

    override fun createFrom(
        normalizedAudioFrame: NormalizedAudioFrame,
        lowestSupportedFrequency: Float
    ): FrequencyBins {
        // TODO: Only pass in necessary samples
//        val latestSamplesForSupportedFrequency = samplesFor(lowestSupportedFrequency, normalizedAudioFrame)
//        val amplitudes = amplitudeFactory.createFrom(latestSamplesForSupportedFrequency)
        val amplitudes = doubleArrayOf(1.2)

        val frequencyBins = mutableListOf<FrequencyBin>()

        for (i in amplitudes.indices) {
            // TODO: Frequency may need to be divided by 2 in order to get the Nyquist Frequency
            // https://www.mixinglessons.com/sample-rate/
            // TODO: Why times two? To make up for the halving of amplitudes?
            val frequency = i * 2.0//sampleRate.toDouble() / sampleSize.toDouble()
            val amplitude = amplitudes[i]
            val frequencyBin = FrequencyBin(frequency, amplitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

    private fun getLatestSamplesForSupportedFrequency(
        lowestSupportedFrequency: Float,
        normalizedAudioFrame: NormalizedAudioFrame
    ): DoubleArray {
        val samplesNeeded = samplesNeededFor(lowestSupportedFrequency, normalizedAudioFrame.sampleRate)
        val latestSamples = normalizedAudioFrame.samples.takeLast(samplesNeeded)
        return latestSamples.toDoubleArray()
    }

    private fun samplesNeededFor(lowestSupportedFrequency: Float, sampleRate: Float): Int {
        // TODO: This is a bit lazy. We need to ensure that it is a power of something.
        val secondsOfAudioRequired = 1 / lowestSupportedFrequency
        return (secondsOfAudioRequired * sampleRate).toInt()
    }

}