package wrappers.sound

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    private val readSize = 2048

    private val format: javax.sound.sampled.AudioFormat = mockk()
    private val exception = nextException()

    @BeforeEach
    fun setupHappyPath() {
        every { dataLine.format } returns format
        every { format.sampleRate } returns nextPositiveInt().toFloat()
        every { format.sampleSizeInBits } returns nextPositiveInt()
        every { format.channels } returns nextPositiveInt()
        every { format.isBigEndian } returns nextBoolean()

        every { dataLine.open(format, readSize) } returns Unit
        every { dataLine.start() } returns Unit
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
            readSize = readSize
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

        assertEquals(format.sampleRate.toInt(), sut.sampleRate)
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
            dataLine.open(format, readSize)
            dataLine.start()
        }
    }

    @Test
    fun `when open fails, then stop and rethrow`() {
        val sut = createSUT()
        every { dataLine.open(format, readSize) } throws exception

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