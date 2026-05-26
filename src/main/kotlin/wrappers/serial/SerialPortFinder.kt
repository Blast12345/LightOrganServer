package wrappers.serial

import annotations.SkipCoverage

@SkipCoverage
class SerialPortFinder {

    fun find(): List<SerialPort> {
        return com.fazecast.jSerialComm.SerialPort.getCommPorts().map { SerialPortWrapper(it) }
    }

}
