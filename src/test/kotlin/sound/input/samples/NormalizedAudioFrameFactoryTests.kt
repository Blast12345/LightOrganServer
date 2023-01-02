package sound.input.samples

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.sound.sampled.AudioFormat

class NormalizedAudioFrameFactoryTests {

    @MockK
    private lateinit var sampleNormalizer: SampleNormalizer

    @MockK
    private lateinit var format: AudioFormat

    private val rawSamples = byteArrayOf(1, 2, 3)
    private val normalizedSamples = doubleArrayOf(1.1, 2.2, 3.3)

    @BeforeEach
    fun setup() {
        sampleNormalizer = mockk()
        format = mockk()

        every { sampleNormalizer.normalize(rawSamples, format) } returns normalizedSamples
    }

    private fun createSUT(): NormalizedAudioFrameFactory {
        return NormalizedAudioFrameFactory(sampleNormalizer)
    }

    @Test
    fun `create a normalized audio frame`() {
        val sut = createSUT()
        val actual = sut.createFor(rawSamples, format)
        assertTrue(actual is NormalizedAudioFrame)
    }

    @Test
    fun `the samples are a normalized version of the raw samples`() {
        val sut = createSUT()
        val actual = sut.createFor(rawSamples, format)
        assertEquals(normalizedSamples, actual.samples)
    }

}
