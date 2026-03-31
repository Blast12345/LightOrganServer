package audio.samples

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

// TODO: Magnitude typealias with toDBFS() and vice versa dbfs.toMagnitude()
// TODO: Replace float arrays with this
class Samples(val values: FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Samples) return false
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int = values.contentHashCode()
}