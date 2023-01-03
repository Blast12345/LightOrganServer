package sound.frequencyBins

import sound.fft.AmplitudeFactory
import sound.fft.AmplitudeFactoryInterface
import sound.input.samples.NormalizedAudioFrame

interface FrequencyBinsFactoryInterface {
    fun createFrom(audioFrame: NormalizedAudioFrame, lowestSupportedFrequency: Float): FrequencyBins
}

class FrequencyBinsFactory(
    private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory(),
    private val sampleSizeFactory: SampleSizeFactoryInterface = SampleSizeFactory()
) :
    FrequencyBinsFactoryInterface {

    override fun createFrom(
        audioFrame: NormalizedAudioFrame,
        lowestSupportedFrequency: Float
    ): FrequencyBins {
        val sampleRate = getSampleRateFor(audioFrame)
        val sampleSize = getSampleSizeFor(lowestSupportedFrequency, sampleRate)
        val latestSamples = getLatestSamplesFor(sampleSize, audioFrame.samples)
        val amplitudes = getAmplitudesFor(latestSamples)

        return amplitudes.mapIndexed { index, amplitude ->
            getFrequencyBinFor(index, amplitude, sampleSize, sampleRate)
        }
    }

    private fun getSampleRateFor(audioFrame: NormalizedAudioFrame): Float {
        return audioFrame.sampleRate
    }

    private fun getSampleSizeFor(frequency: Float, sampleRate: Float): Int {
        return sampleSizeFactory.createFor(frequency, sampleRate)
    }

    private fun getLatestSamplesFor(sampleSize: Int, samples: DoubleArray): DoubleArray {
        val latestSamples = samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

    private fun getAmplitudesFor(samples: DoubleArray): DoubleArray {
        return amplitudeFactory.createFrom(samples)
    }

    private fun getFrequencyBinFor(index: Int, amplitude: Double, sampleSize: Int, sampleRate: Float): FrequencyBin {
        return FrequencyBin(
            frequency = getFrequencyFor(index, sampleSize, sampleRate),
            amplitude = amplitude
        )
    }

    private fun getFrequencyFor(index: Int, sampleSize: Int, sampleRate: Float): Float {
        return index * sampleRate / sampleSize
    }

}