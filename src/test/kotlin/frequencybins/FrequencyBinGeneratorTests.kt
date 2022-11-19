package frequencybins

import colorListener.sound.frequencyBins.FrequencyBin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sound.frequencybins.FrequencyBinGenerator

class FrequencyBinGeneratorTests {

    private fun createUUT(fakeListener: FakeListener): FrequencyBinGenerator {
        return FrequencyBinGenerator(fakeListener)
    }

    @Test
    fun `when listener has no data then getFrequencyBins returns no bins`() {
        val fftData = doubleArrayOf()
        val fakeListener = FakeListener(fftData)
        val uut = createUUT(fakeListener)
        val actual = uut.getFrequencyBins()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `when listener has data then getFrequencyBins returns bins`() {
        val fftData = doubleArrayOf(0.0, 30.0, 20.0)
        val fakeListener = FakeListener(fftData)
        val uut = createUUT(fakeListener)
        val actual = uut.getFrequencyBins()
        val expected = expectedFrequencyBins()
        assertEquals(actual, expected)
    }

    private fun expectedFrequencyBins(): List<FrequencyBin> {
        return listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(100.0, 30.0),
            FrequencyBin(200.0, 20.0)
        )
    }

}