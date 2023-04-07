package lightOrgan.sound.signalProcessing.hannFilter

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class HannFilterTests {

    private val samples = doubleArrayOf(0.0, 1.1, 2.2, 3.3)

    private fun createSUT(): HannFilter {
        return HannFilter()
    }

    @Test
    fun `process samples using the hann filter`() {
        val sut = createSUT()
        val actual = sut.filter(samples)
        val expected = doubleArrayOf(0.0, 0.825, 1.65, 0.0)
        assertArrayEquals(expected, actual, 0.001)
    }

}