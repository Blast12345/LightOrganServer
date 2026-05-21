package lightOrgan.color

import color.RgbColor
import dsp.peakExtraction.SpectralPeaks

interface ColorAlgorithm {
    fun calculate(spectralPeaks: SpectralPeaks): RgbColor
}