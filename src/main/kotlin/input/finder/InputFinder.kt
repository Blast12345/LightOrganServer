package input.finder

import javax.sound.sampled.TargetDataLine

// TODO: InputLineFinder? TargetDataLineFinder?
class InputFinder(
    private val allInputsFinder: AllInputsFinder = AllInputsFinder()
) {

    // NOTE: This could be extended in the future to take a searchable string
    fun getInput(): TargetDataLine {
        val inputs = allInputsFinder.getInputs()
        return inputs.first()
    }

}