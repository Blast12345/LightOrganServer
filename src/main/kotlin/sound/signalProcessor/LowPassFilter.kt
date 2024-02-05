import kotlin.math.max

class LowPassFilter {

    // TODO: Test me
    fun filter(samples: DoubleArray, frequency: Float): DoubleArray {
        val windowSize = (samples.size * frequency).toInt()
        val filteredSamples = DoubleArray(samples.size)

        for (i in samples.indices) {
            val start = max(0, i - windowSize + 1)
            val end = i + 1
            filteredSamples[i] = samples.slice(start until end).average()
        }

        return filteredSamples
    }

}
