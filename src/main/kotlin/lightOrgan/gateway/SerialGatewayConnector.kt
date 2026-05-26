package lightOrgan.gateway

import kotlin.coroutines.cancellation.CancellationException

class SerialGatewayConnector(
    private val gatewayFactory: SerialGatewayFactory = SerialGatewayFactory()
) {

    suspend fun connect(client: SerialClient): SerialGateway? {
        try {
            client.connect()

            val handshakeRequest = GatewayIdentificationRequest()
            val response = client.request(handshakeRequest)

            return gatewayFactory.create(response, client)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            client.disconnect()
        }

        return null
    }

}