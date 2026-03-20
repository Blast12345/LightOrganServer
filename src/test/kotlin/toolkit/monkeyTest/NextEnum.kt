package toolkit.monkeyTest

inline fun <reified T : Enum<T>> nextEnum(): T {
    val values = enumValues<T>()
    return values.random()
}