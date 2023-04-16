package input

import input.audioFrame.AudioFrame

interface InputSubscriber {
    fun received(audio: AudioFrame)
}