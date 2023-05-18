package input

import input.buffer.InputBufferFactory
import input.lineListener.LineListenerFactory
import input.targetDataLine.DefaultTargetDataLineFinder
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Test

class DefaultInputFactoryTests {

    private val defaultTargetDataLineFinder: DefaultTargetDataLineFinder = mockk()
    private val lineListenerFactory: LineListenerFactory = mockk()
    private val inputBufferFactory: InputBufferFactory = mockk()

    private fun createSUT(): DefaultInputFactory {
        return DefaultInputFactory(
            defaultTargetDataLineFinder = defaultTargetDataLineFinder,
            lineListenerFactory = lineListenerFactory,
            inputBufferFactory = inputBufferFactory
        )
    }

    @Test
    fun `the line listener is created from the default target data line`() {
        mockkConstructor(Input::class)
        createSUT()
        verify { anyConstructed<Input>() }
    }

    @Test
    fun `the buffer is created from the default target data line`() {

    }


//            private fun getLineListener(): LineListener {
//        return lineListenerFactory.create(targetDataLine)
//    }
//
//    private fun getBuffer(): InputBuffer {
//        return inputBufferFactory.create(targetDataLine)
//    }
}