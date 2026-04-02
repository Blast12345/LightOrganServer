package dsp.windowing

import kotlin.math.PI
import kotlin.math.cos

class HannWindow : WindowFunction {

    override fun coefficients(sampleSize: Int): FloatArray {
        val lastIndex = sampleSize - 1

        return FloatArray(sampleSize) { index ->
            0.5f * (1f - cos(2f * PI.toFloat() * index / lastIndex))
        }
    }

}