package toolkit.monkeyTest

import io.mockk.every
import io.mockk.mockk
import javax.sound.sampled.Line
import javax.sound.sampled.TargetDataLine

fun nextTargetLineInfo(): Line.Info {
    val info: Line.Info = mockk()
    every { info.lineClass } returns TargetDataLine::class.java
    return info
}