package input.buffer

class InputBuffer(bufferSize: Int) {

    private var samplesBuffer = ByteArray(bufferSize)

    fun updatedWith(samples: ByteArray): ByteArray {
        val rolloverData = samplesBuffer.drop(samples.size).toByteArray()
        samplesBuffer = rolloverData + samples
        return samplesBuffer
    }

}