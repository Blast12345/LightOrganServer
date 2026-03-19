package dsp

class ZeroPaddingInterpolator {

    fun interpolate(frame: FloatArray, desiredSize: Int): FloatArray {
        if (desiredSize < frame.size) throw InvalidInterpolationSizeException(desiredSize, frame.size)

        val scale = desiredSize.toFloat() / frame.size
        val paddedFrame = frame.copyOf(desiredSize)

        for (i in frame.indices) {
            paddedFrame[i] = frame[i] * scale
        }

        return paddedFrame
    }

}

class InvalidInterpolationSizeException(
    desiredSize: Int,
    frameSize: Int
) : Exception("Desired size ($desiredSize) must be at least the frame size ($frameSize).")