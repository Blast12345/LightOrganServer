package wrappers.audioFormat

data class AudioFormatWrapper(
    val sampleRate: Float,
    val nyquistFrequency: Float,
    val numberOfChannels: Int
)
