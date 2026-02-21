package input.samples

data class AudioFormat(
    val sampleRate: Int,
    val bitDepth: Int,
    val channels: Int
) {

    val nyquistFrequency: Float = sampleRate / 2f

}