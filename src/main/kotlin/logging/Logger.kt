package logging

object Logger {

    fun success(message: String) {
        println("${LogColor.Green.code}$message${LogColor.Default.code}")
    }

    fun error(message: String) {
        println("${LogColor.Red.code}$message${LogColor.Default.code}")
    }

}
