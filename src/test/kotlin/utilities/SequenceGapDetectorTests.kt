package utilities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveLong
import toolkit.monkeyTest.nextString

class SequenceGapDetectorTests {

    private val name = nextString("name")

    private fun createSUT(): SequenceGapDetector {
        return SequenceGapDetector(name)
    }

    @Test
    fun `first check always passes`() {
        val sut = createSUT()
        val sequenceNumber = nextPositiveLong()

        val actual = sut.check(sequenceNumber)

        assertEquals(SequenceGapDetector.Result.OK, actual)
    }

    @Test
    fun `sequential numbers are ok`() {
        val sut = createSUT()
        sut.check(0)

        val actual1 = sut.check(1)
        val actual2 = sut.check(2)

        assertEquals(SequenceGapDetector.Result.OK, actual1)
        assertEquals(SequenceGapDetector.Result.OK, actual2)
    }

    @Test
    fun `a forward gap is detected`() {
        val sut = createSUT()
        sut.check(0)

        val actual = sut.check(5)

        assertEquals(SequenceGapDetector.Result.GAP, actual)
    }

    @Test
    fun `a backward gap is a reset`() {
        val sut = createSUT()
        sut.check(5)

        val actual = sut.check(0)

        assertEquals(SequenceGapDetector.Result.RESET, actual)
    }

    @Test
    fun `after a gap, sequential numbers resume normally`() {
        val sut = createSUT()
        sut.check(0)
        sut.check(5)

        val actual = sut.check(6)

        assertEquals(SequenceGapDetector.Result.OK, actual)
    }

    @Test
    fun `after a reset, sequential numbers resume normally`() {
        val sut = createSUT()
        sut.check(10)
        sut.check(0)

        val actual = sut.check(1)

        assertEquals(SequenceGapDetector.Result.OK, actual)
    }

}