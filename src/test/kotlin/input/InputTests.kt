package input

import input.samples.AudioSignalFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioSignal
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class InputTests {

    private val delegate: InputDelegate = mockk()
    private val targetDataLineListener: TargetDataLineListener = mockk()
    private val buffer: AudioBuffer = mockk()
    private val audioSignalFactory: AudioSignalFactory = mockk()

    private val audioClip = nextAudioSignal()
    private val newSamples = Random.nextBytes(1024)
    private val audioFormat: AudioFormat = mockk()
    private val updatedBuffer = Random.nextBytes(1024)

    @BeforeEach
    fun setup() {
        every { targetDataLineListener.listeners.add(any()) } returns true
        every { targetDataLineListener.audioFormat } returns audioFormat
        every { audioSignalFactory.create(any(), any()) } returns audioClip
        every { delegate.received(any()) } returns Unit
        every { buffer.updatedWith(any()) } returns updatedBuffer
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            targetDataLineListener = targetDataLineListener,
            buffer = buffer,
            audioSignalFactory = audioSignalFactory
        )
    }

    @Test
    fun `begins listening to the target data line upon initialization`() {
        val sut = createSUT()
        verify { targetDataLineListener.listeners.add(sut) }
    }

    @Test
    fun `the buffer is updated when samples are received`() {
        val sut = createSUT()
        sut.received(newSamples)
        verify { buffer.updatedWith(newSamples) }
    }

    @Test
    fun `the updated samples from the buffer are used to create an audio clip`() {
        val sut = createSUT()
        sut.received(newSamples)
        verify { audioSignalFactory.create(updatedBuffer, audioFormat) }
    }

    @Test
    fun `the audio clip is sent to the listeners`() {
        val sut = createSUT()
        val listener: InputDelegate = mockk(relaxed = true)
        sut.listeners.add(listener)

        sut.received(newSamples)

        verify(exactly = 1) { listener.received(audioClip) }
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        assertEquals(targetDataLineListener.audioFormat, sut.audioFormat)
    }

}