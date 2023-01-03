package sound.input.samples

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import javax.sound.sampled.AudioFormat
import kotlin.random.Random

class NormalizedAudioFrameFactoryTests {

    private lateinit var sampleNormalizer: SampleNormalizer
    private val rawSamples = byteArrayOf(1, 2, 3)
    private lateinit var format: AudioFormat

    @BeforeEach
    fun setup() {
        sampleNormalizer = mockk()
        every { sampleNormalizer.normalize(any(), any()) } returns nextDoubleArray()

        format = mockk()
        every { format.sampleRate } returns Random.nextFloat()
    }

    private fun createSUT(): NormalizedAudioFrameFactory {
        return NormalizedAudioFrameFactory(sampleNormalizer)
    }

    @Test
    fun `the samples are a normalized version of the raw samples`() {
        val sut = createSUT()
        val normalizedSamples = doubleArrayOf(1.1, 2.2, 3.3)
        every { sampleNormalizer.normalize(rawSamples, any()) } returns normalizedSamples

        val actual = sut.createFor(rawSamples, format)

        assertEquals(normalizedSamples, actual.samples)
    }

    @Test
    fun `the sample rate is provided from the audio format`() {
        val sut = createSUT()
        val sampleRate = Random.nextFloat()
        every { format.sampleRate } returns sampleRate

        val actual = sut.createFor(rawSamples, format)

        assertEquals(sampleRate, actual.sampleRate)
    }

}
