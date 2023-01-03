import sound.input.Input
import sound.input.finder.InputFinder

fun main() {
    val input = findInput()
    startLightOrganWith(input)
}

private fun findInput(): Input {
    val targetDataLine = InputFinder().getInput()
    return Input(targetDataLine)
}

private fun startLightOrganWith(input: Input) {
    val lightOrgan = createLightOrganFor(input)
    lightOrgan.start()
}

private fun createLightOrganFor(input: Input): LightOrgan {
    return LightOrgan(input)
}