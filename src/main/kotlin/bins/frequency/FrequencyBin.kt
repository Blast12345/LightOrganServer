package bins.frequency

data class FrequencyBin(
    val frequency: Float,
    val magnitude: Float
)

typealias FrequencyBins = List<FrequencyBin>
