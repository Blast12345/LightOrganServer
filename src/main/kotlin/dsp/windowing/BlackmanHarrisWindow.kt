package dsp.windowing

import kotlin.math.PI
import kotlin.math.cos

class BlackmanHarrisWindow : WindowFunction {

    override fun coefficients(size: Int): FloatArray {
        val lastIndex = size - 1
        return FloatArray(size) { index ->
            val phase = 2f * PI.toFloat() * index / lastIndex
            0.35875f -
                    0.48829f * cos(phase) +
                    0.14128f * cos(2f * phase) -
                    0.01168f * cos(3f * phase)
        }
    }

}