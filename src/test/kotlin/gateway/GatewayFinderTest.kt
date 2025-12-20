package gateway

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextGateway

class GatewayFinderTest {

    //    val usbDevice1 =
    val expectedGateway = nextGateway()

    private fun createSUT(): GatewayFinder {
        return GatewayFinder()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `find the connected gateway`() {
        val sut = createSUT()

        val gateway = sut.find()

        assertEquals(expectedGateway, gateway)
    }

    @Test
    fun `if no gateway is found, return null`() {
        val sut = createSUT()

        val gateway = sut.find()

        assertNull(gateway)
    }

}