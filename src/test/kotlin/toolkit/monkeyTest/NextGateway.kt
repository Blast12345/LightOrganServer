package toolkit.monkeyTest

import gateway.Gateway
import io.mockk.mockk

fun nextGateway(): Gateway {
    return mockk()
}