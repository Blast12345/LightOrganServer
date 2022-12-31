import sound.input.Input
import sound.input.InputFinder

fun main() {
    val input = findInput()
    startLightOrganWith(input)
}

private fun findInput(): Input {
    // TODO: Improve me
    val targetDataLine = InputFinder().getInput()
    return Input(targetDataLine)
}

private fun startLightOrganWith(input: Input) {
    val lightOrgan = LightOrgan(input)
    lightOrgan.start()
}