package input.lineListener

import javax.sound.sampled.TargetDataLine

class TargetDataLineReader {

    fun getAvailableData(dataLine: TargetDataLine): ByteArray {
        val bytesToRead = dataLine.available()
        val data = ByteArray(bytesToRead)
        dataLine.read(data, 0, bytesToRead)
        return data
    }

}