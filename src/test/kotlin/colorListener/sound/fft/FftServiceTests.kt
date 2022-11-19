package colorListener.sound.fft

import colorListener.sound.FftService
import colorListener.sound.input.FakeLineInputListener
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FftServiceTests {

    private lateinit var lineInputListener: FakeLineInputListener
    private lateinit var amplitudeFactory: FakeAmplitudeFactory
    private val sampleRate = 1
    private val sampleSize = 2
    private val signal = DoubleArray(5)

    @Before
    fun setup() {
        lineInputListener = FakeLineInputListener()
        amplitudeFactory = FakeAmplitudeFactory()
    }

    private fun createSUT(): FftService {
        return FftService(
            lineInputListener,
            amplitudeFactory
        )
    }

    @Test
    fun `the sample rate is returned when a signal is received from a line input`() {
        val sut = createSUT()

        var actual: Int? = null
        sut.listenForFftData { sampleRate, _, _ ->
            actual = sampleRate
        }

        lineInputListener.newAudioSample?.invoke(sampleRate, sampleSize, signal)
        assertEquals(sampleRate, actual)
    }

    @Test
    fun `the sample size is returned when a signal is received from a line input`() {
        val sut = createSUT()

        var actual: Int? = null
        sut.listenForFftData { _, sampleSize, _ ->
            actual = sampleSize
        }

        lineInputListener.newAudioSample?.invoke(sampleRate, sampleSize, signal)
        assertEquals(sampleSize, actual)
    }

    @Test
    fun `amplitudes are returned when a signal is received from a line input`() {
        val sut = createSUT()

        var actual: DoubleArray? = null
        sut.listenForFftData { _, _, amplitudes ->
            actual = amplitudes
        }

        lineInputListener.newAudioSample?.invoke(sampleRate, sampleSize, signal)
        assertEquals(amplitudeFactory.signal, signal)
        assertEquals(amplitudeFactory.amplitudes, actual)
    }

}