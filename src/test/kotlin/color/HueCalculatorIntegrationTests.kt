package color

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.filters.Crossover
import sound.notes.Note
import sound.notes.Notes
import toolkit.generators.SineWave
import toolkit.generators.SineWaveGenerator
import toolkit.monkeyTest.nextConfig
import wrappers.audioFormat.AudioFormatWrapper

class HueCalculatorIntegrationTests {

    // TODO: Improve accuracy?
    private val tolerance = 0.1F
    private val config = nextConfig(
        hueSampleSize = 65536,
        hueLowCrossover = Crossover(
            stopFrequency = Notes.C.getFrequency(octave = 0),
            cornerFrequency = Notes.C.getFrequency(octave = 1)
        ),
        hueHighCrossover = Crossover(
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
    fun `C notes are red`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.C))

        val actual = sut.calculate(audioFrame)

        assertEquals(0F, actual!!, tolerance)
    }

    @Test
    fun `E notes are green`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.E))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.33F, actual!!, tolerance)
    }

    @Test
    fun `F# notes are teal`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.F_SHARP))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.5F, actual!!, tolerance)
    }

    @Test
    fun `G# notes are blue`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.G_SHARP))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.66F, actual!!, tolerance)
    }

    @Test
    fun `A notes are purple`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.A))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.75F, actual!!, tolerance)
    }

    @Test
    fun `D# and A at the same time is teal`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.D_SHARP, Notes.A))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.5F, actual!!, tolerance)
    }

    @Test
    fun `F and G at the same time is teal`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.F, Notes.G))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.5F, actual!!, tolerance)
    }

    @Test
    fun `D# and F at the same time is green`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.D_SHARP, Notes.F))

        val actual = sut.calculate(audioFrame)

        assertEquals(0.33F, actual!!, tolerance)
    }

    // TODO: Weighting
    private fun generateAudioFrame(notes: List<Note>): AudioFrame {
        val sampleRate = config.interpolatedSampleSize.toFloat()
        val sineWaveGenerator = SineWaveGenerator(sampleRate)
        val sineWaves = notes.map { SineWave(it.getFrequency(1), 1F) }

        val samples = sineWaveGenerator.generate(
            sineWaves = sineWaves,
            sampleSize = sampleRate.toInt()
        )

        val audioFormat = AudioFormatWrapper(sampleRate, sampleRate / 2F, 1)
        return AudioFrame(samples, audioFormat)
    }

}
