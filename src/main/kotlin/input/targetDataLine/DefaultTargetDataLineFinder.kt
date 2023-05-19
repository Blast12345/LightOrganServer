package input.targetDataLine

import javax.sound.sampled.TargetDataLine

class DefaultTargetDataLineFinder(
    private val targetDataLinesFinder: TargetDataLinesFinder = TargetDataLinesFinder()
) {

    fun find(): TargetDataLine {
        return findTargetDataLines().first()
    }

    private fun findTargetDataLines(): List<TargetDataLine> {
        return targetDataLinesFinder.find()
    }

}