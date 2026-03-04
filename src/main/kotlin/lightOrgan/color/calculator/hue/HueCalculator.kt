package lightOrgan.color.calculator.hue

import bins.frequency.FrequencyBins

// ENHANCEMENT: Add a linear frequency calculator
// ENHANCEMENT: Add a configurable hue offset
interface HueCalculator {
    fun calculate(frequencyBins: FrequencyBins): Float?
}