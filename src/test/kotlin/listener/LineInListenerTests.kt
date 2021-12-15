package listener

import org.junit.*
import org.junit.Assert.*
import sound.listener.LineInListener

class LineInListenerTests {

    private val fakeLineInFrame = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)
    private val fakeLineIn = FakeLineIn(500.0, 5, fakeLineInFrame)

    @Test
    fun `getSampleRate returns the sample rate of line-in`() {
        val uut = LineInListener(fakeLineIn)
        val actual = uut.getSampleRate()
        val expected = 500.0
        assertEquals(actual, expected, 1.0)
    }

    @Test
    fun `getSampleSize returns the sample size of line-in`() {
        val uut = LineInListener(fakeLineIn)
        val actual = uut.getSampleSize()
        val expected = 5
        assertEquals(actual, expected)
    }

    @Test
    fun `when line-in has audio frame then getFftData returns processed amplitudes`() {
        val uut = LineInListener(fakeLineIn)
        val actual = uut.getFftData()
        val expected = doubleArrayOf(0.0, 30.0, 20.0)
        assertTrue(actual contentEquals expected)
    }

}