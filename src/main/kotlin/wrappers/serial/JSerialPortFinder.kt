package wrappers.serial

import config.ConfigSingleton
import serial.SerialPort
import serial.SerialPortFinder

class JSerialPortFinder : SerialPortFinder {

    override fun find(): List<SerialPort> {
        return com.fazecast.jSerialComm.SerialPort.getCommPorts().map {
            JSerialCommPort(
                it.systemPortName,
                ConfigSingleton.gateway.baudRate,
                ConfigSingleton.gateway.frameFormat
            )
        }
    }

}
