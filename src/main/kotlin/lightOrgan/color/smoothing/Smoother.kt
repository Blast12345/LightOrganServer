package math.smoothing

interface Smoother<T> {
    fun smooth(value: T): T
}