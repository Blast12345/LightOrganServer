package toolkit.monkeyTest

import wrappers.serial.SerialFormat

fun nextSerialFormat(): SerialFormat {
    return SerialFormat(
        bits = nextInt(),
        parity = nextInt(),
        stopBits = nextInt()
    )
}