package lightOrgan.gateway

interface GatewayFinder {
    suspend fun find(): Gateway
}