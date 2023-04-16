package input

import input.samples.AudioSignal

interface InputDelegate {
    fun received(audio: AudioSignal)
}