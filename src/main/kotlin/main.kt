import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sound.input.Input
import sound.input.finder.InputFinder

fun main(): Unit = runBlocking {
    launch {
        startLightOrgan()
        keepAlive()
    }
}

private fun startLightOrgan() {
    val dataLine = InputFinder().getInput()
    val input = Input(dataLine)
    val config = DefaultConfig(dataLine.format)
    val lightOrgan = LightOrgan(config, input)
    lightOrgan.start()
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}