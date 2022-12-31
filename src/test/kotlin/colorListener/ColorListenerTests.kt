package colorListener

import colorListener.color.FakeColorFactory
import sound.frequencyBins.FakeFrequencyBinsListener
import sound.frequencyBins.FrequencyBin
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.awt.Color

class ColorListenerTests {

    private lateinit var frequencyBinsService: FakeFrequencyBinsListener
    private lateinit var colorFactory: FakeColorFactory
    private val frequencyBins = listOf(FrequencyBin(100.0, 10.0))

    @Before
    fun setup() {
        frequencyBinsService = FakeFrequencyBinsListener()
        colorFactory = FakeColorFactory()
    }

    private fun createSUT(): ColorListener {
        return ColorListener(frequencyBinsService, colorFactory)
    }

    @Test
    fun `a color is generated when frequency bins become available`() {
        val sut = createSUT()

        var nextColor: Color? = null
        sut.listenForNextColor {
            nextColor = it
        }

        frequencyBinsService.nextFrequencyBins?.invoke(frequencyBins)

        assertEquals(colorFactory.frequencyBins, frequencyBins)
        assertEquals(nextColor, colorFactory.color)
    }

}