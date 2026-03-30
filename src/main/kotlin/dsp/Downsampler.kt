package dsp

// TODO: Test me
class Downsampler {

    private var sampleOffset: Int = 0
    private var lastFactor: Int = 0
    private var lastChannels: Int = 0
    private var lastSampleRate: Float = 0f

    fun decimationFactor(frequency: Float, sampleRate: Float): Int {
        return maxOf(1, (frequency / (2 * sampleRate)).toInt())
    }

    fun decimate(samples: FloatArray, factor: Int, sampleRate: Float, channels: Int): FloatArray {
        require(factor >= 1) { "Decimation factor must be positive, got $factor" }
        require(channels >= 1) { "Channel count must be positive, got $channels" }

        if (factor != lastFactor || channels != lastChannels || sampleRate != lastSampleRate) {
            sampleOffset = 0
            lastFactor = factor
            lastChannels = channels
            lastSampleRate = sampleRate
        }

        if (factor == 1) return samples

        val frameSize = channels
        val totalFrames = samples.size / frameSize
        val availableFrames = totalFrames - sampleOffset
        if (availableFrames <= 0) {
            sampleOffset -= totalFrames
            return FloatArray(0)
        }

        val outputFrames = (availableFrames + factor - 1) / factor
        val output = FloatArray(outputFrames * frameSize)

        for (frame in 0 until outputFrames) {
            val sourceFrame = sampleOffset + frame * factor
            for (channel in 0 until channels) {
                output[frame * frameSize + channel] = samples[sourceFrame * frameSize + channel]
            }
        }

        sampleOffset = sampleOffset + outputFrames * factor - totalFrames

        return output
    }

}