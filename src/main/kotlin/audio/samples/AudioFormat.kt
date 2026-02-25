package audio.samples

data class AudioFormat(
    val sampleRate: Float,
    val bitDepth: Int,
    val channels: Int
) {

    val nyquistFrequency = sampleRate / 2f

}