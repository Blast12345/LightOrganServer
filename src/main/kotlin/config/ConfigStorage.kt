package config

//class ConfigStorage(
//    private val preferences: Preferences = Preferences.userRoot()
//) {
//
//    private val key = "persistedConfig"
//
//    fun get(): PersistedConfig? {
//        val json = preferences.get(key, "")
//        preferences.get
//        try {
//            return Json.decodeFromString(json)
//        } catch (e: Exception) {
//            println("Failed to get persistedConfig: $e")
//            return null
//        }
//    }
//
//    fun set(persistedConfig: PersistedConfig?) {
//        val json = Json.encodeToString(persistedConfig)
//        preferences.put(key, json)
//    }
//}