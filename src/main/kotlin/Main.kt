import config.ConfigSingleton
import input.DefaultInputFactory
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
    val input = DefaultInputFactory().create()
    return LightOrgan(input)
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}