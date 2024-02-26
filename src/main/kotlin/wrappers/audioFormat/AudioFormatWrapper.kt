package wrappers.audioFormat

data class AudioFormatWrapper(
    // TODO: Effective sample rate
    val sampleRate: Float,
    val nyquistFrequency: Float,
    val numberOfChannels: Int
)
