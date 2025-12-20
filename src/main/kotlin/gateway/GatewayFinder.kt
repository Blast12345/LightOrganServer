package gateway

import JsonMapper
import com.fazecast.jSerialComm.SerialPort
import gateway.serial.GatewayIdentificationRequest
import gateway.serial.GatewayIdentificationResponse
import tools.jackson.module.kotlin.readValue
import java.io.ByteArrayOutputStream

class GatewayFinder {

    fun find(): Gateway? {
        val allPorts = SerialPort.getCommPorts()

        val gatewayPort = allPorts.firstOrNull {
            isGatewayPort(it)
        }

        return if (gatewayPort != null) {
            Gateway(gatewayPort)
        } else {
            null
        }
    }

    private fun isGatewayPort(port: SerialPort): Boolean {
        // Configure port
        port.baudRate = 115200
        port.numDataBits = 8
        port.numStopBits = SerialPort.ONE_STOP_BIT
        port.parity = SerialPort.NO_PARITY

        port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 250, 250)
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 250, 250)

        // open port
        port.openPort()

        if (!port.isOpen) {
            return false
        }

        // send identification request
        port.flushIOBuffers()

        val request = GatewayIdentificationRequest()
        val requestLine = JsonMapper.writeValueAsString(request) + "\n"
        val requestBytes = requestLine.toByteArray(Charsets.UTF_8)

        val written = port.writeBytes(requestBytes, requestBytes.size)

        if (written == -1) return false

        // read response
        val responseJson = readLine(port)

        port.closePort()

        if (responseJson != null) {
            try {
                val response: GatewayIdentificationResponse = JsonMapper.readValue(responseJson)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }

    fun readLine(port: SerialPort): String? {
        val line = ByteArrayOutputStream()
        val one = ByteArray(1)

        while (true) {
            val n = port.readBytes(one, 1)
            if (n <= 0) return null // timeout / no data

            if (one[0] == '\n'.code.toByte()) break
            line.write(one[0].toInt())
        }

        if (line.size() == 0) return null

        return line.toString(Charsets.UTF_8.name()).trim()
    }

}