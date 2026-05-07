package dsp.bins

data class PeakWithProminence(
    val frequencyBin: FrequencyBin,
    val prominence: Float
)

class ProminenceCalculator {

    fun calculate(peaks: FrequencyBins, spectrum: FrequencyBins): List<PeakWithProminence> {
        if (peaks.isEmpty()) return emptyList()

        val sortedPeaks = peaks.sortedBy { it.frequency }
        val sortedSpectrum = spectrum.sortedBy { it.frequency }

        return sortedPeaks.map { peak ->
            val higherPeaks = sortedPeaks.filter { it.magnitude > peak.magnitude }

            val keyCol = if (higherPeaks.isEmpty()) {
                0f
            } else {
                higherPeaks.maxOf { higherPeak ->
                    lowestPointBetween(sortedSpectrum, peak.frequency, higherPeak.frequency)
                }
            }

            PeakWithProminence(
                frequencyBin = peak,
                prominence = peak.magnitude - keyCol
            )
        }
    }

    private fun lowestPointBetween(spectrum: List<FrequencyBin>, freq1: Float, freq2: Float): Float {
        val from = minOf(freq1, freq2)
        val to = maxOf(freq1, freq2)
        return spectrum
            .filter { it.frequency in from..to }
            .minOfOrNull { it.magnitude }
            ?: 0f
    }
}
//
//class ProminenceCalculator {
//
//    fun calculate(peaks: FrequencyBins, spectrum: FrequencyBins): List<PeakWithProminence> {
//        if (peaks.isEmpty()) return emptyList()
//
//        val sortedPeaks = peaks.sortedBy { it.frequency }
//        val sortedSpectrum = spectrum.sortedBy { it.frequency }
//
//        return sortedPeaks.mapIndexed { index, peak ->
//            val leftValley = findValley(
//                spectrum = sortedSpectrum,
//                from = sortedPeaks.getOrNull(index - 1)?.frequency ?: sortedSpectrum.first().frequency,
//                to = peak.frequency
//            )
//
//            val rightValley = findValley(
//                spectrum = sortedSpectrum,
//                from = peak.frequency,
//                to = sortedPeaks.getOrNull(index + 1)?.frequency ?: sortedSpectrum.last().frequency
//            )
//
//            val referenceLevel = maxOf(leftValley, rightValley)
//
//            PeakWithProminence(
//                frequencyBin = peak,
//                prominence = peak.magnitude - referenceLevel
//            )
//        }
//    }
//
//    private fun findValley(spectrum: List<FrequencyBin>, from: Float, to: Float): Float {
//        return spectrum
//            .filter { it.frequency in from..to }
//            .minOfOrNull { it.magnitude }
//            ?: 0f
//    }
//
//}