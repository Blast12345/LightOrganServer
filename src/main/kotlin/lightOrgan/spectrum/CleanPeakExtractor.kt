package lightOrgan.spectrum

import dsp.bins.FrequencyBins
import dsp.clean.InterpolatedCleanAlgorithm1D
import dsp.windowing.Window

data class Peak(
    val frequency: Double,
    val magnitude: Double
)

typealias Peaks = List<Peak>

interface PeakExtractor {
    fun extract(spectrum: FrequencyBins): List<Peak>
}

class CleanPeakExtractor(
    private val window: Window,
    private val windowSize: Int,
    private val fftLength: Int,
    val cleanAlgorithm: InterpolatedCleanAlgorithm1D = InterpolatedCleanAlgorithm1D(),
) : PeakExtractor {


    override fun extract(spectrum: FrequencyBins): List<Peak> {
        val dirtySignal = spectrum.map { it.value }

        val result = cleanAlgorithm.clean(
            dirtySignal,
            impulseResponseFunction,
            loopGain = 0.1,
            maxIterations = 200,
            magnitudeThreshold = 40.0
        )

        val binSpacing = spectrum[1].frequency - spectrum[0].frequency

        return result.components.map { component ->
            Peak(
                frequency = spectrum[0].frequency + component.position * binSpacing,
                magnitude = component.value.abs()
            )
        }
    }

}