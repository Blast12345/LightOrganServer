package sound.input

import sound.input.samples.NormalizedAudioFrame

interface InputDelegate {
    fun receiveAudioFrame(audioFrame: NormalizedAudioFrame)
}