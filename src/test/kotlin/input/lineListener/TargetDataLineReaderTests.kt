package input.lineListener

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

class TargetDataLineReaderTests {

    private val dataLine: TargetDataLine = mockk()
    private val bytesAvailable = Random.nextInt(1024)
    private val newSamples = Random.nextBytes(bytesAvailable)

    @BeforeEach
    fun setupHappyPath() {
        every { dataLine.available() } returns bytesAvailable

        // Target Data Lines return data through the first parameter, so I must do this nasty mocking.
        every { dataLine.read(any(), any(), any()) } answers {
            val outputParameter = invocation.args[0] as ByteArray
            newSamples.copyInto(outputParameter)
            bytesAvailable
        }
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): TargetDataLineReader {
        return TargetDataLineReader()
    }

    @Test
    fun `get available data from the data line`() {
        val sut = createSUT()

        val actual = sut.getAvailableData(dataLine)

        verify { dataLine.read(any(), 0, bytesAvailable) }
        assertArrayEquals(newSamples, actual)
    }

}
