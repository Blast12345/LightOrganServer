import config.ColorWheel
import config.Config
import config.HighPassFilter
import config.MagnitudeEstimationStrategy

class DefaultConfig : Config {
    override val colorWheel = ColorWheel(40F, 120F, 0.25F)
    override val highPassFilter = HighPassFilter(120F, 15F)
    override val sampleSize = 8192
    override val interpolatedSampleSize = 65536
    override val magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5)
    override val magnitudeMultiplier: Float = 1.25F
}