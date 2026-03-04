package bins.octave

import bins.frequency.FrequencyBin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.math.pow

class OctaveBinFactoryUnitTests {

    private fun createSUT(): OctaveBinFactory {
        return OctaveBinFactory(
            rootFrequency = 1f
        )
    }

    // Octave
    @Test
    fun `the root frequency is octave 0`() {
        val sut = createSUT()
        val bin = FrequencyBin(1f, 1f)

        val result = sut.create(bin)

        assertEquals(0, result.octave)
    }

    @Test
    fun `double the root frequency is octave 1`() {
        val sut = createSUT()
        val bin = FrequencyBin(2f, 1f)

        val result = sut.create(bin)

        assertEquals(1, result.octave)
    }

    @Test
    fun `half the root frequency is octave -1`() {
        val sut = createSUT()
        val bin = FrequencyBin(0.5f, 1f)

        val result = sut.create(bin)

        assertEquals(-1, result.octave)
    }

    // Position
    @Test
    fun `the root frequency is position 0`() {
        val sut = createSUT()
        val bin = FrequencyBin(1f, 1f)

        val result = sut.create(bin)

        assertEquals(0f, result.position, 0.001f)
    }

    @Test
    fun `double the root frequency is position 0`() {
        val sut = createSUT()
        val bin = FrequencyBin(2f, 1f)

        val result = sut.create(bin)

        assertEquals(0f, result.position, 0.001f)
    }

    @Test
    fun `midway through octave maps is position 0_5`() {
        val sut = createSUT()
        val midOctaveFrequency = 2f.pow(0.5f) // Octaves are not linear
        val bin = FrequencyBin(midOctaveFrequency, 1f)

        val result = sut.create(bin)

        assertEquals(0.5f, result.position, 0.001f)
    }

    // Magnitude
    @Test
    fun `magnitude is passed through unchanged`() {
        val sut = createSUT()
        val bin = nextFrequencyBin()

        val result = sut.create(bin)

        assertEquals(bin.magnitude, result.magnitude, 0.001f)
    }

}