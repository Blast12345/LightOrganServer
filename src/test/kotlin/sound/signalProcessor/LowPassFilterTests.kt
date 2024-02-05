package sound.signalProcessor

import LowPassFilter
import toolkit.generators.SineWaveGenerator

class LowPassFilterTests {

    private val sineWaveGenerator = SineWaveGenerator(100F)
    val fiftyHertz = sineWaveGenerator.generate(50F, 100)
    val silence = DoubleArray(100)

    private fun createSUT(): LowPassFilter {
        return LowPassFilter()
    }

//    @Test
//    fun `frequencies below the crossover are returned`() {
//        val sut = createSUT()
//
//        val actual = sut.filter(fiftyHertz, 40F)
//
//        assertArrayEquals(silence, actual)
//    }
//
//    @Test
//    fun `frequencies above the crossover are removed`() {
//        val sut = createSUT()
//
//        val actual = sut.filter(fiftyHertz, 60F)
//
//        assertArrayEquals(fiftyHertz, actual)
//    }

}
