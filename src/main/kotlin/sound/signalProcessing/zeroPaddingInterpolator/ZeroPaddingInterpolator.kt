package sound.signalProcessing.zeroPaddingInterpolator

interface ZeroPaddingInterpolator {
    fun interpolate(samples: DoubleArray, desiredSize: Int): DoubleArray
}