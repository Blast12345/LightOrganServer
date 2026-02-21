package input.samples

class UnsupportedBitDepthException(bitDepth: Int) : Exception("$bitDepth-bit audio is unsupported.")