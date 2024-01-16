package sound.signalProcessor.filters.latest

import config.ConfigSingleton

class LatestSamplesFilter(
    private val sampleSize: Int = ConfigSingleton.sampleSize
) {

    fun filter(samples: DoubleArray): DoubleArray {
        return samples
            .takeLast(sampleSize)
            .toDoubleArray()
    }

}
