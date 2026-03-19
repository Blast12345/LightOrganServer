package extensions

// TODO: Test me?
operator fun FloatArray.plus(other: FloatArray): FloatArray {
    val result = FloatArray(size + other.size)
    copyInto(result)
    other.copyInto(result, destinationOffset = size)
    return result
}