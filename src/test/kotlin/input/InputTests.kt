package input

import input.audioFrame.AudioFrameFactory
import input.buffer.InputBuffer
import input.lineListener.LineListener
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class InputTests {

    private val delegate: InputSubscriber = mockk()
    private val lineListener: LineListener = mockk()
    private val buffer: InputBuffer = mockk()
    private val audioFrameFactory: AudioFrameFactory = mockk()

    private val audioFrame = nextAudioFrame()
    private val newSamples = Random.nextBytes(1024)
    private val audioFormat: AudioFormat = mockk()
    private val updatedBuffer = Random.nextBytes(1024)

    @BeforeEach
    fun setup() {
        every { lineListener.subscribers.add(any()) } returns true
        every { lineListener.audioFormat } returns audioFormat
        every { audioFrameFactory.create(any(), any()) } returns audioFrame
        every { delegate.received(any()) } returns Unit
        every { buffer.updatedWith(any()) } returns updatedBuffer
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            lineListener = lineListener,
            buffer = buffer,
            audioFrameFactory = audioFrameFactory
        )
    }

    @Test
    fun `begins listening to the target data line upon initialization`() {
        val sut = createSUT()
        verify { lineListener.subscribers.add(sut) }
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
        verify { audioFrameFactory.create(updatedBuffer, audioFormat) }
    }

    @Test
    fun `the audio clip is sent to the listeners`() {
        val sut = createSUT()
        val listener: InputSubscriber = mockk(relaxed = true)
        sut.subscribers.add(listener)

        sut.received(newSamples)

        verify(exactly = 1) { listener.received(audioFrame) }
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        assertEquals(lineListener.audioFormat, sut.audioFormat)
    }

}