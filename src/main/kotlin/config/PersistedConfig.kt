package config

import java.util.prefs.Preferences

class PersistedConfig(
    private val preferences: Preferences = Preferences.userRoot()
) {

    var startAutomatically: Boolean
        get() = preferences.getBoolean(startAutomaticallyKey, false)
        set(value) = preferences.putBoolean(startAutomaticallyKey, value)
    private val startAutomaticallyKey = "startAutomaticallyKey"

}
