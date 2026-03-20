package dsp.filtering

import kotlin.math.abs
import kotlin.math.log10

// Filters produce a transient when starting from the initial state.
// Skip the first portion of the output to let the filter settle, then measure the peak of the steady signal.
fun measureMagnitude(
    samples: FloatArray,
    skipFraction: Float = 0.5f,
): Float {
    val start = (samples.size * skipFraction).toInt()
    return samples.drop(start).maxOf { abs(it) }
}

fun magnitudeToDb(magnitude: Float): Float {
    return 20f * log10(magnitude)
}