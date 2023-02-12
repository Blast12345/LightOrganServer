package sound.input

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.AudioSignalFactory
import toolkit.monkeyTest.nextAudioSignal
import javax.sound.sampled.AudioFormat
import kotlin.random.Random


class InputTests {

    private val targetDataLineListener: TargetDataLineListener = mockk()
    private val buffer: AudioBuffer = mockk()
    private val audioClipFactory: AudioSignalFactory = mockk()
    private val delegate: InputDelegate = mockk()

    private val newSamples = Random.nextBytes(1024)
    private val format: AudioFormat = mockk()
    private val updatedBuffer = Random.nextBytes(1024)
    private val audioClip = nextAudioSignal()

    @BeforeEach
    fun setup() {
        every { buffer.updatedWith(any()) } returns updatedBuffer
        every { audioClipFactory.create(any(), any()) } returns audioClip
        every { delegate.received(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Input {
        return Input(
            targetDataLineListener = targetDataLineListener,
            buffer = buffer,
            audioClipFactory = audioClipFactory,
            delegate = delegate
        )
    }

    @Test
    fun `update the buffer when new samples are received`() {
        val sut = createSUT()
        sut.received(newSamples, format)
        verify { buffer.updatedWith(newSamples) }
    }

    @Test
    fun `the updated buffer is used to create an audio clip`() {
        val sut = createSUT()
        sut.received(newSamples, format)
        verify { audioClipFactory.create(updatedBuffer, format) }
    }

    @Test
    fun `the audio clip is give to the delegate`() {
        val sut = createSUT()
        sut.received(newSamples, format)
        verify { delegate.received(audioClip) }
    }

}