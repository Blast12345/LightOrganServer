package mocking

import sound.Listener

class MockLineListener(private val fftData: DoubleArray): Listener {

    override fun getSampleRate(): Double {
        return 500.0
    }

    override fun getSampleSize(): Int {
        return 5
    }

    override fun getFftData(): DoubleArray {
        return fftData
    }

}