package server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color

class ServerTests {

    private lateinit var timeUtility: FakeTimeUtility
    private lateinit var socket: FakeUdpSocket
    private val color = Color.blue

    @BeforeEach
    fun setup() {
        timeUtility = FakeTimeUtility()
        socket = FakeUdpSocket()
    }

    private fun createSUT(): Server {
        return Server(timeUtility, socket)
    }

    @Test
    fun `the server can send a color`() {
        val sut = createSUT()
        sut.sendColor(color)
        assertEquals(socket.message, "${color.red},${color.green},${color.blue}")
        // NOTE: We are assuming this is hardcoded for the MVP.
        assertEquals(socket.address, "192.168.1.55")
        assertEquals(socket.port, 9999)
    }

    @Test
    fun `a timestamp is updated when a color is sent`() {
        val sut = createSUT()
        sut.sendColor(color)
        assertEquals(timeUtility.currentTimeInMillisecondsValue, sut.lastColorTimestampInMilliseconds)
    }

    @Test
    fun `compute the time since the last color`() {
        val sut = createSUT()
        val startTime: Long = 0
        val duration: Long = 100
        val endTime: Long = startTime + duration
        sut.lastColorTimestampInMilliseconds = startTime
        timeUtility.currentTimeInMillisecondsValue = endTime
        assertEquals(duration, sut.millisecondsSinceLastSentColor)
    }

}