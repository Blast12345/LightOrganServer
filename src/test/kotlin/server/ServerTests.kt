package server

import Server
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ServerTests {

    private lateinit var client: FakeClient
    private val message = "fake message"

    @Before
    fun setup() {
        client = FakeClient()
    }

    @Test
    fun `the server can send a message`() {
        val sut = Server()
        sut.sendMessage(message)
        Assert.assertEquals(message, client.getMessage())
    }

    @After
    fun tearDown() {
        client.stop()
    }

}