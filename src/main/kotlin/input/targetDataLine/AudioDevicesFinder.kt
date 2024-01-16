package input.targetDataLine

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer

class AudioDevicesFinder {

    fun find(): List<Mixer> {
        return getMixerInfo().map { AudioSystem.getMixer(it) }
    }

    private fun getMixerInfo(): Array<Mixer.Info> {
        return AudioSystem.getMixerInfo()
    }

}
