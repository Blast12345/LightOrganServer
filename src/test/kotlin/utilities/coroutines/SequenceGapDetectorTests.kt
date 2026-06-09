package utilities.coroutines

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SequenceGapDetectorTests {

    private fun createSUT(): SequenceGapDetector {
        return SequenceGapDetector()
    }

    // No gap
    @Test
    fun `first check has no gap`() {
        val sut = createSUT()

        val result = sut.check(5L)

        assertEquals(0L, result)
    }

    @Test
    fun `sequential numbers have no gap`() {
        val sut = createSUT()
        sut.check(5L)

        val result = sut.check(6L)

        assertEquals(0L, result)
    }

    // Forward gap
    @Test
    fun `a forward gap is reported`() {
        val sut = createSUT()
        sut.check(0L)

        val result = sut.check(5L)

        assertEquals(4L, result)
    }

    @Test
    fun `after a forward gap, sequential numbers resume normally`() {
        val sut = createSUT()
        sut.check(0L)
        sut.check(5L)

        val result = sut.check(6L)

        assertEquals(0L, result)
    }

    // Backward gap
    @Test
    fun `a backwards gap is reported`() {
        val sut = createSUT()
        sut.check(5L)

        val result = sut.check(0L)

        assertEquals(-6L, result)
    }

    @Test
    fun `after a backwards gap, sequential numbers resume normally`() {
        val sut = createSUT()
        sut.check(5L)
        sut.check(0L)

        val result = sut.check(6L)

        assertEquals(0L, result)
    }
    
}