package utilities.coroutines

data class Sequenced<T>(
    val sequenceNumber: Long,
    val value: T
)

fun <T> T.asSequenced(sequenceNumber: Long): Sequenced<T> {
    return Sequenced(sequenceNumber, value = this)
}