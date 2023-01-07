package sound.frequencyBins

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.samples.AudioSignal
import sound.signalProcessing.AmplitudeFactoryInterface
import toolkit.monkeyTest.nextDoubleArray

class FrequencyBinsFactoryTests {

    private var amplitudeFactory: AmplitudeFactoryInterface = mockk()
    private var sampleSizeCalculator: SampleSizeCalculatorInterface = mockk()

    private val sampleRate = 48000F
    private val sampleRateToSizeRatio = 4F
    private val sampleSize = (sampleRate / sampleRateToSizeRatio).toInt()
    private val samples = nextDoubleArray(length = sampleSize)
    private val audioSignal = AudioSignal(samples, sampleRate)
    private val amplitudes = nextDoubleArray(length = sampleSize / 2)

    @BeforeEach
    fun setup() {
        every { amplitudeFactory.create(any()) } returns amplitudes
        every { sampleSizeCalculator.calculate(any(), any()) } returns sampleSize
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinsFactory {
        return FrequencyBinsFactory(
            amplitudeFactory = amplitudeFactory,
            sampleSizeCalculator = sampleSizeCalculator
        )
    }

    @Test
    fun `each frequency bin has an amplitude`() {
        val sut = createSUT()
        val actual = sut.create(audioSignal, 0F)
        val actualAmplitudes = actual.map { it.amplitude }
        val expectedAmplitudes = amplitudes.map { it.toFloat() }
        assertEquals(expectedAmplitudes, actualAmplitudes)
    }

    @Test
    fun `the amplitudes are calculated from the fewest samples necessary for a minimum frequency`() {
        val sut = createSUT()
        val lowestSupportedFrequency = 20F
        val sampleSize = 4096
        every { sampleSizeCalculator.calculate(lowestSupportedFrequency, sampleRate) } returns sampleSize

        sut.create(audioSignal, lowestSupportedFrequency)

        val expectedSamples = samples.takeLast(sampleSize).toDoubleArray()
        verify { amplitudeFactory.create(expectedSamples) }
    }

    @Test
    fun `each frequency bin has a frequency`() {
        val sut = createSUT()
        val actual = sut.create(audioSignal, 0F)
        val actualFrequencies = actual.map { it.frequency }
        assertEquals(expectedFrequencies(), actualFrequencies)
    }

    private fun expectedFrequencies(): List<Float> {
        val numberOfBins = amplitudes.count()
        var frequencies: MutableList<Float> = mutableListOf()

        for (i in 0 until numberOfBins) {
            val frequency = (i * sampleRateToSizeRatio)
            frequencies.add(frequency)
        }

        return frequencies
    }
}