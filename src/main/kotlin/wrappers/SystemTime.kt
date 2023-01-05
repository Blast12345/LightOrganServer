package wrappers

interface SystemTimeInterface {
    fun currentTimeInSeconds(): Double
}

// NOTE: MockK could not wrap the System function, so I needed to make my own wrapper.
class SystemTime() : SystemTimeInterface {

    override fun currentTimeInSeconds(): Double {
        return System.currentTimeMillis() / 1000.0
    }

}