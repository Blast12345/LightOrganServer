package sound.input

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
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

    @MockK
    private lateinit var delegate: InputDelegate

    private val bufferSize = 4096
    private val bytesAvailable = 1024
    private val bytesRead = 1024
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val audioFrame = NormalizedAudioFrame(doubleArrayOf(1.1), 44100F)

    @BeforeEach
    fun setup() {
        // TODO: Perhaps these can be improved. Feels a bit heavy handed.
        dataLine = mockk()
        every { dataLine.bufferSize } returns bufferSize
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.isActive } returns true andThen false
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.read(any(), 0, bytesAvailable) } returns bytesRead
        every { dataLine.format } returns format

        sampleFactory = mockk()
        every { sampleFactory.createFor(any(), format) } returns audioFrame

        delegate = mockk()
        every { delegate.receiveAudioFrame(any()) } returns Unit
    }

    private fun createSUT(): Input {
        return Input(dataLine, sampleFactory)
    }

    @Test
    suspend fun `listen to a data line`() {
        val sut = createSUT()
        sut.listenForAudioSamples(delegate)
        verifyOrder {
            dataLine.open()
            dataLine.start()
        }
    }

    @Test
    suspend fun `return an audio frame when the data line has data available`() {
        val sut = createSUT()
        sut.listenForAudioSamples(delegate)
        verify { delegate.receiveAudioFrame(audioFrame) }
    }

}