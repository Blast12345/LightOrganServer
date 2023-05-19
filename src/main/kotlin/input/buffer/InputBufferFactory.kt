package input.buffer

import javax.sound.sampled.TargetDataLine

class InputBufferFactory {

    fun create(targetDataLine: TargetDataLine): InputBuffer {
        return InputBuffer(
            bufferSize = targetDataLine.bufferSize
        )
    }

}