package logging

object Logger {

    val enabled: Boolean = true // TODO: Disable this for unit tests

    fun success(message: String) {
        if (!enabled) return
        println("${LogColor.Green.code}SUCCESS: $message${LogColor.Default.code}")
    }

    fun warning(message: String) {
        if (!enabled) return
        println("${LogColor.Orange.code}WARNING: $message${LogColor.Default.code}")
    }

    fun error(message: String) {
        if (!enabled) return
        println("${LogColor.Red.code}ERROR: $message${LogColor.Default.code}")
    }

    fun debug(message: String) {
        if (!enabled) return
        println("${LogColor.Yellow.code}DEBUG: $message${LogColor.Yellow.code}")
    }

}
