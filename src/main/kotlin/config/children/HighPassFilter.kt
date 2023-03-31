package config.children

import kotlinx.serialization.Serializable

@Serializable
data class HighPassFilter(
    val frequency: Float,
    val rollOffRange: Float
)