package sound.signalProcessor.interpolator

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ZeroPaddingInterpolationAlgorithmTests {

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ZeroPaddingInterpolationAlgorithm {
        return ZeroPaddingInterpolationAlgorithm()
    }

    @Test
    fun `when a desired size is larger than the samples passed in then zero-pad to the desired size`() {
        val sut = createSUT()
        val samples = doubleArrayOf(1.0, 2.0)

        val actual = sut.applyTo(samples, 4)

        val expected = doubleArrayOf(1.0, 2.0, 0.0, 0.0)
        assertArrayEquals(expected, actual, 0.001)
    }

    @Test
    fun `when a desired size is equal to than the samples passed in then return the input`() {
        val sut = createSUT()
        val samples = doubleArrayOf(3.0, 4.0)

        val actual = sut.applyTo(samples, 2)

        val expected = doubleArrayOf(3.0, 4.0)
        assertArrayEquals(expected, actual, 0.001)
    }

    @Test
    fun `when a desired size is smaller than the samples passed in then throw an exception`() {
        val sut = createSUT()
        val samples = doubleArrayOf(5.0, 6.0)

        assertThrows<Exception>(
            executable = { sut.applyTo(samples, 1) },
            message = "You cannot interpolate to a smaller size."
        )
    }

}
