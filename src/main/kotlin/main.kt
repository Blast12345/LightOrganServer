import config.Config
import sound.input.Input
import sound.input.finder.InputFinder
import javax.sound.sampled.TargetDataLine

fun main() {
    val dataLine = findDataLine()
    val input = Input(dataLine)
    val config = DefaultConfig(dataLine.format)
    startLightOrganWith(input, config)
}

private fun findDataLine(): TargetDataLine {
    return InputFinder().getInput()
}

private fun startLightOrganWith(input: Input, config: Config) {
    val lightOrgan = LightOrgan(config, input)
    lightOrgan.start()
}