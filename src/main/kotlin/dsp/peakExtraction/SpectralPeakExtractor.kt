package dsp.peakExtraction

import dsp.bins.FrequencyBins

interface SpectralPeakExtractor {
    fun extract(spectrum: FrequencyBins): SpectralPeaks
}