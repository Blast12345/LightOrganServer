import input.Input
import input.buffer.InputBuffer
import input.finder.InputFinder
import input.lineListener.LineListener

class DefaultInputFactory {

    fun create(): Input {
        val dataLine = InputFinder().getInput()
        val lineListener = LineListener(dataLine = dataLine)
        val buffer = InputBuffer(bufferSize = dataLine.bufferSize)
        return Input(
            lineListener = lineListener,
            buffer = buffer
        )
    }

}