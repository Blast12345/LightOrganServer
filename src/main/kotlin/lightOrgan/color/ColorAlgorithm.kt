package lightOrgan.color

import color.SrgbColor
import dsp.peakExtraction.SpectralPeaks

interface ColorAlgorithm {
    fun calculate(spectralPeaks: SpectralPeaks): SrgbColor
}