import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sound.input.Input
import sound.input.finder.InputFinder

fun main(): Unit = runBlocking {
    launch {
        createLightOrgan()
        keepAlive()
    }
}

private fun createLightOrgan(): LightOrgan {
    val dataLine = InputFinder().getInput()
    val config = DefaultConfig(dataLine.format)
    val input = Input(dataLine, config)
    return LightOrgan(config, input)
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}