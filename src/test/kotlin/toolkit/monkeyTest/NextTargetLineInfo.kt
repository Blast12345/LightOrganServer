package toolkit.monkeyTest

import javax.sound.sampled.Line
import javax.sound.sampled.TargetDataLine

fun nextTargetLineInfo(): Line.Info {
    return Line.Info(TargetDataLine::class.java)
}