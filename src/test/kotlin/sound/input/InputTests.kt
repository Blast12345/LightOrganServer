package sound.input

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.NormalizedAudioFrame
import sound.input.samples.NormalizedAudioFrameFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class InputTests {

    @MockK
    private lateinit var dataLine: TargetDataLine

    @MockK
    private lateinit var sampleFactory: NormalizedAudioFrameFactory

    private val bufferSize = 4096
    private val bytesAvailable = 1024
    private val bytesRead = 1024
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val audioFrame = NormalizedAudioFrame(doubleArrayOf(1.1))

    @BeforeEach
    fun setup() {
        dataLine = mockk()
        every { dataLine.bufferSize } returns bufferSize
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.read(any(), 0, bytesAvailable) } returns bytesRead
        every { dataLine.format } returns format

        sampleFactory = mockk()
        every { sampleFactory.createFor(any(), format) } returns audioFrame
    }

    private fun createSUT(): Input {
        return Input(dataLine, sampleFactory)
    }

    @Test
    fun `the line is opened and started when the input is initialized`() {
        createSUT()
        verifyOrder {
            dataLine.open()
            dataLine.start()
        }
    }

    @Test
    fun `return the audio frame when the data line has data available`() {
        val sut = createSUT()

        var nextNormalizedAudioFrame: NormalizedAudioFrame? = null
        sut.listenForAudioSamples {
            nextNormalizedAudioFrame = it

            // We must stop listening or the while loop will prevent us from continuing
            sut.stopListening()
        }

        assertEquals(audioFrame, nextNormalizedAudioFrame)
    }


}