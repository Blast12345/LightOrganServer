package sound.signalProcessing.zeroPaddingInterpolator

class StandardZeroPaddingInterpolator : ZeroPaddingInterpolator {

    override fun interpolate(samples: DoubleArray, desiredSize: Int): DoubleArray {
        val sizeDifference = desiredSize - samples.size

        if (sizeDifference >= 0) {
            return samples + DoubleArray(sizeDifference)
        } else {
            throw Exception("You cannot interpolate to a smaller size.")
        }
    }

}