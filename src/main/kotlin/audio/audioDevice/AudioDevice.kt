package audio.audioDevice

import audio.audioInput.AudioInput

// ENHANCEMENT: Adding outputs would allow built-in routing from a virtual input to speakers.
class AudioDevice(
    val name: String,
    val inputs: List<AudioInput>,
)