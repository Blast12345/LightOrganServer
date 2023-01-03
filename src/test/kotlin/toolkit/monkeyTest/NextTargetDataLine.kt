package toolkit.monkeyTest

import io.mockk.mockk
import javax.sound.sampled.TargetDataLine

fun nextTargetDataLine(): TargetDataLine {
    return mockk()
}