package config.children

import kotlinx.serialization.Serializable

@Serializable
data class ColorWheel(
    val startingFrequency: Float,
    val endingFrequency: Float,
    val offset: Float
)