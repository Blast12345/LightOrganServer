package gateway.serial.wrappers

import annotations.Wrapper

@Wrapper
class SerialPortFinder {

    fun find(): List<SerialPort> {
        return com.fazecast.jSerialComm.SerialPort.getCommPorts().map { SerialPortWrapper(it) }
    }

}