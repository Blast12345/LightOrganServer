package dsp.filtering

interface SampleFilter {
    val sampleRate: Float
    fun filter(samples: FloatArray): FloatArray
    fun reset()
}