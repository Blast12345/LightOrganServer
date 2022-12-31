package sound.frequencyBins

import org.junit.Assert.assertEquals
import org.junit.Test

class FrequencyBinsFactoryTests {

    private fun createSUT(): FrequencyBinsFactory {
        return FrequencyBinsFactory()
    }

    @Test
    fun `frequency bins are created`() {
        val sut = createSUT()
        val sampleRate = 44100
        val sampleSize = 512
        val amplitude0 = 0.0
        val amplitude1 = 1.0
        val amplitude2 = 2.0
        val amplitudes = doubleArrayOf(amplitude0, amplitude1, amplitude2)

        val actual = sut.createFrom(sampleRate, sampleSize, amplitudes)

        // TODO: Is it true that the first bin is always 0?
        val expected = listOf(
            FrequencyBin(0.0 * sampleRate / sampleSize, amplitude0),
            FrequencyBin(1.0 * sampleRate / sampleSize, amplitude1),
            FrequencyBin(2.0 * sampleRate / sampleSize, amplitude2)
        )

        assertEquals(expected, actual)
    }

}