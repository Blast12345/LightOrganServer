package dsp.peakExtraction

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpectralPeakTests {

    @Test
    fun `magnitudes of peaks are combined using root sum of squares`() {
        val peaks: SpectralPeaks = listOf(
            SpectralPeak(frequency = 100f, magnitude = 3f),
            SpectralPeak(frequency = 200f, magnitude = 4f),
        )

        assertEquals(5.0, peaks.combinedMagnitude, 0.001)
    }

}