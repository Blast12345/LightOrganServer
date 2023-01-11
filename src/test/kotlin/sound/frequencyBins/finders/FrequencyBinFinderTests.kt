package sound.frequencyBins.finders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class FrequencyBinFinderTests {

    private val bin1 = FrequencyBin(10F, 0F)
    private val bin2 = FrequencyBin(20F, 0F)
    private val bin3 = FrequencyBin(30F, 0F)
    private val bin4 = FrequencyBin(40F, 0F)
    private val bin5 = FrequencyBin(50F, 0F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4, bin5)

    private fun createSUT(): FrequencyBinFinder {
        return FrequencyBinFinder()
    }

    @Test
    fun `find an exact frequency bin by frequency`() {
        val sut = createSUT()
        val frequencyBin = sut.findExact(30F, frequencyBins)
        assertEquals(bin3, frequencyBin)
    }

    @Test
    fun `return null when an exact match cannot be found`() {
        val sut = createSUT()
        val frequencyBin = sut.findExact(35F, frequencyBins)
        assertNull(frequencyBin)
    }

    @Test
    fun `find the lower neighbor of a frequency`() {
        val sut = createSUT()
        val frequencyBin = sut.findLowerNeighbor(30F, frequencyBins)
        assertEquals(bin2, frequencyBin)
    }

    @Test
    fun `return null when a lower neighbor cannot be found`() {
        val sut = createSUT()
        val frequencyBin = sut.findLowerNeighbor(5F, frequencyBins)
        assertNull(frequencyBin)
    }

    @Test
    fun `find the higher neighbor of a frequency`() {
        val sut = createSUT()
        val frequencyBin = sut.findHigherNeighbor(30F, frequencyBins)
        assertEquals(bin4, frequencyBin)
    }

    @Test
    fun `return null when a higher neighbor cannot be found`() {
        val sut = createSUT()
        val frequencyBin = sut.findHigherNeighbor(50F, frequencyBins)
        assertNull(frequencyBin)
    }

    @Test
    fun `find the highest frequency bin by frequency`() {
        val sut = createSUT()
        val frequencyBin = sut.findHighest(frequencyBins)
        assertEquals(bin5, frequencyBin)
    }

    @Test
    fun `return null when a highest frequency cannot be found`() {
        val sut = createSUT()
        val frequencyBin = sut.findHighest(emptyList())
        assertNull(frequencyBin)
    }

    @Test
    fun `find the lowest frequency bin by frequency`() {
        val sut = createSUT()
        val frequencyBin = sut.findLowest(frequencyBins)
        assertEquals(bin1, frequencyBin)
    }

    @Test
    fun `return null when a lowest frequency cannot be found`() {
        val sut = createSUT()
        val frequencyBin = sut.findLowest(emptyList())
        assertNull(frequencyBin)
    }

}