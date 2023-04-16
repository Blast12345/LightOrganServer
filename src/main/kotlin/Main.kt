import input.Input
import input.buffer.InputBuffer
import input.finder.InputFinder
import input.lineListener.LineListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        createLightOrgan()
        keepAlive()
    }
}

private fun createLightOrgan(): LightOrgan {
    val dataLine = InputFinder().getInput()
    val lineListener = LineListener(dataLine = dataLine)
    val buffer = InputBuffer(bufferSize = dataLine.bufferSize)
    val input = Input(
        lineListener = lineListener,
        buffer = buffer
    )
    return LightOrgan(input)
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}