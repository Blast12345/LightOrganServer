package sound.input.samples

import wrappers.audioFormat.AudioFormatWrapper
import wrappers.audioFormat.AudioFormatWrapperFactory
import javax.sound.sampled.AudioFormat

interface AudioSignalFactoryInterface {
    fun create(samples: ByteArray, format: AudioFormat): AudioSignal
}

class AudioSignalFactory(
    private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer(),
    private val audioFormatWrapperFactory: AudioFormatWrapperFactory = AudioFormatWrapperFactory()
) : AudioSignalFactoryInterface {

    override fun create(samples: ByteArray, format: AudioFormat): AudioSignal {
        return AudioSignal(
            samples = getNormalizedSamples(samples, format),
            format = getWrappedFormat(format)
        )
    }

    private fun getNormalizedSamples(rawSamples: ByteArray, format: AudioFormat): DoubleArray {
        return sampleNormalizer.normalize(rawSamples, format)
    }

    private fun getWrappedFormat(format: AudioFormat): AudioFormatWrapper {
        return audioFormatWrapperFactory.create(format)
    }

}