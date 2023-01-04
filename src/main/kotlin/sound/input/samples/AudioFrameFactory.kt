package sound.input.samples

import javax.sound.sampled.AudioFormat

interface AudioFrameFactoryInterface {
    fun create(rawSamples: ByteArray, format: AudioFormat): AudioFrame
}

class AudioFrameFactory(
    private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer()
) : AudioFrameFactoryInterface {

    override fun create(rawSamples: ByteArray, format: AudioFormat): AudioFrame {
        return AudioFrame(
            samples = getNormalizedSamples(rawSamples, format),
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