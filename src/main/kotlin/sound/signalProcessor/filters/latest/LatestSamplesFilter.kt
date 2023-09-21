package sound.signalProcessor.filters.latest

import config.ConfigSingleton

class LatestSamplesFilter(
    private val sampleSize: Int = ConfigSingleton.sampleSize
) {

    fun filter(samples: DoubleArray): DoubleArray {
        val latestSamples = samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}