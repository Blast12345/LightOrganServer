package input.targetDataLine

import javax.sound.sampled.TargetDataLine

class DefaultTargetDataLineFinder(
    private val targetDataLinesFinder: TargetDataLinesFinder = TargetDataLinesFinder()
) {

    fun find(): TargetDataLine {
        val inputs = targetDataLinesFinder.find()
        return inputs.first()
    }

}