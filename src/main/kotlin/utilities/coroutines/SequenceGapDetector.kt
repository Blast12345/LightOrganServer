package utilities.coroutines

class SequenceGapDetector {

    private var expected: Long? = null

    fun check(sequenceNumber: Long, onGap: (size: Long) -> Unit) {
        val expected = this.expected ?: sequenceNumber
        val delta = sequenceNumber - expected

        if (delta != 0L) {
            onGap(delta)
        }

        if (delta >= 0) {
            this.expected = sequenceNumber + 1
        }
    }
    
}