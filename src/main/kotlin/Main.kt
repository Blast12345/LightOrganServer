import config.ConfigSingleton
import input.Input
import input.buffer.InputBuffer
import input.finders.DefaultTargetDataLineFinder
import input.lineListener.LineListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lightOrgan.LightOrgan
import server.Server

fun main(): Unit = runBlocking {
    launch {
        val lightOrgan = createLightOrgan()

        val server = Server(ConfigSingleton.clients)
        lightOrgan.addSubscriber(server)

        keepAlive()
    }
}

private fun createLightOrgan(): LightOrgan {
    val dataLine = DefaultTargetDataLineFinder().find()
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