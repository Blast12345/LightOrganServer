package sound.fft

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AmplitudeFactoryTests {

    private lateinit var hannWindowFilter: FakeHannWindowFilter
    private lateinit var fftAlgorithm: FakeFftAlgorithm
    private val signal = DoubleArray(5)

    @BeforeEach
    fun setup() {
        hannWindowFilter = FakeHannWindowFilter()
        fftAlgorithm = FakeFftAlgorithm()
    }

    private fun createSUT(): AmplitudeFactory {
        return AmplitudeFactory(hannWindowFilter, fftAlgorithm)
    }

    @Test
    fun `the signal goes through a hann window filter to reduce spectral leakage`() {
        val sut = createSUT()
        sut.createFrom(signal)
        assertEquals(hannWindowFilter.signal, signal)
    }

    @Test
    fun `the filtered signal is processed by FFT`() {
        // TODO: Is processed by FFT? Has FFT applied to it? Goes through an FFT algorithm?
        val sut = createSUT()
        val amplitudes = sut.createFrom(signal)
        assertEquals(fftAlgorithm.signal, hannWindowFilter.filteredSignal)
        assertEquals(fftAlgorithm.amplitudes, amplitudes)
    }

}