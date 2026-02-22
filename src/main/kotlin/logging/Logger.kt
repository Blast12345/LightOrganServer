package logging

object Logger {

    fun success(message: String) {
        println("${LogColor.Green.code}SUCCESS: $message${LogColor.Default.code}")
    }

    fun error(message: String) {
        println("${LogColor.Red.code}ERROR: $message${LogColor.Default.code}")
    }

    fun debug(message: String) {
        println("${LogColor.Yellow.code}DEBUG: $message${LogColor.Yellow.code}")
    }

}
