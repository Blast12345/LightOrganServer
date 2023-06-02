package config

import Scopes.IoScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ConfigPersistenceHelper(
    private val persistedConfig: PersistedConfig = PersistedConfig(),
    private val scope: CoroutineScope = IoScope
) {

    fun persistStateChanges(config: Config) {
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