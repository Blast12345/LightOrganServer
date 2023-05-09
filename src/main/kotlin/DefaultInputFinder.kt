import input.Input
import input.buffer.InputBuffer
import input.finder.InputFinder
import input.lineListener.LineListener

// TODO: Test me?
class DefaultInputFinder {

    fun find(): Input {
        val dataLine = InputFinder().getInput()
        val lineListener = LineListener(dataLine = dataLine)
        val buffer = InputBuffer(bufferSize = dataLine.bufferSize)
        return Input(
            lineListener = lineListener,
            buffer = buffer
        )
    }

}