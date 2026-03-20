package lightOrgan.color.calculator.hue

class OctaveHueCalculatorIntegrationTests {
//
//    @Suppress("ClassName")
//    @Nested
//    inner class `given Western tuning` {
//
//        private val c3LowPassFilter = LowPassFilter(
//            frequency = Keys.C.getFrequency(3),
//            slope = 48f,
//            thresholdDb = 48f
//        )
//
//        private fun createSUT(lowPassFilter: LowPassFilter? = null) = OctaveHueCalculator(
//            lowPassFilter = lowPassFilter,
//            tuning = Tuning.western()
//        )
//
//        // Single notes
//        @Test
//        fun `C notes are red`() {
//            val sut = createSUT()
//            val bins = createFrequencyBinsFor(Keys.C)
//
//            val actual = sut.calculate(bins)
//
//            assertCircularEquals(0F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `F# notes are teal`() {
//            val sut = createSUT()
//            val bins = createFrequencyBinsFor(Keys.F_SHARP)
//
//            val actual = sut.calculate(bins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `A notes are purple`() {
//            val sut = createSUT()
//            val bins = createFrequencyBinsFor(Keys.A)
//
//            val actual = sut.calculate(bins)
//
//            assertCircularEquals(0.75F, actual!!, 0.01F)
//        }
//
//        // Multiple notes
//        @Test
//        fun `D# and A at the same time is teal`() {
//            val sut = createSUT()
//            val dSharpBins = createFrequencyBinsFor(Keys.D_SHARP)
//            val aBins = createFrequencyBinsFor(Keys.A)
//
//            val actual = sut.calculate(dSharpBins + aBins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `F and G at the same time is teal`() {
//            val sut = createSUT()
//            val fBins = createFrequencyBinsFor(Keys.F)
//            val gBins = createFrequencyBinsFor(Keys.G)
//
//            val actual = sut.calculate(fBins + gBins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `D# and F at the same time is green`() {
//            val sut = createSUT()
//            val dSharpBins = createFrequencyBinsFor(Keys.D_SHARP)
//            val fBins = createFrequencyBinsFor(Keys.F)
//
//            val actual = sut.calculate(dSharpBins + fBins)
//
//            assertCircularEquals(0.33F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `C slightly sharp and C slightly flat average to red`() {
//            val sut = createSUT()
//            val cSlightlyFlat = createFrequencyBinsFor(Keys.C.getFrequency(3) - 1)
//            val cSlightlySharp = createFrequencyBinsFor(Keys.C.getFrequency(4) + 1)
//
//            val actual = sut.calculate(cSlightlyFlat + cSlightlySharp)
//
//            assertCircularEquals(0.0F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `two equidistant notes`() {
//            val sut = createSUT()
//            val bins1 = createFrequencyBinsFor(Keys.C.getFrequency(3))
//            val bins2 = createFrequencyBinsFor(Keys.F_SHARP.getFrequency(3))
//
//            val actual = sut.calculate(bins1 + bins2)
//
//            assertCircularEquals(0.0F, actual!!, 0.01F)
//        }
//
//        // Octaves
//        @ParameterizedTest
//        @ValueSource(ints = [-1, 0, 1, 10])
//        fun `given a single note, the hue is consistent across octaves`(octave: Int) {
//            val sut = createSUT()
//            val bins = createFrequencyBinsFor(Keys.F_SHARP, octave)
//
//            val actual = sut.calculate(bins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `given a multiple notes, the hue is consistent across octaves`() {
//            val sut = createSUT()
//            val dSharpBins = createFrequencyBinsFor(Keys.D_SHARP, 1)
//            val aBins = createFrequencyBinsFor(Keys.A, 4)
//
//            val actual = sut.calculate(dSharpBins + aBins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        // High crossover
//        @Test
//        fun `given the note is below the crossover, return the hue`() {
//            val sut = createSUT(c3LowPassFilter)
//            val bins = createFrequencyBinsFor(Keys.F_SHARP, 2)
//
//            val actual = sut.calculate(bins)
//
//            assertCircularEquals(0.5F, actual!!, 0.01F)
//        }
//
//        @Test
//        fun `given the note is far above the crossover, return null`() {
//            val sut = createSUT(c3LowPassFilter)
//            val bins = createFrequencyBinsFor(Keys.F_SHARP, 6)
//
//            val actual = sut.calculate(bins)
//
//            assertNull(actual)
//        }
//
//        // TODO: Decide how to handle peaks in rolloff range
////        @Test
////        fun `given one note is slightly above the crossover and another is below, return the average`() {
////            val sut = createSUT(c3LowPassFilter)
////            val belowBins = createFrequencyBinsFor(Keys.C, 2)
////            val aboveBins = createFrequencyBinsFor(Keys.F_SHARP, 3)
////
////            val actual = sut.calculate(belowBins + aboveBins)
////
////            assertCircularEquals(???F, actual!!, 0.01F)
////        }
//
//        // Helpers
//        private fun createFrequencyBinsFor(
//            note: Note,
//            octave: Int = 0
//        ): FrequencyBins {
//            return createFrequencyBinsFor(note.getFrequency(octave))
//        }
//
//        private fun createFrequencyBinsFor(
//            frequency: Float
//        ): FrequencyBins {
//            return listOf(
//                FrequencyBin(frequency - 1, 0F),
//                FrequencyBin(frequency, 1F),
//                FrequencyBin(frequency + 1, 0F),
//            )
//        }
//
//        fun assertCircularEquals(expected: Float, actual: Float, tolerance: Float) {
//            val delta = abs(expected - actual)
//            val circularDelta = min(delta, 1F - delta)
//
//            // Values of 0.001 and 0.999 are effectively the same hue because values wrap around at 1.
//            assertTrue(
//                circularDelta <= tolerance,
//                "Expected $expected ± $tolerance but was $actual (circular distance: $circularDelta)"
//            )
//        }
//    }


}