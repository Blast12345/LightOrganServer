package config

import kotlinx.coroutines.flow.MutableStateFlow

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically)
        )
    }

}