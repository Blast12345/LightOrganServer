package input.targetDataLine

import javax.sound.sampled.Line
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

class TargetDataLinesFinder(
    private val audioDevicesFinder: AudioDevicesFinder = AudioDevicesFinder()
) {

    fun find(): List<TargetDataLine> {
        return getAudioDevices().flatMap { audioDevice ->
            getTargetDataLines(audioDevice)
        }
    }

    private fun getAudioDevices(): List<Mixer> {
        return audioDevicesFinder.find()
    }

    private fun getTargetDataLines(audioDevice: Mixer): List<TargetDataLine> {
        val targetDataLineInfo = getTargetDataLineInfoList(audioDevice)
        return targetDataLineInfo.map { audioDevice.getLine(it) as TargetDataLine }
    }

    private fun getTargetDataLineInfoList(audioDevice: Mixer): List<Line.Info> {
        val targetLineInfo = audioDevice.targetLineInfo
        return targetLineInfo.filter { it.lineClass == TargetDataLine::class.java }
    }

}
