package sound.bins

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextOctaveBin

class FrequencyBinsToOctaveBinsConverterTests {

    private val frequencyBin1 = nextFrequencyBin()
    private val frequencyBin2 = nextFrequencyBin()
    private val frequencyBin3 = nextFrequencyBin()
    private val frequencyBins = listOf(frequencyBin1, frequencyBin2, frequencyBin3)

    private val frequencyBinToOctaveBinConverter: FrequencyBinToOctaveBinConverter = mockk()
    private val octaveBin1 = nextOctaveBin()
    private val octaveBin2 = nextOctaveBin()
    private val octaveBin3 = nextOctaveBin()

    private fun createSUT(): FrequencyBinsToOctaveBinsConverter {
        return FrequencyBinsToOctaveBinsConverter(
            frequencyBinToOctaveBinConverter = frequencyBinToOctaveBinConverter
        )
    }

    @Test
    fun `convert frequency bins to octave bins`() {
        val sut = createSUT()
        every { frequencyBinToOctaveBinConverter.convert(frequencyBin1) } returns octaveBin1
        every { frequencyBinToOctaveBinConverter.convert(frequencyBin2) } returns octaveBin2
        every { frequencyBinToOctaveBinConverter.convert(frequencyBin3) } returns octaveBin3

        val actual = sut.convert(frequencyBins)

        val expected = listOf(octaveBin1, octaveBin2, octaveBin3)
        assertEquals(expected, actual)
    }

}
