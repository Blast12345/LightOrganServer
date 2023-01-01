package sound.input

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sound.input.sample.AudioFrame
import sound.input.sample.AudioFrameFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class InputTests {

    @MockK
    private lateinit var dataLine: TargetDataLine

    @MockK
    private lateinit var sampleFactory: AudioFrameFactory

    private val bufferSize = 4096
    private val bytesAvailable = 1024
    private val bytesRead = 1024
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val audioFrame = AudioFrame(byteArrayOf(1), format)

    @Before
    fun setup() {
        dataLine = mockk()
        every { dataLine.bufferSize } returns bufferSize
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.read(any(), 0, bytesAvailable) } returns bytesRead
        every { dataLine.format } returns format

        sampleFactory = mockk()
        every { sampleFactory.audioFrameFor(any(), dataLine) } returns audioFrame
    }

    private fun createSUT(): Input {
        return Input(dataLine, sampleFactory)
    }

    @Test
    fun `return the audio frame when the data line has new data`() {
        val sut = createSUT()

        var nextAudioFrame: AudioFrame? = null
        sut.listenForAudioSamples {
            nextAudioFrame = it

            // We must stop listening or the while loop will prevent us from continuing
            sut.stopListening()
        }

        assertEquals(audioFrame, nextAudioFrame)
    }

    @Test
    fun `the line is opened and started before looking for data availability`() {
        val sut = createSUT()

        sut.listenForAudioSamples {
            sut.stopListening()
        }

        verifyOrder {
            dataLine.open()
            dataLine.start()
            dataLine.available()
        }
    }


}