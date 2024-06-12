package sound.signalProcessor

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class SampleTrimmerTests {

    private val samples = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)

    private fun createSUT(): SampleTrimmer {
        return SampleTrimmer()
    }

    @Test
    fun `get the latest n samples`() {
        val sut = createSUT()

        val actual = sut.trim(
            samples = samples,
            length = 3
        )

        assertArrayEquals(doubleArrayOf(3.0, 4.0, 5.0), actual)
    }

}
