package toolkit.monkeyTest

import serial.SerialFrameFormat

fun nextSerialFormat(): SerialFrameFormat {
    return SerialFrameFormat(
        dataBits = nextInt(),
        parity = nextInt(),
        stopBits = nextInt()
    )
}