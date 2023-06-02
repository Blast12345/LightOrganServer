package sound.frequencyBins.filters

import config.Config
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import toolkit.monkeyTest.nextConfig
import kotlin.random.Random

class FrequencyBinDenoiserTests {

    private val config: Config = nextConfig()

    private val frequency = Random.nextFloat()
    private val magnitudeAboveNoiseFloor = config.noiseFloor + 0.1F
    private val magnitudeBelowNoiseFloor = config.noiseFloor - 0.1F
    private val frequencyBinAboveNoiseFloor = FrequencyBin(frequency, magnitudeAboveNoiseFloor)
    private val frequencyBinBelowNoiseFloor = FrequencyBin(frequency, magnitudeBelowNoiseFloor)

    private fun createSUT(): FrequencyBinDenoiser {
        return FrequencyBinDenoiser(
            config = config
        )
    }

    @Test
    fun `denoise a magnitude greater than the noise floor`() {
        val sut = createSUT()

        val actual = sut.denoise(frequencyBinAboveNoiseFloor)

        assertEquals(frequencyBinAboveNoiseFloor, actual)
    }

    @Test
    fun `denoise a magnitude less than the noise floor`() {
        val sut = createSUT()

        val actual = sut.denoise(frequencyBinBelowNoiseFloor)

        val expected = FrequencyBin(frequency, 0F)
        assertEquals(expected, actual)
    }

}