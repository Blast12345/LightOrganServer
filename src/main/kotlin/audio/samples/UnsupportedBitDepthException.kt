package audio.samples

class UnsupportedBitDepthException(bitDepth: Int) : Exception("$bitDepth-bit audio is unsupported.")