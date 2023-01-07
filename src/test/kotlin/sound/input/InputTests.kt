package sound.input

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.AudioSignal
import sound.input.samples.AudioSignalFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class InputTests {

    private var dataLine: TargetDataLine = mockk()
    private var audioSignalFactory: AudioSignalFactoryInterface = mockk()
    private var delegate: InputDelegate = mockk()

    private val bufferSize = 4096
    private val bytesAvailable = 1024
    private val bytesRead = 1024
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val audioSignal = AudioSignal(doubleArrayOf(1.1), 44100F)

    @BeforeEach
    fun setup() {
        every { dataLine.bufferSize } returns bufferSize
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.read(any(), 0, bytesAvailable) } returns bytesRead
        every { dataLine.format } returns format
        every { audioSignalFactory.create(any(), format) } returns audioSignal
        every { delegate.receiveAudioSignal(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(dataLine, audioSignalFactory)
    }

    @Test
    fun `open and start a data line on initialization`() {
        createSUT()
        verifyOrder {
            dataLine.open()
            dataLine.start()
        }
    }

    @Test
    fun `return an audio signal when the data line has data available`() {
        val sut = createSUT()
        every { delegate.receiveAudioSignal(any()) } answers { sut.stopListening() }
        sut.listenForAudioSamples(delegate)
        verify { delegate.receiveAudioSignal(audioSignal) }
    }

}