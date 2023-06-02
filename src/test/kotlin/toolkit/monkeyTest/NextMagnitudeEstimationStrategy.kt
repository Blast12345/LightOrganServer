package toolkit.monkeyTest

import config.children.MagnitudeEstimationStrategy

fun nextMagnitudeEstimationStrategy(): MagnitudeEstimationStrategy {
    return MagnitudeEstimationStrategy(
        numberOfPeaksToUse = nextPositiveInt()
    )
}