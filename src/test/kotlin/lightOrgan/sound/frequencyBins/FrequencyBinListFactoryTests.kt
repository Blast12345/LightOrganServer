package lightOrgan.sound.frequencyBins

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import kotlin.random.Random

class FrequencyBinListFactoryTests {

    private var frequencyBinFactory: FrequencyBinFactoryInterface = mockk()

    private val magnitudes = nextDoubleArray(length = 2)
    private val granularity = Random.nextFloat()
    private val bin1: FrequencyBin = mockk()
    private val bin2: FrequencyBin = mockk()

    @BeforeEach
    fun setup() {
        every { frequencyBinFactory.create(any(), any(), any()) } returnsMany listOf(bin1, bin2)
    }

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
    fun `a frequency bin is created for each normalized magnitude`() {
        val sut = createSUT()
        val frequencyBins = sut.create(magnitudes, granularity)
        val expected = listOf(bin1, bin2)
        assertEquals(expected, frequencyBins)
        verify { frequencyBinFactory.create(0, granularity, magnitudes[0]) }
        verify { frequencyBinFactory.create(1, granularity, magnitudes[1]) }
    }

}