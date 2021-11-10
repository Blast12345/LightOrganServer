import sound.*
import org.junit.*
import org.junit.Assert.assertEquals

class FrequencyBinGeneratorTest {

    @Test
    fun `when getFrequencyBins then returns frequency bins`() {
        val uut = FrequencyBinGenerator()
        val actual = uut.getFrequencyBins()
        val expected = getFakeFrequencyBins()
        assertEquals(actual, expected)
    }

    private fun getFakeFrequencyBins(): List<FrequencyBin> {
        return listOf(
            fakeFrequencyBin100hz(),
            fakeFrequencyBin200hz(),
            fakeFrequencyBin300hz()
        )
    }

    private fun fakeFrequencyBin100hz(): FrequencyBin {
        return FrequencyBin(100.0, 0.0)
    }

    private fun fakeFrequencyBin200hz(): FrequencyBin {
        return FrequencyBin(200.0, 50.0)
    }

    private fun fakeFrequencyBin300hz(): FrequencyBin {
        return FrequencyBin(300.0, 10.0)
    }

}