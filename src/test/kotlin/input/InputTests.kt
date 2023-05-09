package input

import input.audioFrame.AudioFrameFactory
import input.buffer.InputBuffer
import input.lineListener.LineListener
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class InputTests {

    private val subscriber1: InputSubscriber = mockk(relaxed = true)
    private val subscriber2: InputSubscriber = mockk(relaxed = true)
    private lateinit var subscribers: MutableSet<InputSubscriber>
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
        subscribers = mutableSetOf(subscriber1, subscriber2)
        every { lineListener.subscribers.add(any()) } returns true
        every { lineListener.audioFormat } returns audioFormat
        every { audioFrameFactory.create(any(), any()) } returns audioFrame
        every { delegate.received(any()) } returns Unit
        every { buffer.updatedWith(any()) } returns updatedBuffer
    }

    @AfterEach
    fun teardown() {
        subscribers = mutableSetOf()
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            subscribers = subscribers,
            lineListener = lineListener,
            buffer = buffer,
            audioFrameFactory = audioFrameFactory
        )
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is`() {
        val sut = createSUT()

        val actual = sut.checkIfSubscribed(subscriber1)

        assertTrue(actual)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is not`() {
        val sut = createSUT()

        val newSubscriber: InputSubscriber = mockk()
        val actual = sut.checkIfSubscribed(newSubscriber)

        assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()

        val newSubscriber: InputSubscriber = mockk()
        sut.addSubscriber(newSubscriber)

        assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

    @Test
    fun `remove a subscriber`() {
        val sut = createSUT()

        sut.removeSubscriber(subscriber1)

        assertFalse(sut.checkIfSubscribed(subscriber1))
    }

    @Test
    fun `begin listening to the target data line upon initialization`() {
        val sut = createSUT()
        verify { lineListener.subscribers.add(sut) }
    }

    @Test
    fun `update the buffer when samples are received`() {
        val sut = createSUT()
        sut.received(newSamples)
        verify { buffer.updatedWith(newSamples) }
    }

    @Test
    fun `create an audio clip from the updated buffer samples`() {
        val sut = createSUT()
        sut.received(newSamples)
        verify { audioFrameFactory.create(updatedBuffer, audioFormat) }
    }

    @Test
    fun `send the audio clip to the listeners when new samples are received`() {
        val sut = createSUT()

        sut.received(newSamples)

        verify(exactly = 1) { subscriber1.received(audioFrame) }
        verify(exactly = 1) { subscriber2.received(audioFrame) }
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        assertEquals(lineListener.audioFormat, sut.audioFormat)
    }

}