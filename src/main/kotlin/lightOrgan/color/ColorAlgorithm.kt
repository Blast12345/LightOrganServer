package lightOrgan.color

import dsp.peakExtraction.SpectralPeaks

// ENHANCEMENT: Return combined color and individual colors
interface ColorAlgorithm {
    fun calculate(spectralPeaks: SpectralPeaks): SrgbColor
}