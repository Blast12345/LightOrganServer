package config

class ConfigProvider {

    companion object {
        private val sharedConfig = ConfigFactory().create()
        private val persistenceHelper: ConfigPersistenceHelper = ConfigPersistenceHelper()

        init {
            persistenceHelper.persistStateChanges(sharedConfig)
        }
    }

    val current: Config
        get() = sharedConfig

}