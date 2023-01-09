package sound.signalProcessing.zeroPaddingInterpolator

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ZeroPaddingInterpolatorTests {

    private val samples = doubleArrayOf(1.0, 2.0)

    private fun createSUT(): ZeroPaddingInterpolator {
        return ZeroPaddingInterpolator()
    }

    @Test
    fun `when a desired size is larger than the samples passed in then zero-pad to the desired size`() {
        val sut = createSUT()
        val interpolatedSamples = sut.interpolate(samples, 4)
        val expected = doubleArrayOf(1.0, 2.0, 0.0, 0.0)
        assertArrayEquals(expected, interpolatedSamples, 0.001)
    }

    @Test
    fun `when a desired size is equal to than the samples passed in then return the input`() {
        val sut = createSUT()
        val interpolatedSamples = sut.interpolate(samples, 2)
        val expected = doubleArrayOf(1.0, 2.0)
        assertArrayEquals(expected, interpolatedSamples, 0.001)
    }

    @Test
    fun `when a desired size is smaller than the samples passed in then throw an exception`() {
        val sut = createSUT()
        assertThrows<Exception>(
            executable = { sut.interpolate(samples, 1) },
            message = "You cannot interpolate to a smaller size."
        )
    }

}