package sound

interface Listener {
    fun getSampleRate(): Double
    fun getSampleSize(): Int
    fun getFftData(): DoubleArray
}