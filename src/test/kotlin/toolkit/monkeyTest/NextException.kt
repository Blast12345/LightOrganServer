package toolkit.monkeyTest

fun nextException(prefix: String = "exception"): Exception {
    return Exception(nextString(prefix))
}