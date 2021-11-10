import mocking.MockLineListener
import sound.*
import org.junit.*
import org.junit.Assert.assertEquals

class FrequencyBinGeneratorTest {

    @Test
    fun `when line listener has frequencies with no amplitudes then getFrequencyBins returns those bins`() {
        val listener = listenerWithNoAmplitudes()
        val uut = FrequencyBinGenerator(listener)
        val actual = uut.getFrequencyBins()
        val expected = getFakeFrequencyBinsWithNoAmplitudes()
        assertEquals(actual, expected)
    }

    private fun listenerWithNoAmplitudes(): MockLineListener {
        val fftData = doubleArrayOf(0.0, 0.0, 0.0)
        return MockLineListener(fftData)
    }

    private fun getFakeFrequencyBinsWithNoAmplitudes(): List<FrequencyBin> {
        return listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(100.0, 0.0),
            FrequencyBin(200.0, 0.0)
        )
    }
    @Test
    fun `when line listener has frequencies with amplitudes then getFrequencyBins returns those bins`() {
        val listener = listenerWithAmplitudes()
        val uut = FrequencyBinGenerator(listener)
        val actual = uut.getFrequencyBins()
        val expected = getFakeFrequencyBinsWithAmplitudes()
        assertEquals(actual, expected)
    }

    private fun listenerWithAmplitudes(): MockLineListener {
        val fftData = doubleArrayOf(0.0, 30.0, 20.0)
        return MockLineListener(fftData)
    }

    private fun getFakeFrequencyBinsWithAmplitudes(): List<FrequencyBin> {
        return listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(100.0, 30.0),
            FrequencyBin(200.0, 20.0)
        )
    }

}