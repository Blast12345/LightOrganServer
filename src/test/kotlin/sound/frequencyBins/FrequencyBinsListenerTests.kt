package sound.frequencyBins

import sound.fft.FakeFftService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FrequencyBinsListenerTests {

    private lateinit var fftService: FakeFftService
    private lateinit var frequencyBinsFactory: FakeFrequencyBinsFactory

    @Before
    fun setup() {
        fftService = FakeFftService()
        frequencyBinsFactory = FakeFrequencyBinsFactory()
    }

    private fun createSUT(): FrequencyBinsListener {
        return FrequencyBinsListener(fftService, frequencyBinsFactory)
    }

    @Test
    fun `frequency bins are generated when there is new FFT data`() {
        val sut = createSUT()

        var nextFrequencyBins: List<FrequencyBin>? = null
        sut.listenForFrequencyBins {
            nextFrequencyBins = it
        }

        val sampleRate = 44100
        val sampleSize = 512
        val amplitudes = doubleArrayOf(0.0, 1.0, 2.0)
        fftService.nextFftData?.invoke(sampleRate, sampleSize, amplitudes)

        assertEquals(sampleRate, frequencyBinsFactory.sampleRate)
        assertEquals(sampleSize, frequencyBinsFactory.sampleSize)
        assertEquals(amplitudes, frequencyBinsFactory.amplitudes)
        assertEquals(frequencyBinsFactory.frequencyBins, nextFrequencyBins)
    }

}