package input

import audio.samples.AudioFormat

data class AudioInputDetails(
    val name: String,
    val format: AudioFormat,
)