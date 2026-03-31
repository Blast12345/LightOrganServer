package math

// TODO: Test me
fun nextPowerOfTwo(value: Int): Int {
    require(value > 0) { "Value must be positive, got $value" }
    if (value and (value - 1) == 0) return value
    return Integer.highestOneBit(value) shl 1
}