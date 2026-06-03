//package lightOrgan.gateway.serial
//
//import annotations.SkipCoverage
//import color.StandardRgbColor
//import lightOrgan.gateway.Gateway
//import lightOrgan.gateway.GatewayDetails
//import lightOrgan.gateway.serial.messages.GatewayIdentificationResponse
//import serial.rpc.SerialRpcClient
//
//class SerialGateway(
//    override val details: SerialGatewayDetails,
//    private val serialRpcClient: SerialRpcClient,
//) : Gateway {
//
//    override val isConnected = serialRpcClient.isConnected
//
//    override fun connect() {
//        serialRpcClient.connect()
//    }
//
//    override fun disconnect() {
//        serialRpcClient.disconnect()
//    }
//
//    override fun sendColor(color: StandardRgbColor) {
//        TODO()
//    }
//
//}
//
//data class SerialGatewayDetails(
//    override val macAddress: String,
//    override val firmwareVersion: String,
//    val portPath: String
//    // TODO: Baud rate and format?
//) : GatewayDetails
//
//@SkipCoverage
//class SerialGatewayFactory {
//
//    fun create(
//        identity: GatewayIdentificationResponse,
//        client: SerialRpcClient
//    ): SerialGateway {
//        return SerialGateway(
//            details = SerialGatewayDetails(
//                portPath = client.name,
//                macAddress = identity.macAddress,
//                firmwareVersion = identity.firmwareVersion,
//            ),
//            serialRpcClient = client
//        )
//    }
//
//}
