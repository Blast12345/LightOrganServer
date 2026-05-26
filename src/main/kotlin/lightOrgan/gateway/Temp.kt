package lightOrgan.gateway

import annotations.SkipCoverage
import config.ConfigSingleton
import gateway.serial.SerialConnection
import gateway.serial.SerialObject
import gateway.serial.SerialRequest
import gateway.serial.SerialResponse
import wrappers.serial.SerialFormat
import wrappers.serial.SerialPort
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@SkipCoverage
class SerialClientFactory(
    private val baudRate: Int = ConfigSingleton.gateway.baudRate,
    private val format: SerialFormat = ConfigSingleton.gateway.serialFormat
) {

    fun create(port: SerialPort): SerialClient {
        return SerialClient(port, baudRate, format)
    }

}

class SerialClient(
    private val serialPort: SerialPort,
    // TODO: Make public?
    private val baudRate: Int,
    private val format: SerialFormat
) {


    private val connection = SerialConnection(serialPort, baudRate, format) // TODO:
    val isConnected = connection.isConnected

    val systemPath = serialPort.systemPath

    fun connect() = connection.connect()
    fun disconnect() = connection.disconnect()

    // TODO: Throw on error
    fun send(obj: SerialObject) {
        TODO()
    }

    // TODO: Throw on error
    suspend fun <T : SerialResponse> request(request: SerialRequest<T>, timeout: Duration = 5.seconds): T {
        TODO()
    }

}

data class GatewayIdentificationRequest(
    override val requestId: String = UUID.randomUUID().toString(),
) : SerialRequest<GatewayIdentificationResponse> {

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
