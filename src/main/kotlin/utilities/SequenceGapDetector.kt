package utilities

import logging.Logger

class SequenceGapDetector(
    private val name: String
) {

    enum class Result {
        OK,
        GAP,
        RESET
    }

    private var expectedSequenceNumber: Long? = null

    fun check(sequenceNumber: Long): Result {
        val expected = expectedSequenceNumber ?: sequenceNumber
        val delta = sequenceNumber - expected

        expectedSequenceNumber = sequenceNumber + 1

        return when {
            (delta > 0) -> {
                Logger.warning("$name: gap of $delta (expected $expected, got $sequenceNumber)")
                Result.GAP
            }

            (delta < 0) -> {
                Logger.debug("$name: sequence reset (expected $expected, got $sequenceNumber)")
                Result.RESET
            }

            else -> Result.OK
        }
    }

}