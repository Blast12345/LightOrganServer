package toolkit.monkeyTest

import kotlin.random.Random

fun nextByteArray(size: Int = Random.nextInt(1024)): ByteArray {
    return Random.nextBytes(size)
}