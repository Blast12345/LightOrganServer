package toolkit.byteArray

import java.nio.ByteBuffer

fun byteArrayOf(vararg ints: Int): ByteArray {
    var buffer = ByteArray(0)

    for (int in ints) {
        val intInBytes = intToBytes(int)
        buffer += intInBytes
    }

    return buffer
}

private fun intToBytes(int: Int): ByteArray {
    val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
    buffer.putInt(int)
    return buffer.array()
}
