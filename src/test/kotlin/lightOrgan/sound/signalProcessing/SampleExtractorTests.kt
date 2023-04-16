package lightOrgan.sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame

class SampleExtractorTests {

    private val samples = doubleArrayOf(1.0, 2.0, 3.0)
    private val audioFrame = nextAudioFrame(samples)

    private fun createSUT(): SampleExtractor {
        return SampleExtractor()
    }

    @Test
    fun `take the latest N samples that support the lowest frequency`() {
        val sut = createSUT()
        val extractedSamples = sut.extract(audioFrame, 2)
        val expected = doubleArrayOf(2.0, 3.0)
        assertArrayEquals(expected, extractedSamples, 0.001)
    }

}