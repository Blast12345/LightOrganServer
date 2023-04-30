package lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.averagePeakStrategy

import io.mockk.clearAllMocks
import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class MagnitudeAveragePeakEstimatorTests {

    private val numberOfPeaksToUse: Int = 3

    private val frequency = Random.nextFloat()
    private val bin1 = FrequencyBin(10F, 1F)
    private val bin2 = FrequencyBin(20F, 5F)
    private val bin3 = FrequencyBin(30F, 2F)
    private val bin4 = FrequencyBin(40F, 2F)
    private val bin5 = FrequencyBin(40F, 0F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4, bin5)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): MagnitudeAveragePeakEstimator {
        return MagnitudeAveragePeakEstimator(
            numberOfPeaksToUse = numberOfPeaksToUse
        )
    }

    @Test
    fun `return the average of the N loudest peaks`() {
        val sut = createSUT()
        val actual = sut.estimate(frequency, frequencyBins)
        assertEquals(3F, actual)
    }

}