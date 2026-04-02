package dsp

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextPositiveFloat

class DecimatorTests {

    private val monoSamples = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f) // [S1, S2, S3, S4, S5, S6]
    private val monoSamplesPart1 = floatArrayOf(1f, 2f, 3f) // [S1, S2, S3]
    private val monoSamplesPart2 = floatArrayOf(4f, 5f, 6f) // [S4, S5, S6]
    private val stereoSamples = floatArrayOf(1f, 10f, 2f, 20f, 3f, 30f, 4f, 40f) // [L1, R1, L2, R2, L3, L4, R4]
    private val sampleRate1 = nextPositiveFloat()
    private val sampleRate2 = nextPositiveFloat()

    // Decimation factor
    @Test
    fun `decimation factor is the ratio of sample rate to target nyquist rate`() {
        val sut = Decimator()

        assertEquals(1, sut.decimationFactor(48000f, 24000f))
        assertEquals(2, sut.decimationFactor(48000f, 12000f))
        assertEquals(4, sut.decimationFactor(48000f, 6000f))
    }

    @Test
    fun `throw when the target nyquist frequency exceeds sample rate capability`() {
        val sut = Decimator()

        assertThrows<IllegalArgumentException> {
            sut.decimationFactor(48000f, 100000f)
        }
    }

    // Decimation
    @Test
    fun `given a factor of 1, samples are returned unchanged`() {
        val sut = Decimator()

        val result = sut.decimate(monoSamples, factor = 1, sampleRate1, channels = 1)

        assertArrayEquals(monoSamples, result)
    }

    @Test
    fun `given a factor of 2, every other sample is kept`() {
        val sut = Decimator()

        val result = sut.decimate(monoSamples, factor = 2, sampleRate1, channels = 1)

        val expected = floatArrayOf(1f, 3f, 5f) // [S1, S3, S5]
        assertArrayEquals(expected, result)
    }

    @Test
    fun `given multichannel audio, decimation keeps channels aligned`() {
        val sut = Decimator()

        val result = sut.decimate(stereoSamples, factor = 2, sampleRate1, channels = 2)

        val expected = floatArrayOf(1f, 10f, 3f, 30f) // [L1, R1, L3, R3]
        assertArrayEquals(expected, result)
    }

    // Decimation state
    @Test
    fun `phase is maintained across consecutive calls`() {
        val sut = Decimator()

        val result1 = sut.decimate(monoSamplesPart1, factor = 2, sampleRate1, channels = 1)
        val result2 = sut.decimate(monoSamplesPart2, factor = 2, sampleRate1, channels = 1)

        // Processing as one block: [S1, S2, S3, S4, S5, S6] with factor 2 → [S1, S3, S5]
        // Split across calls, should produce the same total output
        val combined = result1 + result2
        val expected = floatArrayOf(1f, 3f, 5f) // [S1, S3, S5]
        assertArrayEquals(expected, combined)
    }

    @Test
    fun `phase resets when sample rate changes`() {
        val sut = Decimator()

        val result1 = sut.decimate(monoSamplesPart1, factor = 2, sampleRate1, channels = 1)
        val result2 = sut.decimate(monoSamplesPart2, factor = 2, sampleRate2, channels = 1)

        val expected = floatArrayOf(4f, 6f)
        assertArrayEquals(expected, result2)
    }

    @Test
    fun `phase resets when factor changes`() {
        val sut = Decimator()

        val result1 = sut.decimate(monoSamplesPart1, factor = 2, sampleRate1, channels = 1)
        val result2 = sut.decimate(monoSamplesPart2, factor = 3, sampleRate1, channels = 1)

        val expected = floatArrayOf(4f)
        assertArrayEquals(expected, result2)
    }

    @Test
    fun `phase resets when channel count changes`() {
        val sut = Decimator()

        val result1 = sut.decimate(monoSamplesPart1, factor = 2, sampleRate1, channels = 1)
        val result2 = sut.decimate(stereoSamples, factor = 2, sampleRate1, channels = 2)

        val expected = floatArrayOf(1f, 10f, 3f, 30f)
        assertArrayEquals(expected, result2)
    }

    @Test
    fun `given chunk is smaller than decimation stride, phase carries across without output`() {
        val sut = Decimator()

        // result3 has no output because we have COMPLETELY stepped over it given the factor.
        // But we still need to keep track of the phase for the next set of samples.
        val result1 = sut.decimate(floatArrayOf(1f, 2f, 3f), factor = 5, sampleRate1, channels = 1)
        val result2 = sut.decimate(floatArrayOf(4f, 5f, 6f), factor = 5, sampleRate1, channels = 1)
        val result3 = sut.decimate(floatArrayOf(7f, 8f, 9f), factor = 5, sampleRate1, channels = 1)
        val result4 = sut.decimate(floatArrayOf(10f, 11f, 12f), factor = 5, sampleRate1, channels = 1)

        val combined = result1 + result2 + result3 + result4
        assertArrayEquals(floatArrayOf(1f, 6f, 11f), combined)
    }

    // Decimation - invalid input
    @Test
    fun `given a factor less than 1, then throw`() {
        val sut = Decimator()

        assertThrows<IllegalArgumentException> {
            sut.decimate(monoSamples, factor = 0, sampleRate1, channels = 1)
        }
    }

    @Test
    fun `given channels less than 1, then throw`() {
        val sut = Decimator()

        assertThrows<IllegalArgumentException> {
            sut.decimate(monoSamples, factor = 1, sampleRate1, channels = 0)
        }
    }

}