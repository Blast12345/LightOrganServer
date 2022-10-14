package server

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ServerTests {

    private lateinit var timeUtility: FakeTimeUtility
    private lateinit var socket: FakeUdpSocket
    private val message = "fake message"

    @Before
    fun setup() {
        timeUtility = FakeTimeUtility()
        socket = FakeUdpSocket()
    }

    private fun createSUT(): Server {
        return Server(timeUtility, socket)
    }

    @Test
    fun `the server can send a message`() {
        val sut = createSUT()
        sut.sendMessage(message)
        assertEquals(message, socket.message)
        // TODO: Implement me when device handshake is ready
//        assertEquals(message, socket.address)
//        assertEquals(message, socket.port)
    }

    @Test
    fun `a timestamp is updated when a message is sent`() {
        val sut = createSUT()
        sut.sendMessage(message)
        assertEquals(timeUtility.currentTimeInMillisecondsValue, sut.lastMessageTimestampInMilliseconds)
    }

    @Test
    fun `compute the time since the last message`() {
        val sut = createSUT()
        val startTime: Long = 0
        val duration: Long = 100
        val endTime: Long = startTime + duration
        sut.lastMessageTimestampInMilliseconds = startTime
        timeUtility.currentTimeInMillisecondsValue = endTime
        assertEquals(duration, sut.millisecondsSinceLastSentMessage)
    }

}