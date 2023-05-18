package input.finders

import input.Input
import input.buffer.InputBuffer
import input.buffer.InputBufferFactory
import input.lineListener.LineListener
import input.lineListener.LineListenerFactory

// TODO: Test me
class DefaultInputFactory(
    defaultTargetDataLineFinder: DefaultTargetDataLineFinder = DefaultTargetDataLineFinder(),
    private val lineListenerFactory: LineListenerFactory = LineListenerFactory(),
    private val inputBufferFactory: InputBufferFactory = InputBufferFactory()
) {

    private val targetDataLine = defaultTargetDataLineFinder.find()

    fun create(): Input {
        return Input(
            lineListener = getLineListener(),
            buffer = getBuffer()
        )
    }

    private fun getLineListener(): LineListener {
        return lineListenerFactory.create(targetDataLine)
    }

    private fun getBuffer(): InputBuffer {
        return inputBufferFactory.create(targetDataLine)
    }

}