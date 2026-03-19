package dsp.filtering

interface SampleFilter {
    fun filter(samples: FloatArray): FloatArray
    fun reset()
}