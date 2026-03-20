package dsp.filtering

interface OrderedFilter {
    val order: Int
    val supportedSampleRate: Float
    fun filter(samples: FloatArray): FloatArray
}