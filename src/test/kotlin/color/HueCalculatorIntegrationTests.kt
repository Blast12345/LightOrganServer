package color

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.notes.Note
import sound.notes.Notes
import toolkit.generators.SineWaveGenerator
import wrappers.audioFormat.AudioFormatWrapper

class HueCalculatorIntegrationTests {

    // TODO: Improve accuracy?
    private val tolerance = 0.1F

    @Test
    fun `C notes are red`() {
        val sut = HueCalculator()
        val audioFrame = generateAudioFrame(listOf(Notes.C))

        val actual = sut.calculate(audioFrame)

        assertEquals(0F, actual!!, tolerance)
    }

    @Test
    fun `E notes are teal`() {
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

    private fun generateAudioFrame(notes: List<Note>): AudioFrame {
        val sampleRate = ConfigSingleton.interpolatedSampleSize.toFloat()
        val sineWaveGenerator = SineWaveGenerator(sampleRate)

        val samples = sineWaveGenerator.generate(
            frequencies = notes.map { it.getFrequency(0) },
            sampleSize = sampleRate.toInt()
        )

        val audioFormat = AudioFormatWrapper(sampleRate, sampleRate / 2F, 1)
        return AudioFrame(samples, audioFormat)
    }

}
