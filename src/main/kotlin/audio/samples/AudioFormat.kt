package audio.samples

data class AudioFormat(
    val sampleRate: Float,
    val bitDepth: Int,
    val channels: Int
) {

    val nyquistFrequency: Float = sampleRate / 2f // TODO: Is this used?

}