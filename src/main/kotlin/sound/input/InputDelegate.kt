package sound.input

import sound.input.samples.AudioSignal

interface InputDelegate {
    fun receivedAudio(audioSignal: AudioSignal)
}