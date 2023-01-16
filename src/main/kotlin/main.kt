import config.Config
import sound.input.Input
import sound.input.finder.InputFinder

fun main() {
    val input = findInput()
    val config = DefaultConfig()
    startLightOrganWith(input, config)
}

private fun findInput(): Input {
    val targetDataLine = InputFinder().getInput()
    return Input(targetDataLine)
}

private fun startLightOrganWith(input: Input, config: Config) {
    val lightOrgan = LightOrgan(config, input)
    lightOrgan.start()
}