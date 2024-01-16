package input

import input.audioFrame.AudioFrame

interface InputSubscriber {
    fun received(audioFrame: AudioFrame)
}
