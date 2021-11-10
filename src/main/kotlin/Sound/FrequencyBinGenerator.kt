package sound

class FrequencyBinGenerator {

    fun getFrequencyBins(): List<FrequencyBin> {
        return listOf(
            stubFrequencyBin100hz(),
            stubFrequencyBin200hz(),
            stubFrequencyBin300hz()
        )
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