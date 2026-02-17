package toolkit.monkeyTest

import java.util.*

fun nextString(prefix: String? = null): String {
    val output = StringBuilder()

    if (prefix != null) {
        output.append(prefix)
        output.append("-")
    }

    val randomString = UUID.randomUUID().toString().take(4)
    output.append(randomString)

    return output.toString()
}
