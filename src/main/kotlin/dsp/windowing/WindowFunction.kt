package dsp.windowing

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
// Reference: https://dsp.stackexchange.com/questions/8317/fft-amplitude-or-magnitude
// I'm using the term "magnitude" instead of "amplitude" because the value is inherently non-negative.
interface WindowFunction {
    val magnitudeCorrectionFactor: Float
    val energyCorrectionFactor: Float
    fun appliedTo(frame: FloatArray): FloatArray
}
