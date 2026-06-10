package lightOrgan.gateway.serial

import jsonrpc.FakeJsonRpcConnection
import serial.SerialFrameFormat
import toolkit.monkeyTest.nextInt
import toolkit.monkeyTest.nextSerialFrameFormat
import toolkit.monkeyTest.nextString

class FakeSerialJsonRpcConnection : FakeJsonRpcConnection(), SerialJsonRpcConnection {

    override val name: String = nextString("name")
    override val baudRate: Int = nextInt()
    override val frameFormat: SerialFrameFormat = nextSerialFrameFormat()

}