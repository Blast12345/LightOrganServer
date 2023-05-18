package input.buffer

import javax.sound.sampled.TargetDataLine

// TODO: Test me
class InputBufferFactory {

    fun create(targetDataLine: TargetDataLine): InputBuffer {
        return InputBuffer(
            bufferSize = targetDataLine.bufferSize
        )
    }

}