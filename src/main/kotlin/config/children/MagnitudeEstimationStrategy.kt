package config.children

import kotlinx.serialization.Serializable

@Serializable
data class MagnitudeEstimationStrategy(
    val numberOfPeaksToUse: Int
)