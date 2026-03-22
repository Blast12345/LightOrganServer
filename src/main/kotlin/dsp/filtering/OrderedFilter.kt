package dsp.filtering

interface OrderedFilter {
    val order: Int
    val sampleRate: Float
    fun filter(samples: FloatArray): FloatArray
}