package input.audioFrame

class UnsupportedBitDepthException(bitDepth: Int) : Exception("$bitDepth-bit audio is unsupported.")

