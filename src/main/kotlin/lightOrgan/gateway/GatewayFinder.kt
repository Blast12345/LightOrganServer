package lightOrgan.gateway

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import wrappers.serial.SerialPortFinder


class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = SerialPortFinder(),
    private val searchLock: Mutex = sharedLock
) {

    companion object {
        val sharedLock = Mutex() // TODO: Private?
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching: Flow<Boolean> = _isSearching

    // TODO: There should only be a single find operation GLOBALLY happening at a time; how to enforce?
    // TODO: Prioritize last known path
    suspend fun find(): Gateway? {
        val allPorts = serialPortFinder.find()

        for (port in allPorts) {

        }

        return null
    }

}

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
