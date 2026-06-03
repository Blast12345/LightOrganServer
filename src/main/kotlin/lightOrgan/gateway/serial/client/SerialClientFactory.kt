//package lightOrgan.gateway.serial.client
//
//import annotations.SkipCoverage
//import config.ConfigSingleton
//import lightOrgan.gateway.serial.SerialConnection
//import serial.SerialFrameFormat
//import serial.SerialPort
//import serial.rpc.DefaultSerialRpcClient
//import serial.rpc.SerialRpcClient
//
//@SkipCoverage
//class SerialClientFactory(
//    private val baudRate: Int = ConfigSingleton.gateway.baudRate,
//    private val format: SerialFrameFormat = ConfigSingleton.gateway.frameFormat
//) {
//
//    fun create(port: SerialPort): SerialRpcClient {
//        val connection = SerialConnection(port, baudRate, format)
//        return DefaultSerialRpcClient(connection)
//    }
//
//}