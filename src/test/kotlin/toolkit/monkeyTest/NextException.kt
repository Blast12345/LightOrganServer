package toolkit.monkeyTest

fun nextException(prefix: String): Exception {
    return Exception("$prefix: ${nextString()}")
}