package toolkit

import java.nio.ByteBuffer

fun byteArrayOf(vararg longs: Long): ByteArray {
    var buffer = ByteArray(0)

    for (long in longs) {
        val longInBytes = longToBytes(long)
        buffer += longInBytes
    }

    return buffer
}

private fun longToBytes(long: Long): ByteArray {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    buffer.putLong(long)
    return buffer.array()
}