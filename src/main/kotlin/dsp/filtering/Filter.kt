package dsp.filtering

interface Filter {
    val order: Int
    val sampleRate: Float
    fun filter(samples: FloatArray): FloatArray
    fun magnitudeAt(frequency: Float): Float
}