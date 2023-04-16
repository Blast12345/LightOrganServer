package input.audioFrame

import wrappers.audioFormat.AudioFormatWrapper
import wrappers.audioFormat.AudioFormatWrapperFactory
import javax.sound.sampled.AudioFormat

interface AudioFrameFactoryInterface {
    fun create(samples: ByteArray, format: AudioFormat): AudioFrame
}

class AudioFrameFactory(
    private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer(),
    private val audioFormatWrapperFactory: AudioFormatWrapperFactory = AudioFormatWrapperFactory()
) : AudioFrameFactoryInterface {

    override fun create(samples: ByteArray, format: AudioFormat): AudioFrame {
        return AudioFrame(
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