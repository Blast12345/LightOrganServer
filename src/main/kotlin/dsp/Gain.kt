package dsp

import kotlin.math.pow

class Gain {

    fun apply(samples: FloatArray, decibels: Float): FloatArray {
        val linearGain = 10f.pow(decibels / 20f)

        return samples.map { it * linearGain }.toFloatArray()
    }

}