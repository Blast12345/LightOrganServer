package input.lineListener

import javax.sound.sampled.TargetDataLine

class LineListenerFactory {

    fun create(targetDataLine: TargetDataLine): LineListener {
        return LineListener(
            dataLine = targetDataLine
        )
    }

}