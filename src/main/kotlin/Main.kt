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
    val input = Input(dataLine)
    return LightOrgan(input)
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}