package sound.frequencyBins

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.fft.AmplitudeFactoryInterface
import sound.input.samples.NormalizedAudioFrame
import toolkit.monkeyTest.nextDoubleArray

class FrequencyBinsFactoryTests {

    private lateinit var amplitudeFactory: AmplitudeFactoryInterface
    private lateinit var sampleSizeFactory: SampleSizeFactoryInterface

    private val sampleRate = 48000F
    private val sampleRateToSizeRatio = 4
    private val sampleSize = (sampleRate / sampleRateToSizeRatio).toInt()
    private val samples = nextDoubleArray(length = sampleSize)
    private val audioFrame = NormalizedAudioFrame(samples, sampleRate)
    private val amplitudes = nextDoubleArray(length = sampleSize / 2)

    @BeforeEach
    fun setup() {
        amplitudeFactory = mockk()
        every { amplitudeFactory.createFrom(any()) } returns amplitudes

        sampleSizeFactory = mockk()
        every { sampleSizeFactory.createFor(any(), any()) } returns sampleSize
    }

    private fun createSUT(): FrequencyBinsFactory {
        return FrequencyBinsFactory(
            amplitudeFactory = amplitudeFactory,
            sampleSizeFactory = sampleSizeFactory
        )
    }

    @Test
    fun `each frequency bin has an amplitude`() {
        val sut = createSUT()
        val actual = sut.createFrom(audioFrame, 0F)
        val actualAmplitudes = actual.map { it.amplitude }.toDoubleArray()
        assertArrayEquals(amplitudes, actualAmplitudes)
    }

    @Test
    fun `the amplitudes are calculated from the fewest samples necessary for a minimum frequency`() {
        val sut = createSUT()
        val lowestSupportedFrequency = 20F
        val sampleSize = 4096
        every { sampleSizeFactory.createFor(lowestSupportedFrequency, sampleRate) } returns sampleSize

        sut.createFrom(audioFrame, lowestSupportedFrequency)

        val expectedSamples = samples.takeLast(sampleSize).toDoubleArray()
        verify { amplitudeFactory.createFrom(expectedSamples) }
    }

    @Test
    fun `each frequency bin has a frequency`() {
        val sut = createSUT()
        val actual = sut.createFrom(audioFrame, 0F)
        val actualFrequencies = actual.map { it.frequency }
        assertEquals(expectedFrequencies(), actualFrequencies)
    }

    private fun expectedFrequencies(): List<Float> {
        val numberOfBins = amplitudes.count()
        var frequencies: MutableList<Float> = mutableListOf()

        for (i in 0 until numberOfBins) {
            val frequency = (i * sampleRateToSizeRatio).toFloat()
            frequencies.add(frequency)
        }

        return frequencies
    }
}