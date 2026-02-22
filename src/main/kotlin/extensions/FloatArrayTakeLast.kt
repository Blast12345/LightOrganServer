package extensions

// Similar to `takeLast(): List<Float>` - but uses a copy function for performance.
fun FloatArray.takeLastArray(n: Int): FloatArray {
    if (n >= size) return this
    return copyOfRange(size - n, size)
}