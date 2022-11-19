package colorListener.sound.fft

import org.junit.Assert.assertArrayEquals
import org.junit.Test

// NOTE: These are characterization tests due to the complex math.
class HannWindowFilterTests {

    private fun createSUT(): HannWindowFilter {
        return HannWindowFilter()
    }

    @Test
    fun `apply the hann window filter`() {
        val sut = createSUT()
        val signal = doubleArrayOf(0.0, 1.1, 2.2, 3.3)
        val actual = sut.filter(signal)
        val expected = doubleArrayOf(0.0, 0.825, 1.65, 0.0)
        assertArrayEquals(expected, actual, 0.001)
    }

}