package sound.fft

import org.junit.Before

class FftServiceTests {

    //    private lateinit var lineInputListener: FakeLineInputListener
    private lateinit var amplitudeFactory: FakeAmplitudeFactory
    private val sampleRate = 1
    private val sampleSize = 2
    private val signal = DoubleArray(5)

    @Before
    fun setup() {
//        lineInputListener = FakeLineInputListener()
        amplitudeFactory = FakeAmplitudeFactory()
    }

    private fun createSUT(): FftService {
        return FftService(
//            lineInputListener,
            amplitudeFactory
        )
    }

//    @Test
//    fun `the audioFrame rate is returned when a signal is received from a line input`() {
//        val sut = createSUT()
//
//        var actual: Int? = null
//        sut.listenForFftData { sampleRate, _, _ ->
//            actual = sampleRate
//        }
//
//        lineInputListener.nextAudioSample?.invoke(sampleRate, sampleSize, signal)
//        assertEquals(sampleRate, actual)
//    }
//
//    @Test
//    fun `the audioFrame size is returned when a signal is received from a line input`() {
//        val sut = createSUT()
//
//        var actual: Int? = null
//        sut.listenForFftData { _, sampleSize, _ ->
//            actual = sampleSize
//        }
//
//        lineInputListener.nextAudioSample?.invoke(sampleRate, sampleSize, signal)
//        assertEquals(sampleSize, actual)
//    }
//
//    @Test
//    fun `amplitudes are returned when a signal is received from a line input`() {
//        val sut = createSUT()
//
//        var actual: DoubleArray? = null
//        sut.listenForFftData { _, _, amplitudes ->
//            actual = amplitudes
//        }
//
//        lineInputListener.nextAudioSample?.invoke(sampleRate, sampleSize, signal)
//        assertEquals(amplitudeFactory.signal, signal)
//        assertEquals(amplitudeFactory.amplitudes, actual)
//    }

}