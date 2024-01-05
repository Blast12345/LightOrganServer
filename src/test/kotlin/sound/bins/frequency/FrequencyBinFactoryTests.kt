package sound.bins.frequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class FrequencyBinFactoryTests {

    private val index = Random.nextInt()
    private val granularity = Random.nextFloat()
    private val magnitude = Random.nextDouble()

    private fun createSUT(magnitudeMultiplier: Float = Random.nextFloat()): FrequencyBinFactory {
        return FrequencyBinFactory(
            magnitudeMultiplier = magnitudeMultiplier
        )
    }

    @Test
    fun `the frequency is calculated`() {
        val sut = createSUT()

        val actual = sut.create(2, 5F, magnitude)

        assertEquals(10F, actual.frequency)
    }

    @Test
    fun `the magnitude is calculated`() {
        val sut = createSUT(3F)

        val actual = sut.create(index, granularity, 1.0)

        assertEquals(3F, actual.magnitude)
    }

}