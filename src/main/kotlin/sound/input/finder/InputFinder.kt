package sound.input.finder

import javax.sound.sampled.TargetDataLine

interface InputFinderInterface {
    fun getInput(): TargetDataLine
}

// TODO: Test me
class InputFinder(private val allInputsFinder: AllInputsFinderInterface = AllInputsFinder()) : InputFinderInterface {

    // NOTE: This could be extended in the future to take a searchable string
    override fun getInput(): TargetDataLine {
        val inputs = allInputsFinder.getInputs()
        return inputs.first()
    }

}