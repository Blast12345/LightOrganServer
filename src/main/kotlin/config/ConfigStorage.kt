package config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class ConfigStorage(
    private val preferences: Preferences = Preferences.userRoot()
) {

    private val key = "config"

    fun get(): Config? {
        val json = preferences.get(key, "")

        try {
            return Json.decodeFromString(json)
        } catch (e: Exception) {
            println("Failed to get config: $e")
            return null
        }
    }

    fun set(config: Config?) {
        val json = Json.encodeToString(config)
        preferences.put(key, json)
    }
}