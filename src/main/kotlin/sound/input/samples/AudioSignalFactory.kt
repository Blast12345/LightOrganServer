package sound.input.samples

import javax.sound.sampled.AudioFormat

interface AudioSignalFactoryInterface {
    fun create(samples: ByteArray, format: AudioFormat): AudioSignal
}

class AudioSignalFactory(
    private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer()
) : AudioSignalFactoryInterface {

    override fun create(samples: ByteArray, format: AudioFormat): AudioSignal {
        return AudioSignal(
            samples = getNormalizedSamples(samples, format),
            sampleRate = getSampleRate(format)
        )
    }

    private fun getNormalizedSamples(rawSamples: ByteArray, format: AudioFormat): DoubleArray {
        return sampleNormalizer.normalize(rawSamples, format)
    }

    private fun getSampleRate(format: AudioFormat): Float {
        return format.sampleRate
    }

}