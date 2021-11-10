import sound.*
import org.junit.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.collection.*


class SoundServiceTest {

    @Test
    fun `when getFrequencyBins then returns frequency bins`() {
        val uut = SoundService()
        val actual = uut.getFrequencyBins()
        val expected = getStubFrequencyBins()

        Assert.assertThat(actual,
                IsIterableContainingInOrder.contains(expected.toArray()));
    }

    private fun getStubFrequencyBins(): List<FrequencyBin> {
        return listOf(stubFrequencyBin100hz(),
                stubFrequencyBin200hz(),
                stubFrequencyBin300hz())
    }

    private fun stubFrequencyBin100hz(): FrequencyBin {
        return FrequencyBin(100.0, 0.0)
    }

    private fun stubFrequencyBin200hz(): FrequencyBin {
        return FrequencyBin(200.0, 50.0)
    }

    private fun stubFrequencyBin300hz(): FrequencyBin {
        return FrequencyBin(300.0, 10.0)
    }

}