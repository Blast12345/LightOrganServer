package sound.input

import javax.sound.sampled.TargetDataLine

interface AllInputsFinderInterface {
    fun getInputs(): List<TargetDataLine>
}

// TODO: Test me
class AllInputsFinder(private val allAudioDevicesFinder: AllAudioDevicesFinderInterface = AllAudioDevicesFinder()) :
    AllInputsFinderInterface {

    override fun getInputs(): List<TargetDataLine> {
        var inputs: MutableList<TargetDataLine> = mutableListOf()
        val audioDevices = allAudioDevicesFinder.getAudioDevices()

        for (audioDevice in audioDevices) {
            val targetLineInfo = audioDevice.targetLineInfo
            val targetDataLineInfo = targetLineInfo.filter { it.lineClass == TargetDataLine::class.java }
            val targetDataLines =
                targetDataLineInfo.map { audioDevice.getLine(it) as TargetDataLine } // TODO: Unwrap gracefully
            inputs.addAll(targetDataLines)
        }

        return inputs
    }

}