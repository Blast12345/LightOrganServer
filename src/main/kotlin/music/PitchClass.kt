package music

data class PitchClass(
    val name: String, // e.g. "C"
    val positionInOctave: Float // 0.0 (inclusive) to 1.0 (exclusive)
)