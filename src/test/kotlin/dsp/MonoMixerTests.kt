package dsp

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame

class MonoMixerTests {

    val channel1 = floatArrayOf(2F, 4F, 6F)
    val channel2 = floatArrayOf(0F, 0F, 0F)

    val monoAudio = nextAudioFrame(listOf(channel1))
    val stereoAudio = nextAudioFrame(listOf(channel1, channel2))

    @Test
    fun `given a mono signal, the return the original signal`() {
        val sut = MonoMixer()

        val result = sut.mix(monoAudio)

        assertEquals(monoAudio, result)
    }

    @Test
    fun `given a multi-channel signal, samples are mixed down by averaging the channels`() {
        val sut = MonoMixer()

        val result = sut.mix(stereoAudio)

        val expected = floatArrayOf(1F, 2F, 3F)
        assertArrayEquals(expected, result.samples, 0.001F)
        assertEquals(stereoAudio.format.sampleRate, result.format.sampleRate)
        assertEquals(stereoAudio.format.bitDepth, result.format.bitDepth)
        assertEquals(1, result.format.channels)
    }

}