package gateway.serial.wrappers

import com.fazecast.jSerialComm.SerialPort
import wrappers.Wrapper

@Wrapper
data class SerialFormat(
    val bits: Int,
    val parity: Int,
    val stopBits: Int
) {

    @Wrapper
    companion object {
        val FORMAT_8N1 = SerialFormat(8, SerialPort.NO_PARITY, SerialPort.ONE_STOP_BIT)
    }

}