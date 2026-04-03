package wrappers.sound

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.*
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random.Default.nextBoolean

class InputLineTests {

    private val name = nextString("name")
    private val dataLine: TargetDataLine = mockk()
    private val minimumReadSize = nextPositiveInt()
    private val bufferSize = minimumReadSize * 2 // easy math

    private val format: javax.sound.sampled.AudioFormat = mockk()
    private val exception = nextException()

    @BeforeEach
    fun setupHappyPath() {
        every { dataLine.format } returns format
        every { format.sampleRate } returns nextPositiveFloat()
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

    // Init
    @Test
    fun `the buffer size must be greater than or equal to the minimum read size`() {
        assertDoesNotThrow { InputLine(name, dataLine, minimumReadSize = 2, bufferSize = 3) }
        assertDoesNotThrow { InputLine(name, dataLine, minimumReadSize = 2, bufferSize = 2) }
        assertThrows<IllegalArgumentException> { InputLine(name, dataLine, minimumReadSize = 2, bufferSize = 1) }
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

    // Lifecycle
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
    fun `stop stops and closes the data line`() {
        val sut = createSUT()

        sut.stop()

        verifyOrder {
            dataLine.stop()
            dataLine.close()
        }
    }

    // Read - data
    @Test
    fun `when no data is available, read the minimum size`() {
        val sut = createSUT()

        every { dataLine.available() } returns 0

        val bytesToBeRead = nextByteArray(minimumReadSize)
        every { dataLine.read(any(), 0, minimumReadSize) } answers {
            bytesToBeRead.copyInto(firstArg<ByteArray>())
            bytesToBeRead.size
        }

        val result = sut.read()

        assertArrayEquals(bytesToBeRead, result.data)
    }

    @Test
    fun `when available data equals the minimum, read the minimum size`() {
        val sut = createSUT()

        every { dataLine.available() } returns minimumReadSize

        val bytesToBeRead = nextByteArray(minimumReadSize)
        every { dataLine.read(any(), 0, minimumReadSize) } answers {
            bytesToBeRead.copyInto(firstArg<ByteArray>())
            bytesToBeRead.size
        }

        val result = sut.read()

        assertArrayEquals(bytesToBeRead, result.data)
    }

    @Test
    fun `when available data exceeds the minimum, read all available`() {
        val sut = createSUT()
        val availableBytes = minimumReadSize + 1

        every { dataLine.available() } returns availableBytes

        val bytesToBeRead = nextByteArray(availableBytes)
        every { dataLine.read(any(), 0, availableBytes) } answers {
            bytesToBeRead.copyInto(firstArg<ByteArray>())
            bytesToBeRead.size
        }

        val result = sut.read()

        assertArrayEquals(bytesToBeRead, result.data)
    }

    // Read - buffer status
    @Test
    fun `when available data is less than the buffer size, the buffer was not full`() {
        val sut = createSUT()
        val availableBytes = bufferSize - 1
        val bytesToBeRead = nextByteArray(availableBytes)

        every { dataLine.available() } returns availableBytes
        every { dataLine.read(any(), 0, availableBytes) } answers {
            bytesToBeRead.copyInto(firstArg<ByteArray>())
            bytesToBeRead.size
        }

        val result = sut.read()

        assertEquals(false, result.bufferWasFull)
    }


    @Test
    fun `when available data is equal to the buffer size, the buffer was full`() {
        val sut = createSUT()
        val availableBytes = bufferSize
        val bytesToBeRead = nextByteArray(availableBytes)

        every { dataLine.available() } returns availableBytes
        every { dataLine.read(any(), 0, availableBytes) } answers {
            bytesToBeRead.copyInto(firstArg<ByteArray>())
            bytesToBeRead.size
        }

        val result = sut.read()

        assertEquals(true, result.bufferWasFull)
    }


}