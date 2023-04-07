package lightOrgan.sound.input.finder

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer

interface AllAudioDevicesFinderInterface {
    fun getAudioDevices(): List<Mixer>
}

class AllAudioDevicesFinder : AllAudioDevicesFinderInterface {

    override fun getAudioDevices(): List<Mixer> {
        return getMixerInfo().map { AudioSystem.getMixer(it) }
    }

    private fun getMixerInfo(): Array<Mixer.Info> {
        return AudioSystem.getMixerInfo()
    }

}