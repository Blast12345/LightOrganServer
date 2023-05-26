package config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ConfigPersistenceHelper(
    private val persistedConfig: PersistedConfig = PersistedConfig(),
    // Enhancement: Create more granular dispatchers and/or scopes
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
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