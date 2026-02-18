package input.audioDevice

import javax.sound.sampled.AudioSystem

class AudioDeviceFinder(
    private val audioDeviceFactory: AudioDeviceFactory = AudioDeviceFactory()
) {

    fun findAll(): List<AudioDevice> {
        val mixerInfo = AudioSystem.getMixerInfo()
        val mixers = mixerInfo.map { AudioSystem.getMixer(it) }
        return mixers.map { audioDeviceFactory.create(it) }
    }

}