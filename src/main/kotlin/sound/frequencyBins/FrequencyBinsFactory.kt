package sound.frequencyBins

import sound.input.samples.AudioFrame
import sound.signalProcessing.AmplitudeFactory
import sound.signalProcessing.AmplitudeFactoryInterface

interface FrequencyBinsFactoryInterface {
    fun create(audioFrame: AudioFrame, lowestSupportedFrequency: Float): FrequencyBins
}

class FrequencyBinsFactory(
    private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory(),
    private val sampleSizeCalculator: SampleSizeCalculatorInterface = SampleSizeCalculator()
) : FrequencyBinsFactoryInterface {

    override fun create(audioFrame: AudioFrame, lowestSupportedFrequency: Float): FrequencyBins {
        val sampleRate = getSampleRate(audioFrame)
        val sampleSize = getSampleSize(lowestSupportedFrequency, sampleRate)
        val latestSamples = getLatestSamples(sampleSize, audioFrame.samples)
        val amplitudes = getAmplitudes(latestSamples)

        return amplitudes.mapIndexed { index, amplitude ->
            getFrequencyBin(index, amplitude, sampleSize, sampleRate)
        }
    }

    private fun getSampleRate(audioFrame: AudioFrame): Float {
        return audioFrame.sampleRate
    }

    private fun getSampleSize(frequency: Float, sampleRate: Float): Int {
        return sampleSizeCalculator.calculate(frequency, sampleRate)
    }

    private fun getLatestSamples(sampleSize: Int, samples: DoubleArray): DoubleArray {
        val latestSamples = samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

    private fun getAmplitudes(samples: DoubleArray): DoubleArray {
        return amplitudeFactory.create(samples)
    }

    private fun getFrequencyBin(index: Int, amplitude: Double, sampleSize: Int, sampleRate: Float): FrequencyBin {
        return FrequencyBin(
            frequency = getFrequency(index, sampleSize, sampleRate),
            amplitude = amplitude.toFloat()
        )
    }

    private fun getFrequency(index: Int, sampleSize: Int, sampleRate: Float): Float {
        return index * sampleRate / sampleSize
    }

}