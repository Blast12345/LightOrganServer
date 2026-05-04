package wrappers.sound

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextByteArray
import toolkit.monkeyTest.nextException
import toolkit.monkeyTest.nextInt
import toolkit.monkeyTest.nextString
import java.nio.ByteOrder
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class InputLineTests {

    private val name = nextString("name")
    private lateinit var fakeDataLine: FakeTargetDataLine
    private val bufferSize = nextInt(min = 16)

    private val exception = nextException()

    @BeforeEach
    fun setupHappyPath() {
        fakeDataLine = FakeTargetDataLine()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): InputLine {
        return InputLine(
            name = name,
            dataLine = fakeDataLine.dataLine,
            bufferSize = bufferSize
        )
    }

    // Details
    @Test
    fun `get the name`() {
        val sut = createSUT()

        assertEquals(name, sut.name)
    }

    @Test
    fun `get the sample rate`() {
        val sut = createSUT()

        assertEquals(fakeDataLine.sampleRate, sut.sampleRate)
    }

    @Test
    fun `get the bit depth`() {
        val sut = createSUT()

        assertEquals(fakeDataLine.sampleSizeInBits, sut.bitDepth)
    }

    @Test
    fun `get the number of channels`() {
        val sut = createSUT()

        assertEquals(fakeDataLine.channels, sut.channels)
    }

    @Test
    fun `given the format is big endian, then the byte order is big endian`() {
        fakeDataLine.isBigEndian = true
        val sut = createSUT()

        assertEquals(ByteOrder.BIG_ENDIAN, sut.byteOrder)
    }

    @Test
    fun `given the format not big endian, then the byte order is little endian`() {
        fakeDataLine.isBigEndian = false
        val sut = createSUT()

        assertEquals(ByteOrder.LITTLE_ENDIAN, sut.byteOrder)
    }

    // Lifecycle
    @Test
    fun `start the input`() {
        val sut = createSUT()

        sut.start()

        verifyOrder {
            fakeDataLine.dataLine.open(fakeDataLine.format, bufferSize)
            fakeDataLine.dataLine.start()
        }
    }

    @Test
    fun `given the data line fails to open when starting the input, then throw`() {
        val sut = createSUT()
        every { fakeDataLine.dataLine.open(any(), any()) } throws exception

        assertThrows<Exception> { sut.start() }
    }

    @Test
    fun `given the data line fails to start when starting the input, then close the line and throw`() {
        val sut = createSUT()
        every { fakeDataLine.dataLine.start() } throws exception

        assertThrows<Exception> { sut.start() }

        verify { fakeDataLine.dataLine.close() }
    }

    @Test
    fun `stop the input`() {
        val sut = createSUT()

        sut.stop()

        verifyOrder {
            fakeDataLine.dataLine.stop()
            fakeDataLine.dataLine.close()
        }
    }

    // Read - data
    @Test
    fun `given data is waiting to be read, read returns the data`() = runTest {
        val sut = createSUT()
        val data = nextByteArray(bufferSize)
        fakeDataLine.queue(data)

        val result = sut.read()

        assertArrayEquals(data, result.data)
    }

    @Test
    fun `given no data is waiting to be read, read waits until it can return the data`() = runTest {
        val sut = createSUT()
        val data = nextByteArray(10)

        val result = async(Dispatchers.Default) { sut.read() }

        delay(50.milliseconds)
        assertFalse(result.isCompleted)

        fakeDataLine.queue(data)

        val readResult = withTimeout(1.seconds) { result.await() }
        assertArrayEquals(data, readResult.data)
    }

    // Read - buffer state
    @Test
    fun `buffer was not full when data is less than buffer size`() = runTest {
        val sut = createSUT()
        val partialData = nextByteArray(bufferSize - 1)
        fakeDataLine.queue(partialData)

        val result = sut.read()

        assertFalse(result.bufferWasFull)
    }

    @Test
    fun `buffer was full when data equals buffer size`() = runTest {
        val sut = createSUT()
        val fullData = nextByteArray(bufferSize)
        fakeDataLine.queue(fullData)

        val result = sut.read()

        assertTrue(result.bufferWasFull)
    }

}