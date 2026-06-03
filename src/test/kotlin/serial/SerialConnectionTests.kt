//package serial
//
//import io.mockk.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.test.runCurrent
//import kotlinx.coroutines.test.runTest
//import lightOrgan.gateway.serial.SerialConnection
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import toolkit.extensions.collectInto
//import toolkit.monkeyTest.nextException
//import toolkit.monkeyTest.nextInt
//import toolkit.monkeyTest.nextSerialFormat
//import toolkit.monkeyTest.nextString
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class SerialConnectionTests {
//
//    private val serialPort: SerialPort = mockk()
//    private val portPath = nextString("port")
//    private val baudRate = nextInt()
//    private val format = nextSerialFormat()
//
//    private val lineChannel = Channel<String>(Channel.UNLIMITED)
//
//    @BeforeEach
//    fun setUpHappyPath() {
//        every { serialPort.portPath } returns portPath
//        every { serialPort.open(baudRate, format) } returns Unit
//        every { serialPort.writeLine(any()) } returns Unit
//        coEvery { serialPort.readNextLine() } coAnswers { lineChannel.receive() }
//        every { serialPort.close() } returns Unit
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(scope: CoroutineScope): SerialConnection {
//        return SerialConnection(
//            serialPort = serialPort,
//            baudRate = baudRate,
//            format = format,
//            scope = scope,
//        )
//    }
//
//    // Connect
//    @Test
//    fun `connect to the serial port`() = runTest {
//        val sut = createSUT(backgroundScope)
//
//        sut.connect()
//
//        verify { serialPort.open(baudRate, format) }
//    }
//
//    @Test
//    fun `given a current connection, calling connect does not attempt to reconnect`() = runTest {
//        val sut = createSUT(backgroundScope)
//
//        // initial attempt
//        sut.connect()
//        runCurrent()
//
//        // redundant attempt
//        sut.connect()
//        runCurrent()
//
//        verify(exactly = 1) { serialPort.open(baudRate, format) }
//    }
//
//    // Sending
//    @Test
//    fun `send a line to the serial port`() = runTest {
//        val sut = createSUT(backgroundScope)
//        sut.connect()
//        runCurrent()
//
//        val line = nextString()
//        sut.write(line)
//
//        verify { serialPort.writeLine(line) }
//    }
//
//    // Read
//    @Test
//    fun `receive incoming lines`() = runTest {
//        val sut = createSUT(backgroundScope)
//        val received = sut.incomingLines.collectInto(backgroundScope)
//        sut.connect()
//        runCurrent()
//
//        val lines = List(3) { nextString("line") }
//        lines.forEach { lineChannel.trySend(it) }
//        runCurrent()
//
//        assertEquals(lines, received)
//    }
//
//    // Disconnect
//    @Test
//    fun `disconnect from the serial port`() = runTest {
//        val sut = createSUT(backgroundScope)
//        sut.connect()
//        runCurrent()
//
//        sut.disconnect()
//        runCurrent()
//
//        verify { serialPort.close() }
//    }
//
//    // Connection state
//    @Test
//    fun `get the connection state`() = runTest {
//        val sut = createSUT(backgroundScope)
//        assertEquals(false, sut.isConnected.value)
//
//        sut.connect()
//        runCurrent()
//        assertEquals(true, sut.isConnected.value)
//
//        sut.disconnect()
//        runCurrent()
//        assertEquals(false, sut.isConnected.value)
//    }
//
//    // Port path
//    @Test
//    fun `get the port path`() = runTest {
//        val sut = createSUT(backgroundScope)
//
//        assertEquals(portPath, sut.portPath)
//    }
//
//    // Error Handling
//    @Test
//    fun `when opening the port fails, then disconnect`() = runTest {
//        val sut = createSUT(backgroundScope)
//        coEvery { serialPort.open(any(), any()) } throws nextException()
//
//        assertThrows<Exception> { sut.connect() }
//        runCurrent()
//
//        verify { serialPort.close() }
//        assertEquals(false, sut.isConnected.value)
//    }
//
//    @Test
//    fun `when reading fails, then disconnect`() = runTest {
//        val sut = createSUT(backgroundScope)
//        coEvery { serialPort.readNextLine() } throws nextException()
//
//        sut.connect()
//        runCurrent()
//
//        verify { serialPort.close() }
//        assertEquals(false, sut.isConnected.value)
//    }
//
//}