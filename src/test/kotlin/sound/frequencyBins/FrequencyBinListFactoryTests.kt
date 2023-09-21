package sound.frequencyBins

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

class FrequencyBinListFactoryTests {

    private val magnitudes = nextDoubleArray(length = 2)
    private val granularity = Random.nextFloat()
    private val frequencyBinFactory: FrequencyBinFactory = mockk()
    private val frequencyBin1 = nextFrequencyBin()
    private val frequencyBin2 = nextFrequencyBin()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): FrequencyBinListFactory {
        return FrequencyBinListFactory(
            frequencyBinFactory = frequencyBinFactory
        )
    }

    @Test
    fun `create a list of frequency bins given their magnitudes and frequency granularity`() {
        val sut = createSUT()
        every { frequencyBinFactory.create(0, granularity, magnitudes[0]) } returns frequencyBin1
        every { frequencyBinFactory.create(1, granularity, magnitudes[1]) } returns frequencyBin2

        val actual = sut.create(magnitudes, granularity)

        val expected = listOf(frequencyBin1, frequencyBin2)
        assertEquals(expected, actual)
    }

}