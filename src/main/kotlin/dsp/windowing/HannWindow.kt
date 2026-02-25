package dsp.windowing

import kotlin.math.PI
import kotlin.math.cos

class HannWindow : WindowFunction {

    override val amplitudeCorrectionFactor = 2f
    override val energyCorrectionFactor = 1.63f

    override fun appliedTo(frame: FloatArray): FloatArray {
        val output = FloatArray(frame.size)
        val lastIndex = frame.size - 1

        for (i in frame.indices) {
            val multiplier = 0.5f * (1f - cos(2f * PI.toFloat() * i / lastIndex))
            output[i] = multiplier * frame[i]
        }

        return output
    }

}
