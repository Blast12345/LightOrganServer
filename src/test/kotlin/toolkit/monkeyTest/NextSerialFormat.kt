package toolkit.monkeyTest

import serial.SerialFrameFormat

fun nextSerialFrameFormat(): SerialFrameFormat {
    return SerialFrameFormat(
        dataBits = SerialFrameFormat.DataBits.entries.random(),
        parity = SerialFrameFormat.Parity.entries.random(),
        stopBits = SerialFrameFormat.StopBits.entries.random()
    )
}