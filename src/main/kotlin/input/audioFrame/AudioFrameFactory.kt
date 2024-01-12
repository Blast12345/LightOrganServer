package input.audioFrame

import wrappers.audioFormat.AudioFormatWrapper
import wrappers.audioFormat.AudioFormatWrapperFactory
import javax.sound.sampled.AudioFormat

class AudioFrameFactory(
    private val sampleNormalizer: SampleNormalizer = SampleNormalizer(),
    private val audioFormatWrapperFactory: AudioFormatWrapperFactory = AudioFormatWrapperFactory()
) {

    fun create(samples: ByteArray, format: AudioFormat): AudioFrame {
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
