package config.children

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val ip: String,
    val port: Int = 9999
)