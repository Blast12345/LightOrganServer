package lightOrgan.sound.input

import lightOrgan.sound.input.samples.AudioSignal

interface InputDelegate {
    fun received(audio: AudioSignal)
}