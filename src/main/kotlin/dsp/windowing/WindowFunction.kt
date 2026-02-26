package dsp.windowing

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
interface WindowFunction {
    val amplitudeCorrectionFactor: Float
    val energyCorrectionFactor: Float
    fun appliedTo(frame: FloatArray): FloatArray
}
