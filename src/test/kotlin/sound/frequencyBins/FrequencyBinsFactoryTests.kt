package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sound.input.samples.NormalizedAudioFrame

class FrequencyBinsFactoryTests {

    private val samples = doubleArrayOf(1.1)
    private val sampleRate = 44100F
    private val audioFrame = NormalizedAudioFrame(samples, sampleRate)
    private val bin1 = FrequencyBin(100.0, 1.0)
    private val bin2 = FrequencyBin(200.0, 2.0)

    private fun createSUT(): FrequencyBinsFactory {
        return FrequencyBinsFactory()
    }

    @Test
    fun `frequency bins are created from an audio frame`() {
        val sut = createSUT()
        val actual = sut.createFrom(audioFrame, 0F)
        assertTrue(actual is FrequencyBins)
//        val expected = listOf(bin1, bin2)
//        assertEquals(expected, actual)
//
//        // TODO: Is it true that the first bin is always 0?
//        val expected = listOf(
//            FrequencyBin(0.0 * sampleRate / sampleSize, amplitude0),
//            FrequencyBin(1.0 * sampleRate / sampleSize, amplitude1),
//            FrequencyBin(2.0 * sampleRate / sampleSize, amplitude2)
//        )
//
//        assertEquals(expected, actual)
    }

//    @Test
//    fun `the frequency bins

}