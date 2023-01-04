package toolkit

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun byteArrayOf(
    vararg shorts: Short,
    byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
): ByteArray {
    var buffer = ByteArray(0)

    for (short in shorts) {
        val shortInBytes = shortToBytes(short, byteOrder)
        buffer += shortInBytes
    }

    return buffer
}

private fun shortToBytes(short: Short, byteOrder: ByteOrder): ByteArray {
    val buffer = ByteBuffer.allocate(Short.SIZE_BYTES)
    buffer.order(byteOrder)
    buffer.putShort(short)
    return buffer.array()
}