package input.finder

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer

class AllAudioDevicesFinder {

    fun getAudioDevices(): List<Mixer> {
        return getMixerInfo().map { AudioSystem.getMixer(it) }
    }

    private fun getMixerInfo(): Array<Mixer.Info> {
        return AudioSystem.getMixerInfo()
    }

}