package bins.octave

data class OctaveBin(
    val octave: Int,
    val position: Float,
    val magnitude: Float
)

typealias OctaveBins = List<OctaveBin>