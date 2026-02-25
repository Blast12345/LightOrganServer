package dsp.fft

data class FrequencyBin(
    val frequency: Float,
    val magnitude: Float
)

typealias FrequencyBins = List<FrequencyBin>
