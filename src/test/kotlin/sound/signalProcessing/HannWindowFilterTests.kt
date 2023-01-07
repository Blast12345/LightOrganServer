package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

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