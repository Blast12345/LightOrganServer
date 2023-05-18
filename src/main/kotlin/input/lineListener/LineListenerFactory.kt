package input.lineListener

import javax.sound.sampled.TargetDataLine

// TODO: Test me
class LineListenerFactory {

    fun create(targetDataLine: TargetDataLine): LineListener {
        return LineListener(
            dataLine = targetDataLine
        )
    }

}