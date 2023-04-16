package input.audioFrame

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormatWrapper
import toolkit.monkeyTest.nextDoubleArray
import wrappers.audioFormat.AudioFormatWrapperFactory
import javax.sound.sampled.AudioFormat

class AudioFrameFactoryTests {

    private val sampleNormalizer: SampleNormalizer = mockk()
    private val audioFormatWrapperFactory: AudioFormatWrapperFactory = mockk()

    private val rawSamples = byteArrayOf(1, 2, 3)
    private val format: AudioFormat = mockk()

    private val normalizedSamples = nextDoubleArray()
    private val wrappedFormat = nextAudioFormatWrapper()

    @BeforeEach
    fun setup() {
        every { sampleNormalizer.normalize(any(), any()) } returns normalizedSamples
        every { audioFormatWrapperFactory.create(any()) } returns wrappedFormat
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioFrameFactory {
        return AudioFrameFactory(
            sampleNormalizer = sampleNormalizer,
            audioFormatWrapperFactory = audioFormatWrapperFactory
        )
    }

    @Test
    fun `normalize the audio so that different formats can be interpreted the same`() {
        val sut = createSUT()
        val actual = sut.create(rawSamples, format)
        assertEquals(normalizedSamples, actual.samples)
        verify { sampleNormalizer.normalize(rawSamples, format) }
    }

    @Test
    fun `the audio format is wrapped to help with mocking`() {
        val sut = createSUT()
        val actual = sut.create(rawSamples, format)
        assertEquals(wrappedFormat, actual.format)
        verify { audioFormatWrapperFactory.create(format) }
    }

}
