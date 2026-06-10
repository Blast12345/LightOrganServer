package wrappers.serial

import config.ConfigSingleton
import serial.SerialPort
import serial.SerialPortFinder

class JSerialPortFinder : SerialPortFinder {

    override fun find(): List<SerialPort> {
        return com.fazecast.jSerialComm.SerialPort.getCommPorts().map {
            JSerialCommPort(
                port = it,
                ConfigSingleton.gateway.baudRate,
                ConfigSingleton.gateway.frameFormat
            )
        }
    }

}
