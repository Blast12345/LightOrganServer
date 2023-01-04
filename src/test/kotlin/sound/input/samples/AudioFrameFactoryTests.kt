package sound.input.samples

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class AudioFrameFactoryTests {

    private var sampleNormalizer: SampleNormalizer = mockk()
    private var format: AudioFormat = mockk()
    private val rawSamples = byteArrayOf(1, 2, 3)

    @BeforeEach
    fun setup() {
        every { sampleNormalizer.normalize(any(), any()) } returns nextDoubleArray()
        every { format.sampleRate } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioFrameFactory {
        return AudioFrameFactory(sampleNormalizer)
    }

    @Test
    fun `normalize the audio so that different formats can be interpreted the same`() {
        val sut = createSUT()
        val normalizedSamples = doubleArrayOf(1.1, 2.2, 3.3)
        every { sampleNormalizer.normalize(rawSamples, any()) } returns normalizedSamples

        val actual = sut.create(rawSamples, format)

        assertEquals(normalizedSamples, actual.samples)
    }

    @Test
    fun `the sample rate is provided from the audio format`() {
        val sut = createSUT()
        val sampleRate = Random.nextFloat()
        every { format.sampleRate } returns sampleRate

        val actual = sut.create(rawSamples, format)

        assertEquals(sampleRate, actual.sampleRate)
    }

}
