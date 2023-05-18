package input.finders

import javax.sound.sampled.TargetDataLine

class DefaultTargetDataLineFinder(
    private val allInputsFinder: AllInputsFinder = AllInputsFinder()
) {

    fun find(): TargetDataLine {
        val inputs = allInputsFinder.getInputs()
        return inputs.first()
    }

}