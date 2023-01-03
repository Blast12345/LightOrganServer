package sound.frequencyBins

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.fft.AmplitudeFactory
import sound.input.samples.NormalizedAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import kotlin.random.Random

class FrequencyBinsFactoryTests {

    private lateinit var amplitudeFactory: AmplitudeFactory // TODO: Interface

    private val samples = Random.nextDoubleArray(length = 44100)
    private val sampleRate = 44100F
    private val audioFrame = NormalizedAudioFrame(samples, sampleRate)
    private val amplitudes = Random.nextDoubleArray(length = 44100)

    @BeforeEach
    fun setup() {
        amplitudeFactory = mockk()
        every { amplitudeFactory.createFrom(any()) } returns amplitudes
    }

    private fun createSUT(): FrequencyBinsFactory {
        return FrequencyBinsFactory(
            amplitudeFactory = amplitudeFactory
        )
    }

    @Test
    fun `the amplitudes of bins are extracted from the audio`() {
        val sut = createSUT()
        val actual = sut.createFrom(audioFrame, 0F)
        val actualAmplitudes = actual.map { it.amplitude }.toDoubleArray()
        assertArrayEquals(amplitudes, actualAmplitudes)
    }

    @Test
    // TODO: Do additional characteristic testing
    fun `the frequencies of bins are inferred from index of amplitude`() {
        val sut = createSUT()
        val actual = sut.createFrom(audioFrame, 0F)
        val actualFrequencies = actual.map { it.frequency }.toFloatArray()
        assertEquals(40F, actualFrequencies[40])
    }

}