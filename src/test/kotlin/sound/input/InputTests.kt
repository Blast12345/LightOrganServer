package sound.input

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.AudioSignalFactory
import toolkit.monkeyTest.nextAudioSignal
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

class InputTests {

    private var dataLine: TargetDataLine = mockk()
    private var config: Config = mockk()
    private val delegate: InputDelegate = mockk()
    private val targetDataLineListener: TargetDataLineListener = mockk()
    private val buffer: AudioBuffer = mockk()
    private val audioClipFactory: AudioSignalFactory = mockk()

    private val audioClip = nextAudioSignal()
    private val newSamples = Random.nextBytes(1024)
    private val format: AudioFormat = mockk()
    private val updatedBuffer = Random.nextBytes(1024)

    @BeforeEach
    fun setup() {
        every { targetDataLineListener.setDelegate(any()) } returns Unit
        every { audioClipFactory.create(any(), any()) } returns audioClip
        every { delegate.received(any()) } returns Unit
        every { buffer.updatedWith(any()) } returns updatedBuffer
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            dataLine = dataLine,
            config = config,
            delegate = delegate,
            targetDataLineListener = targetDataLineListener,
            buffer = buffer,
            audioClipFactory = audioClipFactory
        )
    }

    @Test
    fun `begin listening to the target data line upon initialization`() {
        val sut = createSUT()
        verify { targetDataLineListener.setDelegate(sut) }
    }

    @Test
    fun `the delegate receives a new audio clip when samples are received`() {
        val sut = createSUT()
        sut.received(newSamples, format)
        verify { delegate.received(audioClip) }
    }

    @Test
    fun `the audio clip is created from an audio buffer`() {
        val sut = createSUT()
        sut.received(newSamples, format)
        verify { buffer.updatedWith(newSamples) }
        verify { audioClipFactory.create(updatedBuffer, format) }
    }

    @Test
    fun `get the delegate`() {
        val sut = createSUT()
        val actual = sut.getDelegate()
        assertEquals(delegate, actual)
    }

    @Test
    fun `set the delegate`() {
        val sut = createSUT()
        val newDelegate: InputDelegate = mockk()
        sut.setDelegate(newDelegate)
        assertEquals(sut.getDelegate(), newDelegate)
    }

}