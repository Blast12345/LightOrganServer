package config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import scopes.IoScope

class ConfigPersister(
    private val persistedConfig: PersistedConfig = PersistedConfig(),
    private val scope: CoroutineScope = IoScope
) {

    fun persist(config: Config) {
        scope.launch {
            addObserverForStartAutomaticallyState(config)
        }
    }

    private suspend fun addObserverForStartAutomaticallyState(config: Config) {
        config.startAutomatically.collect {
            persistedConfig.startAutomatically = it
        }
    }

}