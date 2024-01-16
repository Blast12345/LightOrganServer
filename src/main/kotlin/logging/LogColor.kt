package logging

enum class LogColor(val code: String) {
    Black("\u001B[30m"),
    Red("\u001B[31m"),
    Green("\u001B[32m"),
    Yellow("\u001B[33m"),
    Blue("\u001B[34m"),
    Magenta("\u001B[35m"),
    Cyan("\u001B[36m"),
    White("\u001B[37m"),
    Default("\u001B[0m")
}
