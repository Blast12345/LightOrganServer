package sound.input.finder

import javax.sound.sampled.Line
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

interface AllInputsFinderInterface {
    fun getInputs(): List<TargetDataLine>
}

class AllInputsFinder(private val allAudioDevicesFinder: AllAudioDevicesFinderInterface = AllAudioDevicesFinder()) :
    AllInputsFinderInterface {

    override fun getInputs(): List<TargetDataLine> {
        return getAudioDevices().flatMap { audioDevice ->
            getTargetDataLinesFor(audioDevice)
        }
    }

    private fun getAudioDevices(): List<Mixer> {
        return allAudioDevicesFinder.getAudioDevices()
    }

    private fun getTargetDataLinesFor(audioDevice: Mixer): List<TargetDataLine> {
        val targetDataLineInfo = getTargetDataLineInfoListFor(audioDevice)
        return targetDataLineInfo.map { audioDevice.getLine(it) as TargetDataLine }
    }

    private fun getTargetDataLineInfoListFor(audioDevice: Mixer): List<Line.Info> {
        val targetLineInfo = audioDevice.targetLineInfo
        return targetLineInfo.filter { it.lineClass == TargetDataLine::class.java }
    }

}