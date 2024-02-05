package sound.signalProcessor

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class AudioTrimmerTests {

    val samples = doubleArrayOf(1.0, 2.0, 3.0)

    private fun createSUT(): AudioTrimmer {
        return AudioTrimmer()
    }

    @Test
    fun `take the latest N samples`() {
        val sut = createSUT()

        assertArrayEquals(doubleArrayOf(3.0), sut.trim(samples, 1))
        assertArrayEquals(doubleArrayOf(2.0, 3.0), sut.trim(samples, 2))
        assertArrayEquals(doubleArrayOf(1.0, 2.0, 3.0), sut.trim(samples, 3))
    }

}
