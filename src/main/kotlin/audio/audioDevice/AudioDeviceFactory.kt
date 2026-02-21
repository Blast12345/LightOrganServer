package audio.audioDevice

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFactory
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

class AudioDeviceFactory(
    private val audioInputFactory: AudioInputFactory = AudioInputFactory()
) {

    fun create(mixer: Mixer): AudioDevice {
        return AudioDevice(
            name = getName(mixer),
            inputs = getInputs(mixer)
        )
    }

    private fun getName(mixer: Mixer): String {
        return mixer.mixerInfo.name ?: "Unknown"
    }

    private fun getInputs(mixer: Mixer): List<AudioInput> {
        val deviceName = getName(mixer)

        val targetLineInfos = mixer.targetLineInfo
        val inputLineInfos = targetLineInfos.filter { it.lineClass == TargetDataLine::class.java }
        val inputLines = inputLineInfos.map { mixer.getLine(it) as TargetDataLine }
        val inputs = inputLines.map {
            audioInputFactory.create(deviceName, it)
        }

        return inputs
    }

}