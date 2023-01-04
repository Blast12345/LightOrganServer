package sound.input

import sound.input.samples.AudioFrame

interface InputDelegate {
    fun receiveAudioFrame(audioFrame: AudioFrame)
}