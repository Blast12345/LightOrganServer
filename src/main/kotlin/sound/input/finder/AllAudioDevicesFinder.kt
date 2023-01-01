package sound.input.finder

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer

interface AllAudioDevicesFinderInterface {
    fun getAudioDevices(): List<Mixer>
}

// TODO: Test me
class AllAudioDevicesFinder : AllAudioDevicesFinderInterface {

    override fun getAudioDevices(): List<Mixer> {
        return getMixerInfo().map { AudioSystem.getMixer(it) }
    }

    private fun getMixerInfo(): Array<Mixer.Info> {
        return AudioSystem.getMixerInfo()
    }

}