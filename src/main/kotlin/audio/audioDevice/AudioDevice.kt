package audio.audioDevice

import audio.audioInput.AudioInput

class AudioDevice(
    val name: String,
    val inputs: List<AudioInput>,
)