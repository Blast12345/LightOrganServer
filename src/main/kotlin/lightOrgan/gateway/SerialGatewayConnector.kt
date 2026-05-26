package lightOrgan.gateway

import wrappers.serial.SerialPort

class SerialGatewayConnector {

    suspend fun connect(port: SerialPort): Gateway? {
        // connect
        // attempt handshake
        // success -> return gateway
        // failure -> disconnect and return null
        TODO()
    }

}