package lightOrgan.gateway

import gateway.serial.SerialConnection
import gateway.serial.SerialObject
import gateway.serial.SerialRequest
import gateway.serial.SerialResponse
import wrappers.serial.SerialFormat
import wrappers.serial.SerialPort
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GatewayConnector(

) {

    suspend fun connect(port: SerialPort): Gateway? {
        TODO()
    }


//
//    private suspend fun attemptHandshake(port: SerialPort): Gateway? {
//        val client = SerialClient(port, baudRate, serialFormat)
//
//        try {
//            client.connect()
//
//            val handshakeRequest = GatewayIdentificationRequest()
//            val response = client.request(handshakeRequest, GatewayIdentificationResponse::class.java) // TODO: Return type
//
//            return Gateway(
//                systemPath = port.systemPath,
//                macAddress = response.macAddress,
//                firmwareVersion = response.firmwareVersion,
//                serialClient = client
//            )
//        } catch (e: CancellationException) {
//            throw e
//        } catch (e: Exception) {
//            client.disconnect()
//            return null
//        }
//    }


}

class SerialClient(
    private val serialPort: SerialPort,
    // TODO: Make public?
    private val baudRate: Int,
    private val format: SerialFormat
) {


    private val connection = SerialConnection(serialPort, baudRate, format) // TODO:
    val isConnected = connection.isConnected

    fun connect() = connection.connect()
    fun disconnect() = connection.disconnect()

    // TODO: Throw on error
    fun send(obj: SerialObject) {
        TODO()
    }

    // TODO: Throw on error
    suspend fun <Request : SerialRequest, Response : SerialResponse> request(
        request: Request,
        responseClass: Class<Response>,
        timeout: Duration = 100.milliseconds
    ): Response {
        TODO()
    }

}

data class GatewayIdentificationRequest(
    override val requestId: String = UUID.randomUUID().toString(),
) : SerialRequest {

    override val type: String = "gateway-identification"

}

data class GatewayIdentificationResponse(
    override val requestId: String,
    override val type: String = "gateway-identification",
    val macAddress: String,
    val firmwareVersion: String
) : SerialResponse
//class GatewayHandshake {
//
//    suspend fun attempt(client: SerialConnection): GatewayDetails? {
//        return try {
//            val request = IdentificationRequest()
//            val response = client.request(request, IdentificationResponse::class.java)
//            GatewayDetails(response.macAddress, response.firmwareVersion)
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//}

//            val gateway = runCatching {
//                Gateway.connect(port)
//            }.getOrNull()
//
//            if (gateway != null) {
////                gateway.disconnect() // TODO: Be a good little boyscout?
//                return gateway
//            }


//@Test
//fun `given a search was already in progress, then no additional search is started`() = runTest {
//    val sut = createSUT()
//    coEvery { gatewayFinder.find() } coAnswers { delay(1000.milliseconds); gateway }
//
//    // First attempt
//    launch { sut.findGateway() }
//    advanceTimeBy(500.milliseconds)
//
//    // Second attempt
//    sut.findGateway()
//
//    coVerify(exactly = 1) { gatewayFinder.find() }
//}
