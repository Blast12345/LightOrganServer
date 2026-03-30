package audio.samples

data class AudioFormat(
    val sampleRate: Float, // TODO: Maybe make sample rate a type? It can contain the nyquist frequency
    val bitDepth: Int,
    val channels: Int
) {

    val nyquistFrequency = sampleRate / 2f

}