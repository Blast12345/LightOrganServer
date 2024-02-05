package sound.signalProcessor

import sound.signalProcessor.filters.window.HannFilter
import sound.signalProcessor.filters.window.WindowFilter
import sound.signalProcessor.interpolator.ZeroPaddingInterpolator

// TODO: Rename
class SignalProcessor(
    private val windowFilter: WindowFilter = HannFilter(),
    private val sampleInterpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator()
) {

    fun process(samples: DoubleArray): DoubleArray {
        return samples
            .reduceSpectralLeakage()
            .increaseFrequencyResolution()
    }

    private fun DoubleArray.reduceSpectralLeakage(): DoubleArray {
        return windowFilter.applyTo(this)
    }

    private fun DoubleArray.increaseFrequencyResolution(): DoubleArray {
        return sampleInterpolator.interpolate(this)
    }

}
