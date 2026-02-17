package audio.audioInput

class UnsupportedBitDepthException(bitDepth: Int) : Exception("$bitDepth-bit audio is unsupported.")