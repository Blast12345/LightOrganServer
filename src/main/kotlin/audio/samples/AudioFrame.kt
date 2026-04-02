package audio.samples

// ENHANCEMENT: Introduce Samples type to make equality cleaner
data class AudioFrame(
    val samples: FloatArray,
    val format: AudioFormat
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AudioFrame) return false
        return format == other.format && samples.contentEquals(other.samples)
    }

    // Generated automatically
    override fun hashCode(): Int {
        var result = samples.contentHashCode()
        result = 31 * result + format.hashCode()
        return result
    }

}