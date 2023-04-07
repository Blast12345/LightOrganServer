package lightOrgan.sound.signalProcessing.hannFilter

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HannFilterNormalizerTests {

    private val windowedSamples = doubleArrayOf(1.0, 2.0, 3.0)

    private fun createSUT(): HannFilterNormalizer {
        return HannFilterNormalizer()
    }

    @Test
    fun `normalize a signal that has gone through a hann filter by multiplying the amplitudes by a correction factor of two`() {
        val sut = createSUT()
        val correctedSignal = sut.normalize(windowedSamples)
        val expected = doubleArrayOf(2.0, 4.0, 6.0)
        Assertions.assertArrayEquals(expected, correctedSignal, 0.001)
    }

}