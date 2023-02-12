package sound.input

import sound.input.samples.AudioSignal

interface InputDelegate {
    fun received(audio: AudioSignal)
}