package lightOrgan.gateway

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

data class GatewayManagerFixture(
    val mock: GatewayManager,
    val gatewayDetailsFlow: MutableStateFlow<GatewayDetails?>,
    val isConnectedFlow: MutableStateFlow<Boolean>
) {

    companion object {
        fun create(): GatewayManagerFixture {
            val fixture = GatewayManagerFixture(
                mock = mockk<GatewayManager>(),
                gatewayDetailsFlow = MutableStateFlow(null),
                isConnectedFlow = MutableStateFlow(false)
            )

            every { fixture.mock.gatewayDetails } returns fixture.gatewayDetailsFlow
            every { fixture.mock.isConnected } returns fixture.isConnectedFlow
            coEvery { fixture.mock.findGateway() } returns Unit

            return fixture
        }
    }

}