package input

import input.buffer.InputBufferFactory
import input.lineListener.LineListenerFactory
import input.targetDataLine.DefaultTargetDataLineFinder

class DefaultInputFactory(
    defaultTargetDataLineFinder: DefaultTargetDataLineFinder = DefaultTargetDataLineFinder(),
    private val lineListenerFactory: LineListenerFactory = LineListenerFactory(),
    private val inputBufferFactory: InputBufferFactory = InputBufferFactory()
) {

    private val targetDataLine = defaultTargetDataLineFinder.find()

    fun create(): Input {
        return Input(
            lineListener = lineListenerFactory.create(targetDataLine),
            buffer = inputBufferFactory.create(targetDataLine)
        )
    }

}