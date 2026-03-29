package toolkit.monkeyTest

import wrappers.sound.InputLine
import kotlin.random.Random.Default.nextBoolean

fun nextInputLineReadResult(): InputLine.ReadResult {
    return InputLine.ReadResult(
        data = nextByteArray(),
        bufferWasFull = nextBoolean(),
    )
}
