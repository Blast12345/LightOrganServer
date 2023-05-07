package sound.frequencyBins.filters

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import toolkit.monkeyTest.nextFrequencyBin

class FrequencyBinListDenoiserTests {

    private val frequencyBinDenoiser: FrequencyBinDenoiser = mockk()

    private val bin1 = FrequencyBin(10F, 1F)
    private val bin2 = FrequencyBin(20F, 2F)
    private val frequencyBins = listOf(bin1, bin2)

    private val denoisedBin1 = nextFrequencyBin()
    private val denoisedBin2 = nextFrequencyBin()
    private val denoisedFrequencyBins = listOf(denoisedBin1, denoisedBin2)

    @BeforeEach
    fun setup() {
        every { frequencyBinDenoiser.denoise(any()) } returnsMany denoisedFrequencyBins
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinListDenoiser {
        return FrequencyBinListDenoiser(
            frequencyBinDenoiser = frequencyBinDenoiser
        )
    }

    @Test
    fun `each frequency bin is denoised`() {
        val sut = createSUT()
        val actual = sut.denoise(frequencyBins)
        assertEquals(denoisedFrequencyBins, actual)
        verify { frequencyBinDenoiser.denoise(bin1) }
        verify { frequencyBinDenoiser.denoise(bin2) }
    }

}