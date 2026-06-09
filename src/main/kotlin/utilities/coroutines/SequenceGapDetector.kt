package utilities.coroutines

class SequenceGapDetector {

    private var expected: Long? = null

    fun check(sequenceNumber: Long): Long {
        val expected = this.expected ?: sequenceNumber
        val delta = sequenceNumber - expected

        if (delta >= 0) {
            this.expected = sequenceNumber + 1
        }

        return delta
    }

}