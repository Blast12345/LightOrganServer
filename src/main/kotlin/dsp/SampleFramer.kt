package dsp

import config.ConfigSingleton

class SampleFramer(
    private val frameSize: Int = ConfigSingleton.sampleSize
) {

    fun frame(samples: FloatArray, offset: Int = 0): FloatArray {
        val start = samples.size - frameSize - offset

        if (start < 0) {
            throw InsufficientSamplesException(
                samplesRequired = frameSize + offset,
                samplesProvided = samples.size
            )
        }

        return samples.copyOfRange(start, start + frameSize)
    }

}

class InsufficientSamplesException(
    samplesRequired: Int,
    samplesProvided: Int
) : Exception("Frame requires $samplesRequired samples, but only $samplesProvided were provided.")