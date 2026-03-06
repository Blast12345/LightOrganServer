package lightOrgan.color.calculator

class BrightnessCalculatorTests {

//    private val frequencyBins = nextFrequencyBins()
//
//    private val bandPassFilter: BandPassFilter = mockk()
//    private val filteredFrequencyBins = nextFrequencyBins()
//    private val lowCrossover: Crossover = nextCrossover()
//    private val highCrossover: Crossover = nextCrossover()
//    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = mockk()
//    private val greatestMagnitude = Random.nextFloat()
//
//    private fun createSUT(): BrightnessCalculator {
//        return BrightnessCalculator(
//            bandPassFilter = bandPassFilter,
//            lowCrossover = lowCrossover,
//            highCrossover = highCrossover,
//            greatestMagnitudeFinder = greatestMagnitudeFinder
//        )
//    }
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { bandPassFilter.filter(frequencyBins, lowCrossover, highCrossover) } returns filteredFrequencyBins
//        every { greatestMagnitudeFinder.find(filteredFrequencyBins) } returns greatestMagnitude
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    @Test
//    fun `get the brightness for a list of frequency bins`() {
//        val sut = createSUT()
//
//        val brightness = sut.calculate(frequencyBins)
//
//        assertEquals(greatestMagnitude, brightness)
//    }
//
//    @Test
//    fun `given the greatest magnitude is greater than 1, then return 1`() {
//        val sut = createSUT()
//        every { greatestMagnitudeFinder.find(any()) } returns 1.5F
//
//        val brightness = sut.calculate(frequencyBins)
//
//        assertEquals(1F, brightness)
//    }
//
//    @Test
//    fun `given there is no greatest magnitude, then return null`() {
//        val sut = createSUT()
//        every { greatestMagnitudeFinder.find(any()) } returns null
//
//        val brightness = sut.calculate(frequencyBins)
//
//        assertEquals(null, brightness)
//    }

}
