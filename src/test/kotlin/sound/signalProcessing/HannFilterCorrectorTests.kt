package sound.signalProcessing

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HannFilterCorrectorTests {

    private val windowedSamples = doubleArrayOf(1.0, 2.0, 3.0)

    private fun createSUT(): HannFilterCorrector {
        return HannFilterCorrector()
    }

    @Test
    fun `correct samples that have gone through a hann filter by multiplying the amplitudes by a correction factor of two`() {
        val sut = createSUT()
        val correctedSignal = sut.correct(windowedSamples)
        val expectedSignal = doubleArrayOf(2.0, 4.0, 6.0)
        Assertions.assertArrayEquals(expectedSignal, correctedSignal, 0.001)
    }

}