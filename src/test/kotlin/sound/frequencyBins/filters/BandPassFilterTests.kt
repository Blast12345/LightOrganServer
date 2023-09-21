package sound.frequencyBins.filters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class BandPassFilterTests {

    private val highPassFrequency = 20F
    private val lowPassFrequency = 120F
    private val frequencyBinList = listOf(
        FrequencyBin(10F, 1F),
        FrequencyBin(20F, 1F),
        FrequencyBin(100F, 1F),
        FrequencyBin(120F, 1F),
        FrequencyBin(1000F, 1F)
    )

    private fun createSUT(): BandPassFilter {
        return BandPassFilter(
//            highPassCornerFrequency = highPassFrequency,
//            lowPassCornerFrequency = lowPassFrequency
        )
    }

    @Test
    fun `omit frequencies outside of band`() {
        val sut = createSUT()

        val actual = sut.filter(frequencyBinList)
        val actualFrequencies = actual.map { it.frequency }

        val expected = listOf(20F, 100F, 120F)
        assertEquals(expected, actualFrequencies)
    }

    @Test
    fun `magnitudes approach 0 as frequencies approach low pass frequency`() {
        val sut = createSUT()


        val actual = sut.filter(createBins())
        val actualMagnitudes = actual.map { it.magnitude }

        for (bin in actual) {
            println(bin.magnitude)
        }

        val expected = listOf(0F, 1F, 0F)
        assertEquals(expected, actualMagnitudes)
    }

    private fun createBins(): FrequencyBinList {
        val bins = mutableListOf<FrequencyBin>()

        for (i in 1..160) {
            bins.add(FrequencyBin(i.toFloat(), 1F))
        }

        return bins
    }
}