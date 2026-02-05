package gateway.serial.wrappers

import wrappers.Wrapper

@Wrapper
class SerialPortFinder {

    fun find(): List<SerialPort> {
        return com.fazecast.jSerialComm.SerialPort.getCommPorts().map { SerialPortWrapper(it) }
    }

}