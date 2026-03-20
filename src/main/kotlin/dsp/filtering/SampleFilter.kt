package dsp.filtering

interface SampleFilter {
    val supportedSampleRate: Float
    fun filter(samples: FloatArray): FloatArray
}