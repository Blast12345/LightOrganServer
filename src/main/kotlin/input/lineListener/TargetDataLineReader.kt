package input.lineListener

import javax.sound.sampled.TargetDataLine

class TargetDataLineReader {

    // TODO: This may be able to be made an extension
    // MockK this way: mockkStatic(ClassName::ExtensionFunctionName)
    fun getAvailableData(dataLine: TargetDataLine): ByteArray {
        val bytesToRead = dataLine.available()
        val data = ByteArray(bytesToRead)
        dataLine.read(data, 0, bytesToRead)
        return data
    }

}