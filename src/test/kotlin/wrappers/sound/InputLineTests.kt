package wrappers.sound

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextByteArray
import toolkit.monkeyTest.nextException
import toolkit.monkeyTest.nextPositiveInt
import toolkit.monkeyTest.nextString
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random.Default.nextBoolean

@OptIn(ExperimentalCoroutinesApi::class)
class InputLineTests {

    private val name = nextString("name")
    private val dataLine: TargetDataLine = mockk()
    private val minimumReadSize = nextPositiveInt()
    private val bufferSize = nextPositiveInt()

    private val format: javax.sound.sampled.AudioFormat = mockk()
    private val exception = nextException()

    @BeforeEach
    fun setupHappyPath() {
        every { dataLine.format } returns format
        every { format.sampleRate } returns nextPositiveInt().toFloat()
        every { format.sampleSizeInBits } returns nextPositiveInt()
        every { format.channels } returns nextPositiveInt()
        every { format.isBigEndian } returns nextBoolean()

        every { dataLine.open(any(), any()) } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.read(any(), any(), any()) } returns nextPositiveInt()
        every { dataLine.stop() } returns Unit
        every { dataLine.close() } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): InputLine {
        return InputLine(
            name = name,
            dataLine = dataLine,
            minimumReadSize = minimumReadSize,
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

        assertEquals(format.sampleRate, sut.sampleRate)
    }

    @Test
    fun `get the bit depth`() {
        val sut = createSUT()

        assertEquals(format.sampleSizeInBits, sut.bitDepth)
    }

    @Test
    fun `get the number of channels`() {
        val sut = createSUT()

        assertEquals(format.channels, sut.channels)
    }

    @Test
    fun `given the format is big endian, then the byte order is big endian`() {
        every { format.isBigEndian } returns true
        val sut = createSUT()

        assertEquals(ByteOrder.BIG_ENDIAN, sut.byteOrder)
    }

    @Test
    fun `given the format not big endian, then the byte order is little endian`() {
        every { format.isBigEndian } returns false
        val sut = createSUT()

        assertEquals(ByteOrder.LITTLE_ENDIAN, sut.byteOrder)
    }

    // Start
    @Test
    fun `open and start the data line`() {
        val sut = createSUT()

        sut.start()

        verifyOrder {
            dataLine.open(format, bufferSize)
            dataLine.start()
        }
    }

    @Test
    fun `when open fails, then stop and rethrow`() {
        val sut = createSUT()
        every { dataLine.open(any(), any()) } throws exception

        val actual = assertThrows<Exception> { sut.start() }

        verify { dataLine.stop() }
        verify { dataLine.close() }
        assertEquals(exception, actual)
    }

    @Test
    fun `when start fails, then stop and rethrow`() {
        val sut = createSUT()
        every { dataLine.start() } throws exception

        val actual = assertThrows<Exception> { sut.start() }

        verify { dataLine.stop() }
        verify { dataLine.close() }
        assertEquals(exception, actual)
    }

    @Test
    fun `read data from the data line`() = runTest {
        val sut = createSUT()

        // Suggests that data is not immediately available
        every { dataLine.available() } returns 0

        // The next read will return the minimum read size
        val readBytes = nextByteArray(minimumReadSize)
        every { dataLine.read(any(), 0, minimumReadSize) } answers {
            readBytes.copyInto(firstArg<ByteArray>())
            readBytes.size
        }

        val result = sut.read()

        assertArrayEquals(readBytes, result)
    }

    @Test
    fun `when more data is available than minimum, read all available`() = runTest {
        val sut = createSUT()

        // Suggests that excess data is waiting to be read
        val availableBytes = minimumReadSize * 2
        every { dataLine.available() } returns availableBytes

        // The next read will return up to
        val readBytes = nextByteArray(availableBytes)
        every { dataLine.read(any(), 0, availableBytes) } answers {
            readBytes.copyInto(firstArg<ByteArray>())
            readBytes.size
        }

        val result = sut.read()

        assertArrayEquals(readBytes, result)
    }

    // Stop
    @Test
    fun `stop stops and closes the data line`() {
        val sut = createSUT()

        sut.stop()

        verifyOrder {
            dataLine.stop()
            dataLine.close()
        }
    }

}