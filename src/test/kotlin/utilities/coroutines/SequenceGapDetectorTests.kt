package utilities.coroutines

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class SequenceGapDetectorTests {

    private fun createSUT(): SequenceGapDetector {
        return SequenceGapDetector()
    }

    // No gap
    @Test
    fun `first check has no gap`() {
        val sut = createSUT()
        var reportedGap: Long? = null

        sut.check(5L) { reportedGap = it }

        assertNull(reportedGap)
    }

    @Test
    fun `sequential numbers have no gap`() {
        val sut = createSUT()
        var reportedGap: Long? = null

        sut.check(5L) { reportedGap = it }
        sut.check(6L) { reportedGap = it }

        assertNull(reportedGap)
    }

    // Forward gap
    @Test
    fun `a forward gap is reported`() {
        val sut = createSUT()
        var reportedGap: Long? = null

        sut.check(0L) { reportedGap = it }
        sut.check(5L) { reportedGap = it }

        assertEquals(4L, reportedGap)
    }

    @Test
    fun `after a forward gap, sequential numbers resume normally`() {
        val sut = createSUT()
        var gapsReported = 0

        sut.check(0L) { gapsReported++ }
        sut.check(5L) { gapsReported++ }
        sut.check(6L) { gapsReported++ }

        assertEquals(1, gapsReported)
    }

    // Backward gap
    @Test
    fun `a backwards gap is reported`() {
        val sut = createSUT()
        var reportedGap: Long? = null

        sut.check(5L) { reportedGap = it }
        // next expected sequence is 6
        sut.check(0L) { reportedGap = it }

        assertEquals(-6L, reportedGap)
    }

    @Test
    fun `after a backwards gap, sequential numbers resume normally`() {
        val sut = createSUT()
        var gapsReported = 0

        sut.check(5L) { gapsReported++ }
        sut.check(0L) { gapsReported++ }
        sut.check(6L) { gapsReported++ }

        assertEquals(1, gapsReported)
    }

}