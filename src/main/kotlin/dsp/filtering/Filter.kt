package dsp.filtering

interface Filter {
    val order: Int
    val sampleRate: Float
    fun filter(samples: FloatArray): FloatArray
    fun magnitudeAt(frequency: Float): Float
}

// TODO: Move me?
interface LowPassFilter : Filter {
    val cutoffFrequency: Float
    fun frequencyAtMagnitude(magnitudeDb: Float): Float
}

interface HighPassFilter : Filter {
    val cutoffFrequency: Float
    fun frequencyAtMagnitude(magnitudeDb: Float): Float
}