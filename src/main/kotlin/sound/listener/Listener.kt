package sound.listener

interface Listener {
    fun getSampleRate(): Double
    fun getSampleSize(): Int
    fun getFftData(): DoubleArray
}