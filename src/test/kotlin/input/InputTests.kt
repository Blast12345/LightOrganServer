package input

import input.audioFrame.AudioFrameFactory
import input.buffer.InputBuffer
import input.lineListener.LineListener
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class InputTests {

    private val lineListener: LineListener = mockk()
    private val buffer: InputBuffer = mockk()
    private val audioFrameFactory: AudioFrameFactory = mockk()
    private val subscriber1: InputSubscriber = mockk(relaxed = true)
    private val subscriber2: InputSubscriber = mockk(relaxed = true)

    private val newSamples = Random.nextBytes(4)
    private val updatedBuffer = Random.nextBytes(4)
    private val audioFormat: AudioFormat = mockk()
    private val audioFrame = nextAudioFrame()

    @BeforeEach
    fun setup() {
        every { lineListener.addSubscriber(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            lineListener = lineListener,
            buffer = buffer,
            audioFrameFactory = audioFrameFactory,
            subscribers = mutableSetOf(subscriber1, subscriber2)
        )
    }

    @Test
    fun `begin listening to the target data line upon initialization`() {
        val sut = createSUT()
        verify { lineListener.addSubscriber(sut) }
    }

    @Test
    fun `send the audio frame to the subscribers when new samples are received`() {
        val sut = createSUT()
        every { buffer.updatedWith(newSamples) } returns updatedBuffer
        every { lineListener.audioFormat } returns audioFormat
        every { audioFrameFactory.create(updatedBuffer, audioFormat) } returns audioFrame

        sut.received(newSamples)

        verify(exactly = 1) { subscriber1.received(audioFrame) }
        verify(exactly = 1) { subscriber2.received(audioFrame) }
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        every { lineListener.audioFormat } returns audioFormat
        assertEquals(lineListener.audioFormat, sut.audioFormat)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is`() {
        val sut = createSUT()

        val actual = sut.checkIfSubscribed(subscriber1)

        Assertions.assertTrue(actual)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is not`() {
        val sut = createSUT()

        val newSubscriber: InputSubscriber = mockk()
        val actual = sut.checkIfSubscribed(newSubscriber)

        Assertions.assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()

        val newSubscriber: InputSubscriber = mockk()
        sut.addSubscriber(newSubscriber)

        Assertions.assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

    @Test
    fun `remove a subscriber`() {
        val sut = createSUT()

        sut.removeSubscriber(subscriber1)

        Assertions.assertFalse(sut.checkIfSubscribed(subscriber1))
    }

}