package wrappers.serial

import com.fazecast.jSerialComm.SerialPort

data class SerialFormat(
    val bits: Int,
    val parity: Int,
    val stopBits: Int
) {

    companion object {
        val FORMAT_8N1 = SerialFormat(8, SerialPort.NO_PARITY, SerialPort.ONE_STOP_BIT)
    }

}