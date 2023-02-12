package sound.input

import javax.sound.sampled.AudioFormat

interface TargetDataLineListenerDelegate {
    fun received(newSamples: ByteArray, format: AudioFormat)
}