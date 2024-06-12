package color

import config.ConfigSingleton
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.filters.Crossover
import sound.notes.Notes
import toolkit.monkeyTest.nextConfig

class BrightnessCalculatorIntegrationTests {

    // TODO: Improve accuracy?
    private val tolerance = 0.1F
    private val config = nextConfig(
        brightnessSampleSize = 65536,
        brightnessLowCrossover = Crossover(
            stopFrequency = Notes.C.getFrequency(octave = 0),
            cornerFrequency = Notes.C.getFrequency(octave = 1)
        ),
        brightnessHighCrossover = Crossover(
            cornerFrequency = Notes.C.getFrequency(octave = 2),
            stopFrequency = Notes.C.getFrequency(octave = 3)
        ),
        interpolatedSampleSize = 65536
    )

    @BeforeEach
    fun setup() {
        ConfigSingleton = config
    }

    @Test
    fun `the brightness corresponds to the greatest magnitude`() {
//        val sut = BrightnessCalculator()
//        val magnitude = 0.5F
//        val audioFrame = generateAudioFrame(magnitude)
//
//        val actual = sut.calculate(audioFrame)
//
//        assertEquals(magnitude, actual!!, tolerance)
    }

    // TODO: Pairs
//    private fun generateAudioFrame(frequency: Float, magnitude: Float): AudioFrame {
//        val sampleRate = config.interpolatedSampleSize.toFloat()
//        val sineWaveGenerator = SineWaveGenerator(sampleRate)
//
//        val samples = sineWaveGenerator.generate(
//            frequencies = listOf(frequency),
//            sampleSize = sampleRate.toInt()
//        )
//
//        val audioFormat = AudioFormatWrapper(sampleRate, sampleRate / 2F, 1)
//        return AudioFrame(samples, audioFormat)
//    }

}
