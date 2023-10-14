package sound.signalProcessor

import sound.signalProcessor.filters.latest.LatestSamplesFilter
import sound.signalProcessor.filters.window.HannFilter
import sound.signalProcessor.filters.window.WindowFilter
import sound.signalProcessor.interpolator.ZeroPaddingInterpolator

class SignalProcessor(
    private val latestSamplesFilter: LatestSamplesFilter = LatestSamplesFilter(),
    private val windowFilter: WindowFilter = HannFilter(),
    private val sampleInterpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator()
) {

    fun process(samples: DoubleArray): DoubleArray {
        return samples
            .reduceDuration()
            .reduceSpectralLeakage()
            .increaseFrequencyResolution()
    }

    private fun DoubleArray.reduceDuration(): DoubleArray {
        return latestSamplesFilter.filter(this)
    }

    private fun DoubleArray.reduceSpectralLeakage(): DoubleArray {
        return windowFilter.applyTo(this)
    }

    private fun DoubleArray.increaseFrequencyResolution(): DoubleArray {
        return sampleInterpolator.interpolate(this)
    }

}